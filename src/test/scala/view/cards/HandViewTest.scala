package view.cards

import scala.swing.*
import scala.swing.event.ButtonClicked

/** A simple test consisting in launching a frame with a player's hand of train cards to show its representation. */
object HandViewTest extends App:
  import model.utils.Color
  import Color.*
  import model.cards.{Card, CardsGenerator, Deck, Hand}

  private val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLACK), Card(BLUE))
  private val fixedDeckGenerator: CardsGenerator[Deck] = () => fixedList
  private val deckFixed: Deck = Deck()(using fixedDeckGenerator)
  private val hand: Hand = Hand(deckFixed)

  import controller.CardControllerColor.*
  private val handView = HandView(hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor)))
  private val frame = new MainFrame()
  private val panel = new BoxPanel(Orientation.Horizontal)
  private val button = new Button("Draw")
  import javax.swing.WindowConstants.EXIT_ON_CLOSE

  frame.peer.setDefaultCloseOperation(EXIT_ON_CLOSE)
  frame.visible = true
  frame.pack()
  frame.centerOnScreen()
  panel.contents += handView.handComponent
  panel.contents += button
  frame.contents = panel

  button.listenTo(button.mouse.clicks)
  Reactions.Impl()
  button.reactions += {
    case _: ButtonClicked => updateHandView()
    case _ => ()
  }

  Thread.sleep(1000)
  button.doClick()

  private def updateHandView(): Unit =
    val cardsToAdd = List(Card(GREEN), Card(BLUE))
    hand.addCards(cardsToAdd)
    panel.contents.clear()
    handView.updateHand(hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor)))
    panel.contents += handView.handComponent
    panel.contents += button
    frame.repaint()
    frame.pack()
