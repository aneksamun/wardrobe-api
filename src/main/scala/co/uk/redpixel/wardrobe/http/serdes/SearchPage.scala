package co.uk.redpixel.wardrobe.http.serdes

import co.uk.redpixel.wardrobe.algebra.Total
import co.uk.redpixel.wardrobe.data.Clothes

final case class SearchPage(items: Seq[Clothes], total: Total)
