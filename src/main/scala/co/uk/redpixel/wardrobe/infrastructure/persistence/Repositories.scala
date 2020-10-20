package co.uk.redpixel.wardrobe.infrastructure.persistence

import cats.implicits.toFunctorOps
import co.uk.redpixel.wardrobe.domain.model.{Category, Clothes, Outfit}
import co.uk.redpixel.wardrobe.domain.persistence._
import co.uk.redpixel.wardrobe.http.Offset
import co.uk.redpixel.wardrobe.http.search.{Limit, Offset}
import doobie.ConnectionIO
import doobie.implicits._

class DoobieCategoryRepository extends CategoryRepository[ConnectionIO] {

  def add(category: Category): ConnectionIO[Unit] =
    sql"""
         INSERT INTO categories(name)
         VALUES (${category.name})
         ON CONFLICT(name) DO NOTHING
         """.update.run.void
}

class DoobieOutfitRepository extends OutfitRepository[ConnectionIO] {

  def add(outfit: Outfit): ConnectionIO[Unit] =
    sql"""
         INSERT INTO outfits(name)
         VALUES (${outfit.name})
         ON CONFLICT(name) DO NOTHING
         """.update.run.void
}

class DoobieClothesRepository extends ClothesRepository[ConnectionIO] {

  def upsert(clothes: Clothes): ConnectionIO[Unit] = {
    val maybeCategory = clothes.category.map(_.name)
    val maybeOutfit = clothes.outfit.map(_.name)

    sql"""
         INSERT INTO clothes(name, category, outfit)
         VALUES (${clothes.name}, $maybeCategory, $maybeOutfit)
         ON CONFLICT(name)
         DO UPDATE SET category = $maybeCategory, outfit = $maybeOutfit
         """.update.run.void
  }

  def findByName(name: String): ConnectionIO[Option[Clothes]] = {
    sql"""
         SELECT name, category, outfit FROM clothes
         WHERE lower(name) = ${name.toLowerCase}
         LIMIT 1
         """.stripMargin.query[Clothes].option
  }

  def findAll(offset: Offset, limit: Limit): ConnectionIO[Seq[Clothes]] =
    sql"""
         SELECT name, category, outfit FROM clothes
         ORDER BY name
         LIMIT $limit OFFSET $offset
         """.query[Clothes].to[Seq]
}
