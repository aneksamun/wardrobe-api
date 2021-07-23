package co.uk.redpixel.wardrobe.persistence

import cats.data.{EitherT, OptionT}
import cats.effect.Effect
import cats.implicits.toFunctorOps
import cats.syntax.all._
import co.uk.redpixel.wardrobe.algebra._
import co.uk.redpixel.wardrobe.data.search.{Limit, Offset}
import co.uk.redpixel.wardrobe.data.{Category, Clothes, Outfit}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.{ConnectionIO, Update}

class DoobieClothesStore[F[_] : Effect](xa: HikariTransactor[F]) extends ClothesStore[F] {

  import DoobieClothesStore._

  def isAlive: F[Boolean] =
    pingQuery transact xa

  def add(clothes: Seq[Clothes]): F[Total] = {
    clothes.groupBy(_.category).map { grouping =>
      val category = grouping._1
      val clothes = grouping._2
      val query =
        insertCategoryQuery(category) *>
          addMultipleClothesQuery(clothes)

      query transact xa
    } reduceLeft { (x, y) =>
      (x, y) mapN (_ + _)
    }
  }

  def find(name: String): OptionT[F, Clothes] = OptionT(
    findClothesByNameQuery(name) transact xa
  )

  def tag(name: String, outfit: Outfit): EitherT[F, OutfitTagError, Clothes] = {
    for {
      clothes <- EitherT.fromOptionF(
        findClothesByNameQuery(name) transact xa,
        OutfitTagError(s"A '$name' clothes not found")
      )

      outfitted <- EitherT.fromEither[F](
        Either.catchNonFatal {
          val outfitted = clothes copy (outfit = outfit.some)
          insertOutfitQuery(outfit) >> updateClothesQuery(outfitted) transact xa
          outfitted
        } leftMap(throwable => OutfitTagError(throwable.getMessage))
      )

    } yield outfitted
  }

  def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]] =
    findAllClothesQuery(offset, limit) transact xa

  def countAll: F[Total] =
    countAllQuery transact xa
}

object DoobieClothesStore {

  def apply[F[_]: Effect](xa: HikariTransactor[F]) =
    new DoobieClothesStore[F](xa)

  // --ping--
  def pingQuery: ConnectionIO[Boolean] =
    sql"SELECT 1"
      .query[Int].map(_ == 1)
      .unique

  // --category--
  def insertCategoryQuery(category: Category): ConnectionIO[Unit] =
    sql"""
       INSERT INTO categories(name)
       VALUES (${category.name})
       ON CONFLICT(name) DO NOTHING
       """.update.run.void

  // --outfit--
  def insertOutfitQuery(outfit: Outfit): ConnectionIO[Unit] =
    sql"""
       INSERT INTO outfits(name)
       VALUES (${outfit.name})
       ON CONFLICT(name) DO NOTHING
       """.update.run.void

  // --clothes--
  def addMultipleClothesQuery(clothes: Seq[Clothes]): ConnectionIO[Int] =
    Update[Clothes](
      """INSERT INTO clothes(name, category, outfit)
        |VALUES (?, ?, ?)
        |ON CONFLICT(name) DO NOTHING""".stripMargin
    ).updateMany(clothes)

  def updateClothesQuery(clothes: Clothes): ConnectionIO[Unit] =
    sql"""
       UPDATE clothes SET category = ${clothes.category.name}, outfit = ${clothes.outfit.map(_.name)}
       WHERE name = ${clothes.name}
       """.update.run.void

  def findClothesByNameQuery(name: String): ConnectionIO[Option[Clothes]] =
    sql"""
       SELECT name, category, outfit FROM clothes
       WHERE lower(name) = ${name.toLowerCase}
       LIMIT 1
       """.query[Clothes].option

  def findAllClothesQuery(offset: Offset, limit: Limit): ConnectionIO[Seq[Clothes]] =
    sql"""
       SELECT name, category, outfit FROM clothes
       ORDER BY name, category
       LIMIT ${limit.value} OFFSET ${offset * limit}
       """.query[Clothes].to[Seq]

  def countAllQuery: ConnectionIO[Total] =
    sql"SELECT count(*) FROM clothes"
      .query[Total]
      .unique
}
