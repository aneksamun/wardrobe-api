package co.uk.redpixel.wardrobe.persistence.query

import cats.implicits.toFunctorOps
import co.uk.redpixel.wardrobe.data.Clothes
import co.uk.redpixel.wardrobe.data.Search.{Limit, Offset}
import doobie.implicits._
import doobie.{ConnectionIO, Update, Write}

trait ClothesQueries {

  implicit val clothesWriter: Write[Clothes] = {
    Write[(String, String, Option[String])].contramap { fields =>
      (fields.name, fields.category.name, fields.outfit.map(_.name))
    }
  }

  def addMultipleClothes(clothes: Vector[Clothes]): ConnectionIO[Int] = {
    val sql =
      """INSERT INTO clothes(name, category, outfit) VALUES (?, ?, ?)
        |ON CONFLICT(name) DO NOTHING""".stripMargin
    Update[Clothes](sql).updateMany(clothes)
  }

  def updateClothes(clothes: Clothes): ConnectionIO[Unit] = {
    val maybeOutfit = clothes.outfit.map(_.name)

    sql"""
         UPDATE clothes SET category = ${clothes.category.name}, outfit = $maybeOutfit
         WHERE name = ${clothes.name}
         """.update.run.void
  }

  def findClothesByName(name: String): ConnectionIO[Option[Clothes]] =
    sql"""
       SELECT name, category, outfit FROM clothes
       WHERE lower(name) = ${name.toLowerCase}
       LIMIT 1
       """.query[Clothes].option

  def findAllClothes(offset: Offset, limit: Limit): ConnectionIO[Seq[Clothes]] =
    sql"""
         SELECT name, category, outfit FROM clothes
         ORDER BY name
         LIMIT ${limit.value} OFFSET ${offset.value}
         """.query[Clothes].to[Seq]
}
