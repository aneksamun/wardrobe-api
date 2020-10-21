package co.uk.redpixel.wardrobe.persistence.services

import cats.Applicative
import cats.effect.Resource
import cats.implicits._
import co.uk.redpixel.wardrobe.data.{Category, Clothes}
import doobie.hikari.HikariTransactor

import co.uk.redpixel.wardrobe.persistence.queries._

trait ClothingAlg[F[_]] {

  def add(data: Seq[String]): F[Int]

  def tag(): F[Unit]
}

object ClothingAlg {

  def impl[F[_] : Applicative](resource: Resource[F, HikariTransactor[F]]): ClothingAlg[F] = new ClothingAlg[F] {

    def add(data: Seq[String]): F[Int] = {
      import co.uk.redpixel.wardrobe.data.csv._

      val clothes = data.as[Clothes]

      resource.use { transactor =>
        clothes.groupBy(_.category.get).foldLeft(0) { (count, grouped) =>
          val category = grouped._1
          val clothes = grouped._2

          for {
            _ <- insertCategory(category)
            _ <- upsertClothes(clothes)
          }







          count + clothes.size
        }
      }

//      data.as[Clothes].groupBy(_.category).foldLeft(0) { (count, grouped) =>
//        val category = grouped._1
//        val clothes = grouped._2
//
//
//        count + clothes.size
//
//      }.pure[F]
    }

    def tag(): F[Unit] = ???
  }
}
