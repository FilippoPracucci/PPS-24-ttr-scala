package view.player

import scala.swing.*

/** A simple test consisting in launching a frame with a player's objective to show its representation and its update */
object ObjectiveViewTest extends App:
  private val objectiveView = BasicObjectiveView("OBJECTIVE")
  private val frame = new MainFrame() {
    peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    visible = true
    centerOnScreen()
  }
  private val panel = new BoxPanel(Orientation.Vertical)

  objectiveView.updateObjective(("Paris", "Berlin"), 8)

  panel.contents += objectiveView.component
  frame.contents = panel
  frame.pack()

  Thread.sleep(2000)

  objectiveView.updateObjective(("Roma", "Venezia"), 5)
  frame.validate()
  frame.pack()
