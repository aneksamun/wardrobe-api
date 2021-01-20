package co.uk.redpixel.wardrobe.persistence.query

import cats.implicits.toFunctorOps
import co.uk.redpixel.wardrobe.data.Outfit
import doobie.ConnectionIO
import doobie.implicits._

trait OutfitQueries {

  def insertOutfit(outfit: Outfit): ConnectionIO[Unit] =
    sql"""
         INSERT INTO outfits(name)
         VALUES (${outfit.name})
         ON CONFLICT(name) DO NOTHING
         """.update.run.void
}
