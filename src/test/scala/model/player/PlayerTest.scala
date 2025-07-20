package model.player

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:

  import model.cards.Card
  import model.utils.{PlayerColor, Color}
  val id: PlayerColor = PlayerColor.BLUE
  val objective: ObjectiveCompletion = ObjectiveWithCompletion(("Paris", "Berlin"), 8)
  var player: Player = Player(id, objective = objective)
  val NUMBER_TRAIN_CARS = 45

  override def beforeEach(): Unit = player = Player(id, objective = objective)

  "A player" should "be created correctly in the standard mode" in:
    player.id should be(id)
    player.hand.cards should not be empty
    player.objective should be(objective)
    player.trains.trainCars should be(NUMBER_TRAIN_CARS)

  it should "be created correctly using a custom deck" in:
    import Color.*
    import model.cards.{Deck, CardsGenerator}
    val fixedList: List[Card] = List(Card(BLACK), Card(BLUE), Card(RED), Card(RED), Card(YELLOW))
    val deckFixed: Deck = Deck()(using () => fixedList)
    val customPlayer: Player = Player(id, deckFixed, objective = objective)
    customPlayer.id should be(id)
    customPlayer.hand.cards should be(fixedList.take(4))
    customPlayer.objective should be(objective)
    customPlayer.trains.trainCars should be(NUMBER_TRAIN_CARS)

  it should "be able to place train cars" in:
    player.trains.placeTrainCars(5)
    player.trains.trainCars should be(NUMBER_TRAIN_CARS - 5)
    a[IllegalArgumentException] should be thrownBy player.trains.placeTrainCars(NUMBER_TRAIN_CARS)

  it should "be able to draw cards from the deck" in:
    import model.cards.Deck
    val initialHandCards = player.hand.cards
    val numberCardsToDraw = 2
    player.drawCards(numberCardsToDraw)
    player.hand.cards should be(initialHandCards :++ Deck().cards.take(numberCardsToDraw))

  it should "be able to check whether certain cards can be played" in:
    val color = Color.RED
    val n = 10
    player.canPlayCards(color, n) should be(false)
    player.hand.addCards(List.fill(n)(Card(Color.RED)))
    player.canPlayCards(color, n) should be(true)

  it should "be able to play cards" in:
    val color = Color.RED
    val n = 10
    player.playCards(color, n) should be(Left(Player.NotEnoughCards))
    player.hand.addCards(List.fill(n)(Card(Color.RED)))
    player.playCards(color, n) should be(Right(()))

  // TODO check on trains?

  it should "be able to place trains" in: // TODO to review
    val validN = 3
    val invalidN = NUMBER_TRAIN_CARS + 1
    player.placeTrains(validN) should be(Right(()))
    player.placeTrains(invalidN) should be(Left(Player.NotEnoughTrains))
