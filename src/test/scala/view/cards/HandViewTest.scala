package view.cards

import scala.swing.*
import scala.swing.event.ButtonClicked
import view.ViewTest

/** A simple test consisting in launching a frame with a player's hand of train cards to show its representation. */
object HandViewTest extends App with ViewTest:
  import model.utils.Color
  import Color.*
  import model.cards.{Card, CardsGenerator, Deck, Hand}
  import controller.CardControllerColor.*
  import config.GameViewConfig.DrawButtonText

  private val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLACK), Card(BLUE))
  private val hand: Hand = Hand(Deck()(using () => fixedList))

  private val handView = HandView(hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor)))
  private val panel = new BoxPanel(Orientation.Horizontal)
  private val button = new Button(DrawButtonText)
  panel.contents ++= List(handView.handComponent, button)

  super.setFrameContents(panel)

  button.listenTo(button.mouse.clicks)
  button.reactions += {
    case _: ButtonClicked =>
      updateHandView()
      super.updateView()
    case _ => ()
  }

  Thread.sleep(1000)
  button.doClick()

  private def updateHandView(): Unit =
    val cardsToAdd = List(Card(GREEN), Card(BLUE))
    hand.addCards(cardsToAdd)
    panel.contents.clear()
    handView.updateHand(hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor)))
    panel.contents ++= List(handView.handComponent, button)
