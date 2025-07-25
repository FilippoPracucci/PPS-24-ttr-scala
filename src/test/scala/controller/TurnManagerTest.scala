package controller

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TurnManagerTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:
  import model.cards.Deck
  import model.player.{Player, ObjectiveWithCompletion}
  import model.utils.PlayerColor

  private val deck: Deck = Deck()

  private var playerList: List[Player] = List.empty
  for
    color <- PlayerColor.values
  yield playerList :+= Player(color, deck, objective = ObjectiveWithCompletion(("Paris", "Berlin"), 8))

  private var turnManager = TurnManager(playerList)

  override def beforeEach(): Unit = turnManager = TurnManager(playerList)

  "A turn manager" should "have the right current player" in:
    turnManager.currentPlayer should be(playerList.head)

  it should "be possible to switch player turn" in:
    turnManager.currentPlayer should be(playerList.head)
    turnManager.switchTurn()
    turnManager.currentPlayer should be(playerList(1))

  it should "return the same player after a full round of player turns" in:
    playerList.foreach(_ => turnManager.switchTurn())
    turnManager.currentPlayer should be(playerList.head)

  it should "return the right game state" in:
    import GameState.*
    import config.GameConfig.NumberTrainCars
    turnManager.gameState should be(IN_GAME)
    turnManager.switchTurn()
    turnManager.currentPlayer.placeTrains(NumberTrainCars - 1)
    turnManager.switchTurn()
    turnManager.gameState should be(START_LAST_ROUND)
    playerList.foreach(_ => turnManager.switchTurn())
    turnManager.gameState should be(END_GAME)
