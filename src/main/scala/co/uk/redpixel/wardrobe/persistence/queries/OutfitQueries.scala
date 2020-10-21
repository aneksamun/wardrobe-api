package co.uk.redpixel.wardrobe.persistence.queries

import cats.implicits.toFunctorOps
import co.uk.redpixel.wardrobe.data.Outfit
import doobie.ConnectionIO
import doobie.implicits._

private[persistence] trait OutfitQueries {

  def insertOutfit(outfit: Outfit): ConnectionIO[Unit] =
    sql"""
         INSERT INTO outfits(name)
         VALUES (${outfit.name})
         ON CONFLICT(name) DO NOTHING
         """.update.run.void
}
