package view.player

import scala.swing.*
import view.ViewTest

/** A simple test consisting in launching a frame with a player to show its representation and its update. */
object PlayerInfoViewTest extends App with ViewTest:
  import config.GameViewConfig.PlayerInfoTitle
  import model.utils.PlayerColor.*

  private val playerView = BasicPlayerInfoView(PlayerInfoTitle)
  private val panel = new BoxPanel(Orientation.Vertical)

  playerView.updatePlayerInfo("Green", 40)

  panel.contents += playerView.component
  super.setFrameContents(panel)

  Thread.sleep(2000)

  val startTime = System.currentTimeMillis()
  playerView.updatePlayerInfo("Yellow", 30)
  super.updateView(startTime)
