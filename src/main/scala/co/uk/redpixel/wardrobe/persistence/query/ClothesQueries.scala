package co.uk.redpixel.wardrobe.persistence.query

import co.uk.redpixel.wardrobe.data.Clothes
import doobie.Write

trait ClothesQueries {

  implicit val clothesWriter: Write[Clothes] = {
    Write[(String, String, Option[String])].contramap { fields =>
      (fields.name, fields.category.name, fields.outfit.map(_.name))
    }
  }
}
