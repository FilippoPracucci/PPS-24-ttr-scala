package view

import scala.swing.{FlowPanel, *}
import model.cards.Card
import model.utils.Color.*

/** A simple test consisting in launching a frame with a card to show its representation. */
object CardViewTest extends App:
  val cardView = CardView()
  val frame = Frame()
  val panel = FlowPanel(FlowPanel.Alignment.Center)(cardView.cardComponent(Card(YELLOW)))
  frame.visible = true
  frame.pack()
  frame.centerOnScreen()
  frame.contents = panel

  import javax.swing.WindowConstants.EXIT_ON_CLOSE
  frame.peer.setDefaultCloseOperation(EXIT_ON_CLOSE)
