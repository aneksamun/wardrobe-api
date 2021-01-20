package co.uk.redpixel.wardrobe.persistence.query

import cats.implicits.toFunctorOps
import co.uk.redpixel.wardrobe.data.Category
import doobie.ConnectionIO
import doobie.implicits._

trait CategoryQueries {

  def insertCategory(category: Category): ConnectionIO[Unit] =
    sql"""
         INSERT INTO categories(name)
         VALUES (${category.name})
         ON CONFLICT(name) DO NOTHING
         """.update.run.void
}
