package view.player

import scala.swing.*
import view.ViewTest

/** A simple test consisting in launching a frame with a player's objective to show its representation and its update */
object ObjectiveViewTest extends App with ViewTest:
  import config.GameViewConfig.ObjectiveTitle

  private val objectiveView = BasicObjectiveView(ObjectiveTitle)
  private val panel = new BoxPanel(Orientation.Vertical)

  objectiveView.updateObjective(("Paris", "Berlin"), 8)

  panel.contents += objectiveView.component
  super.setFrameContents(panel)

  Thread.sleep(2000)

  val startTime = System.currentTimeMillis()
  objectiveView.updateObjective(("Roma", "Venezia"), 5)
  super.updateView(startTime)
