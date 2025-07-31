package model.player

import model.objective.{ObjectiveCompletion, ObjectiveWithCompletion, Points}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:
  import model.cards.{Card, Deck}
  import Deck.DeckGenerator
  import model.utils.{PlayerColor, Color}
  import Player.*
  import config.GameConfig.{HandInitialSize, NumberTrainCars, InitialScore}

  private val id: PlayerId = PlayerColor.BLUE
  private val objective: ObjectiveCompletion = ObjectiveWithCompletion(("Paris", "Berlin"), 8)
  private var player: Player = Player(id, objective = objective)

  private val FixedDeckInitialSize = 10
  private val SingleColor = Color.RED

  override def beforeEach(): Unit = player = Player(id, objective = objective)

  "A player" should "be created correctly in the standard mode" in:
    player.id should be(id)
    player.hand.size should be(HandInitialSize)
    player.objective should be(objective)
    player.trains should be(NumberTrainCars)
    player.score should be(InitialScore)

  it should "be created correctly using a custom deck" in:
    import Color.*
    val fixedList: List[Card] = List(Card(BLACK), Card(BLUE), Card(RED), Card(RED), Card(YELLOW))
    val customPlayer: Player = Player(id, Deck()(using () => fixedList), objective = objective)
    customPlayer.id should be(id)
    customPlayer.hand should be(fixedList.take(4))
    customPlayer.objective should be(objective)
    customPlayer.trains should be(NumberTrainCars)
    customPlayer.score should be(InitialScore)

  it should "be able to draw cards from the deck" in:
    import config.GameConfig.StandardNumberOfCardsToDraw
    val initialHandCards = player.hand
    player.drawCards(StandardNumberOfCardsToDraw) should be(Right(()))
    player.hand should be(Deck().cards.take(StandardNumberOfCardsToDraw) ++: initialHandCards)
    player.drawCards(Deck().cards.size + 1) should be(Left(NotEnoughCardsInTheDeck))

  it should "be able to check and place trains" in:
    val NumberTrainsCarsToPlace: Trains = 4
    player.canPlaceTrains(NumberTrainsCarsToPlace) should be(true)
    player.placeTrains(NumberTrainsCarsToPlace) should be(Right(()))
    player.trains should be(NumberTrainCars - NumberTrainsCarsToPlace)
    player.canPlaceTrains(NumberTrainCars) should be(false)
    player.placeTrains(NumberTrainCars) should be(Left(NotEnoughTrains))
    player.trains should be(NumberTrainCars - NumberTrainsCarsToPlace)

  private def singleColoredDeck: Deck = Deck()(using () => List.fill(FixedDeckInitialSize)(Card(SingleColor)))

  private def playerWithFixedHand(fixedDeck: Deck): Player = Player(id, fixedDeck, objective)

  it should "be able to check and play cards" in:
    val playerWithSingleColor = playerWithFixedHand(singleColoredDeck)
    val nCardsToPlay = HandInitialSize - 1
    playerWithSingleColor.canPlayCards(SingleColor, nCardsToPlay) should be(true)
    playerWithSingleColor.playCards(SingleColor, nCardsToPlay) should be(Right(()))
    playerWithSingleColor.hand.size should be(HandInitialSize - nCardsToPlay)
    playerWithSingleColor.canPlayCards(SingleColor, nCardsToPlay) should be(false)
    playerWithSingleColor.playCards(SingleColor, nCardsToPlay) should be(Left(NotEnoughCards))
    playerWithSingleColor.hand.size should be(HandInitialSize - nCardsToPlay)

  it should "correctly play cards and reinsert them into the deck" in:
    val deckWithSingleColor = singleColoredDeck
    val playerWithSingleColor = playerWithFixedHand(deckWithSingleColor)
    val nCardsToPlay: Int = HandInitialSize / 2
    playerWithSingleColor.playCards(SingleColor, nCardsToPlay) should be(Right(()))
    deckWithSingleColor.cards.size + playerWithSingleColor.hand.size should be(FixedDeckInitialSize)
    playerWithSingleColor.playCards(SingleColor, HandInitialSize) should be(Left(NotEnoughCards))
    deckWithSingleColor.cards.size + playerWithSingleColor.hand.size should be(FixedDeckInitialSize)

  it should "correctly update the score" in:
    val PointsToAdd: Points = 5
    player.score should be(InitialScore)
    player.addPoints(PointsToAdd)
    player.score should be(InitialScore + PointsToAdd)

  it should "fail when given negative numbers" in:
    val IllegalNumber = -5
    an[IllegalArgumentException] should be thrownBy allOf(
      player.canPlaceTrains(IllegalNumber),
      player.placeTrains(IllegalNumber),
      player.canPlayCards(SingleColor, IllegalNumber),
      player.playCards(SingleColor, IllegalNumber),
      player.addPoints(IllegalNumber)
    )
