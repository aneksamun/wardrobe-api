package co.uk.redpixel.wardrobe.persistence.service

import cats.effect.Effect
import cats.syntax.all._
import co.uk.redpixel.wardrobe.data.Search.{Limit, Offset}
import co.uk.redpixel.wardrobe.data.{Clothes, Outfit}
import co.uk.redpixel.wardrobe.persistence.query._
import doobie.hikari.HikariTransactor
import doobie.implicits._

trait ClothingAlg[F[_]] {

  type Total = Int

  def add(data: Vector[String]): F[Total]

  def tag(name: String, outfit: Outfit): F[Option[Clothes]]

  def find(name: String): F[Option[Clothes]]

  def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]]
}

object ClothingAlg {

  def impl[F[_]: Effect](xa: HikariTransactor[F]): ClothingAlg[F] = new ClothingAlg[F] {

    def add(data: Vector[String]): F[Total] = {
      import co.uk.redpixel.wardrobe.data.csv.instances._
      import co.uk.redpixel.wardrobe.data.csv.syntax._

      val clothes = data.as[Clothes]

      clothes.groupBy(_.category.get).map { grouped =>
        val category = grouped._1
        val clothes = grouped._2

        val query = {
          for {
            _     <- insertCategory(category)
            count <- addMultipleClothes(clothes)
          } yield count
        }

        query.transact(xa)

      }.reduceLeft { (x, y) =>
        (x, y).mapN(_ + _)
      }
    }

    def tag(name: String, outfit: Outfit): F[Option[Clothes]] = {
      def update(clothes: Clothes): F[Option[Clothes]] =
        (insertOutfit(clothes.outfit.get) *> updateClothes(clothes) *> findClothesByName(clothes.name))
          .transact(xa)

      for {
        clothesOpt <- findClothesByName(name).transact(xa)
        taggedOpt = clothesOpt.map(_.copy(outfit = outfit.some))
        x <- taggedOpt.map(update).getOrElse(clothesOpt.pure[F])
      } yield x
    }

    def find(name: String): F[Option[Clothes]] = {
      findClothesByName(name).transact(xa)
    }

    def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]] = {
      findAllClothes(offset, limit).transact(xa)
    }
  }
}
