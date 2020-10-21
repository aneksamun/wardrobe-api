package co.uk.redpixel.wardrobe.persistence.services

import cats.effect.{ConcurrentEffect, Resource}
import cats.implicits._
import co.uk.redpixel.wardrobe.data.{Clothes, Limit, Offset, Outfit}
import co.uk.redpixel.wardrobe.persistence.queries._
import doobie.hikari.HikariTransactor
import doobie.implicits._

trait ClothingAlg[F[_]] {

  def add(data: Vector[String]): F[Int]

  def tag(name: String, outfit: Outfit): F[Option[Clothes]]

  def find(name: String): F[Option[Clothes]]

  def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]]
}

object ClothingAlg {

  def impl[F[_] : ConcurrentEffect](resource: Resource[F, HikariTransactor[F]]): ClothingAlg[F] = new ClothingAlg[F] {

    def add(data: Vector[String]): F[Int] = {
      import co.uk.redpixel.wardrobe.data.csv._

      val clothes = data.as[Clothes]

      resource.use { xa =>
        clothes.groupBy(_.category.get).map { grouped =>
          val category = grouped._1
          val clothes = grouped._2

          val query = {
            for {
              _ <- insertCategory(category)
              count <- addMultipleClothes(clothes)
            } yield count
          }

          query.transact(xa)

        }.reduceLeft { (x, y) =>
          (x, y).mapN(_ + _)
        }
      }
    }

    def tag(name: String, outfit: Outfit): F[Option[Clothes]] = {
      resource.use { xa =>
        def update(clothes: Clothes): F[Option[Clothes]] =
          (insertOutfit(clothes.outfit.get) *> updateClothes(clothes) *> findClothesByName(clothes.name))
            .transact(xa)

        for {
          clothesOpt <- findClothesByName(name).transact(xa)
          taggedOpt = clothesOpt.map(_.copy(outfit = outfit.some))
          x <- taggedOpt.map(update).getOrElse(clothesOpt.pure[F])
        } yield x
      }
    }

    def find(name: String): F[Option[Clothes]] = {
      resource.use { xa =>
        findClothesByName(name).transact(xa)
      }
    }

    def findAll(offset: Offset, limit: Limit): F[Seq[Clothes]] = {
      resource.use { xa =>
        findAllClothes(offset, limit).transact(xa)
      }
    }
  }
}
