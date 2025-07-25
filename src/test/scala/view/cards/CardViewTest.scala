package view.cards

import model.cards.Card
import model.utils.Color.*
import view.ViewTest
import scala.swing.*

/** A simple test consisting in launching a frame with a card to show its representation. */
object CardViewTest extends App with ViewTest:
  import controller.CardControllerColor.*
  val card = Card(YELLOW)
  val cardView = CardView(card.cardName)(card.cardColor, card.cardTextColor)
  val panel = FlowPanel(FlowPanel.Alignment.Center)(cardView.cardComponent)

  super.setFrameContents(panel)
