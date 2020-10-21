package co.uk.redpixel.wardrobe.persistence.queries

import cats.implicits.toFunctorOps
import co.uk.redpixel.wardrobe.data.{Clothes, Limit, Offset}
import doobie.ConnectionIO
import doobie.implicits._

private[persistence] trait ClothesQueries {

  def upsertClothes(clothes: Clothes): ConnectionIO[Unit] = {
    val maybeCategory = clothes.maybeCategoryName()
    val maybeOutfit = clothes.maybeOutfitName()

    sql"""
         INSERT INTO clothes(name, category, outfit)
         VALUES (${clothes.name}, $maybeCategory, $maybeOutfit)
         ON CONFLICT(name)
         DO UPDATE SET category = $maybeCategory, outfit = $maybeOutfit
         """.update.run.void
  }

  def updateClothes(clothes: Clothes): ConnectionIO[Unit] = {
    val maybeCategory = clothes.maybeCategoryName()
    val maybeOutfit = clothes.maybeOutfitName()

    sql"""
         UPDATE clothes SET category = $maybeCategory, outfit = $maybeOutfit
         WHERE name = ${clothes.name}
         """.update.run.void
  }

  def findClothesByName(name: String): ConnectionIO[Option[Clothes]] =
    sql"""
       SELECT name, category, outfit FROM clothes
       WHERE lower(name) = ${name.toLowerCase}
       LIMIT 1
       """.stripMargin.query[Clothes].option

  def findAllClothes(offset: Offset, limit: Limit): ConnectionIO[Seq[Clothes]] =
    sql"""
         SELECT name, category, outfit FROM clothes
         ORDER BY name
         LIMIT $limit OFFSET $offset
         """.query[Clothes].to[Seq]
}
