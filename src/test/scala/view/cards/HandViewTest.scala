package view.cards

import scala.swing.*

/** A simple test consisting in launching a frame with a player's hand of train cards to show its representation. */
object HandViewTest extends App:
  import model.utils.Color
  import Color.*
  import model.cards.{Card, CardsGenerator, Deck, Hand}
  import controller.GameController.*

  val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLACK), Card(BLUE))
  val fixedDeckGenerator: CardsGenerator[Deck] = () => fixedList
  val deckFixed: Deck = Deck()(using fixedDeckGenerator)
  val hand: Hand = Hand(deckFixed)

  val handView = HandView(hand.cards.map(c => CardView(c.colorName)(c.cardColor, c.cardTextColor)))
  val frame = Frame()
  import javax.swing.WindowConstants.EXIT_ON_CLOSE

  frame.peer.setDefaultCloseOperation(EXIT_ON_CLOSE)
  frame.visible = true
  frame.pack()
  frame.centerOnScreen()
  frame.contents = handView.handComponent

  Thread.sleep(1000)
  updateHandView()

  private def updateHandView(): Unit =
    val cardsToAdd = List(Card(GREEN), Card(BLUE))
    hand.addCards(cardsToAdd)
    handView.addCardsComponent(cardsToAdd.map(c => CardView(c.colorName)(c.cardColor, c.cardTextColor)))
    frame.contents = handView.handComponent
    frame.repaint()
    frame.pack()
