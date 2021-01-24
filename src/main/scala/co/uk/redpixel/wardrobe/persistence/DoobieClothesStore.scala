package co.uk.redpixel.wardrobe.persistence

import cats.data.{EitherT, OptionT}
import cats.effect.Effect
import cats.implicits.toFunctorOps
import cats.syntax.all._
import co.uk.redpixel.wardrobe.algebra._
import co.uk.redpixel.wardrobe.data.Search.{Limit, Offset}
import co.uk.redpixel.wardrobe.data.{Category, Clothes, Outfit}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.{ConnectionIO, Update}

class DoobieClothesStore[F[_] : Effect](xa: HikariTransactor[F]) extends ClothesStore[F] {

  import DoobieClothesStore._

  def add(clothes: Vector[Clothes]): F[Total] = {
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

  def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]] =
    findAllClothesQuery(offset, limit) transact xa

  def countAll(): F[Total] =
    countAllQuery() transact xa

  def tag(name: String, outfit: Outfit): EitherT[F, OutfitTagError, Clothes] = ???
}

object DoobieClothesStore {

  def apply[F[_]: Effect](xa: HikariTransactor[F]) =
    new DoobieClothesStore[F](xa)

  // --clothes--
  def addMultipleClothesQuery(clothes: Vector[Clothes]): ConnectionIO[Int] =
    Update[Clothes](
      """INSERT INTO clothes(name, category, outfit) VALUES (?, ?, ?)
        |ON CONFLICT(name) DO NOTHING""".stripMargin
    ).updateMany(clothes)

  def findClothesByNameQuery(name: String): ConnectionIO[Option[Clothes]] =
    sql"""
       SELECT name, category, outfit FROM clothes
       WHERE lower(name) = ${name.toLowerCase}
       LIMIT 1
       """.query[Clothes].option

  def findAllClothesQuery(offset: Offset, limit: Limit): ConnectionIO[Seq[Clothes]] =
    sql"""
       SELECT name, category, outfit FROM clothes
       ORDER BY name
       LIMIT ${limit.value} OFFSET ${offset.value}
       """.query[Clothes].to[Seq]

  def countAllQuery(): ConnectionIO[Total] =
    sql"SELECT count(*) FROM clothes"
      .query[Total]
      .unique

  // --category--
  def insertCategoryQuery(category: Category): ConnectionIO[Unit] =
    sql"""
       INSERT INTO categories(name)
       VALUES (${category.name})
       ON CONFLICT(name) DO NOTHING
       """.update.run.void
}