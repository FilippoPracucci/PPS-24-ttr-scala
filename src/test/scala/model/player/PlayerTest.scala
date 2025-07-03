package model.player

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerTest extends AnyFlatSpec with Matchers:

  val id: Int = 0
  val player: Player = Player(id)
  val NUMBER_TRAIN_CARS = 45

  "A player" should "be created correctly in the standard mode" in:
    player.id should be(id)
    player.deck.cards should not be empty
    player.hand.cards should not be empty
    // TODO: player.objective should not be empty
    player.trains.trainCars should be(NUMBER_TRAIN_CARS)

  it should "be created correctly using a custom deck" in:
    import model.utils.Color
    import Color.*
    import model.cards.{Card, Deck, CardsGenerator}
    val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLACK), Card(BLUE))
    val deckFixed: Deck = Deck()(using () => fixedList)
    val customPlayer: Player = Player(1, deckFixed)
    customPlayer.id should be(1)
    customPlayer.deck should be(deckFixed)
    customPlayer.hand.cards should be(fixedList.take(4))
    // TODO: player.objective should not be empty
    customPlayer.trains.trainCars should be(NUMBER_TRAIN_CARS)

  it should "be able to place train cars" in:
    player.trains.placeTrainCars(5)
    player.trains.trainCars should be(NUMBER_TRAIN_CARS - 5)
    a[IllegalArgumentException] should be thrownBy player.trains.placeTrainCars(NUMBER_TRAIN_CARS)
