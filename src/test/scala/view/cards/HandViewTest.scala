package view.cards

import scala.swing.*

/** A simple test consisting in launching a frame with a player's hand of train cards to show its representation. */
object HandViewTest extends App:
  import model.utils.Color
  import Color.*
  import model.cards.{Card, CardsGenerator, Deck, Hand}

  val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLACK), Card(BLUE))
  val fixedDeckGenerator: CardsGenerator[Deck] = () => fixedList
  val deckFixed: Deck = Deck()(using fixedDeckGenerator)
  val hand: Hand = Hand(deckFixed)

  val handView = HandView(hand)
  val frame = Frame()
  val panel = FlowPanel(FlowPanel.Alignment.Center)()
  import javax.swing.WindowConstants.EXIT_ON_CLOSE

  frame.peer.setDefaultCloseOperation(EXIT_ON_CLOSE)
  panel.contents.++=:(handView.handComponent)
  frame.visible = true
  frame.pack()
  frame.centerOnScreen()
  frame.contents = panel

  Thread.sleep(1000)
  updateHandView()

  private def updateHandView(): Unit =
    hand.addCards(List(Card(GREEN), Card(BLUE)))
    handView.updateHandComponent(hand)
    panel.contents.clear()
    panel.contents.++=:(handView.handComponent)
    frame.repaint()
    frame.pack()
