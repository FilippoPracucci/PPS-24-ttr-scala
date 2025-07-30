package view.player

import scala.swing.*
import view.ViewTest

/** A simple test consisting in launching a frame with some player scores to show its representation and its update. */
object PlayerScoresViewTest extends App with ViewTest:
  import config.GameViewConfig.PlayerScoresTitle

  private val playerScoresView = PlayerScoresView(PlayerScoresTitle)
  private val panel = new BoxPanel(Orientation.Vertical)

  playerScoresView.initPlayerScores(Seq(("Green", 30), ("Blue", 22), ("Red", 16)))

  panel.contents += playerScoresView.component
  super.setFrameContents(panel)

  Thread.sleep(2000)

  val startTime = System.currentTimeMillis()
  playerScoresView.updatePlayerScore("Red", 20)
  super.updateView(startTime)
