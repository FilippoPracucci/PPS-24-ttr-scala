package controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TurnManagerTest extends AnyFlatSpec with Matchers:
  import model.cards.Deck
  import model.player.Player
  import model.utils.PlayerColor

  private val deck: Deck = Deck()

  private var playerList: List[Player] = List.empty
  for
    color <- PlayerColor.values
  yield playerList :+= Player(color, deck)

  private val turnManager = TurnManager(playerList)

  "A turn manager" should "be created correctly" in:
    turnManager.players should contain theSameElementsInOrderAs playerList
    turnManager.currentPlayer should be(playerList.head)

  it should "be possible to switch player turn" in:
    turnManager.currentPlayer should be(playerList.head)
    turnManager.switchTurn()
    turnManager.currentPlayer should be(playerList(1))

  it should "return the same player after a full round of player turns" in:
    playerList.foreach(_ => turnManager.switchTurn())
    turnManager.currentPlayer should be(playerList(1))
