package co.uk.redpixel.wardrobe.persistence.service

//import cats.effect.Effect
//import cats.syntax.all._
//import co.uk.redpixel.wardrobe.data.Search.{Limit, Offset}
//import co.uk.redpixel.wardrobe.data.{Clothes, Outfit}
//import co.uk.redpixel.wardrobe.persistence.query._
//import doobie.hikari.HikariTransactor
//import doobie.implicits._

object ClothingAlg {

//  def impl[F[_]: Effect](xa: HikariTransactor[F]): ClothingAlg[F] = new ClothingAlg[F] {
//
//    def tag(name: String, outfit: Outfit): F[Option[Clothes]] = {
//      def update(clothes: Clothes): F[Option[Clothes]] =
//        (insertOutfit(clothes.outfit.get) *> updateClothes(clothes) *> findClothesByName(clothes.name))
//          .transact(xa)
//
//      for {
//        clothesOpt <- findClothesByName(name).transact(xa)
//        taggedOpt = clothesOpt.map(_.copy(outfit = outfit.some))
//        x <- taggedOpt.map(update).getOrElse(clothesOpt.pure[F])
//      } yield x
//    }
//  }
}
