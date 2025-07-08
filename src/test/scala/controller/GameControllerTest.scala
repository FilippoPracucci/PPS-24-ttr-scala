package controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameControllerTest extends AnyFlatSpec with Matchers:
  import model.cards.Deck
  import model.player.Player

  val gameController: GameController = GameController()

  "A game controller" should "have the right instances reference" in:
    gameController.deck shouldBe a[Deck]
    gameController.players shouldBe a[List[Player]]

  it should "draw cards correctly" in:
    val numberCardsToDraw = 2
    val initialCards = gameController.players.head.hand.cards
    gameController.drawCards(numberCardsToDraw)
    val drawnCards = gameController.players.head.hand.cards diff initialCards
    gameController.players.head.hand.cards should be(initialCards :++ drawnCards)
