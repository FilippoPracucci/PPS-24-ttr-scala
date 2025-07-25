package model.player

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:
  import model.cards.Card
  import model.utils.{PlayerColor, Color}
  import config.GameConfig.NumberTrainCars

  val id: PlayerColor = PlayerColor.BLUE
  val objective: ObjectiveCompletion = ObjectiveWithCompletion(("Paris", "Berlin"), 8)
  var player: Player = Player(id, objective = objective)

  override def beforeEach(): Unit = player = Player(id, objective = objective)

  "A player" should "be created correctly in the standard mode" in:
    player.id should be(id)
    player.hand.cards should not be empty
    player.objective should be(objective)
    player.trains should be(NumberTrainCars)

  it should "be created correctly using a custom deck" in:
    import Color.*
    import model.cards.{Deck, CardsGenerator}
    val fixedList: List[Card] = List(Card(BLACK), Card(BLUE), Card(RED), Card(RED), Card(YELLOW))
    val deckFixed: Deck = Deck()(using () => fixedList)
    val customPlayer: Player = Player(id, deckFixed, objective = objective)
    customPlayer.id should be(id)
    customPlayer.hand.cards should be(fixedList.take(4))
    customPlayer.objective should be(objective)
    customPlayer.trains should be(NumberTrainCars)

  it should "be able to place train cars" in:
    player.placeTrains(5)
    player.trains should be(NumberTrainCars - 5)
    player.placeTrains(NumberTrainCars) should be(Left(Player.NotEnoughTrains))

  it should "be able to draw cards from the deck" in:
    import model.cards.Deck
    import config.GameConfig.StandardNumberOfCardsToDraw
    val initialHandCards = player.hand.cards
    player.drawCards(StandardNumberOfCardsToDraw) should be(Right(()))
    player.hand.cards should be(initialHandCards :++ Deck().cards.take(StandardNumberOfCardsToDraw))
    player.drawCards(Deck().cards.size + 1) should be(Left(Player.NotEnoughCardsInTheDeck))

  val nCards = 10
  val color: Color = Color.RED

  it should "be able to check whether certain cards can be played" in:
    player.canPlayCards(color, nCards) should be(false)
    player.hand.addCards(List.fill(nCards)(Card(Color.RED)))
    player.canPlayCards(color, nCards) should be(true)

  it should "be able to play cards" in:
    player.playCards(color, nCards) should be(Left(Player.NotEnoughCards))
    player.hand.addCards(List.fill(nCards)(Card(Color.RED)))
    player.playCards(color, nCards) should be(Right(()))

  it should "correctly play cards and reinsert them into the deck" in:
    val deckFixed = model.cards.Deck()(using () => List.fill(nCards)(Card(color)))
    val customPlayer: Player = Player(id, deckFixed, objective = objective)
    val nCardsToPlay = 2
    customPlayer.playCards(color, nCardsToPlay)
    deckFixed.cards.size + customPlayer.hand.cards.size should be(nCards)
    customPlayer.playCards(color, nCardsToPlay)
    deckFixed.cards.size + customPlayer.hand.cards.size should be(nCards)

  // TODO check on trains?

  it should "be able to place trains" in: // TODO to review
    val validN = 3
    val invalidN = NumberTrainCars + 1
    player.placeTrains(validN) should be(Right(()))
    player.placeTrains(invalidN) should be(Left(Player.NotEnoughTrains))

  it should "correctly update the score" in:
    val initialScore = 0
    val pointsToAdd = 5
    player.score should be(initialScore)
    player.addPoints(pointsToAdd)
    player.score should be(initialScore + pointsToAdd)

  it should "fail to add negative points to the score" in:
    val pointsToAdd = -5
    an[IllegalArgumentException] should be thrownBy player.addPoints(pointsToAdd)
