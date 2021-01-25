package co.uk.redpixel.wardrobe.data

import eu.timepit.refined.types.all.{NonNegInt, PosInt}

object Search {

  type Limit  = PosInt
  type Offset = NonNegInt
}
