package view.player

import scala.swing.*

/** A simple test consisting in launching a frame with some player scores to show its representation and its update. */
object PlayerScoresViewTest extends App:
  import config.GameViewConfig.PlayerScoresTitle

  private val playerScoresView = PlayerScoresView(PlayerScoresTitle)
  private val frame = new MainFrame() {
    peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    visible = true
    centerOnScreen()
  }
  private val panel = new BoxPanel(Orientation.Vertical)

  playerScoresView.initPlayerScores(Seq(("Green", 30), ("Blue", 22), ("Red", 16)))

  panel.contents += playerScoresView.component
  frame.contents = panel
  frame.pack()

  Thread.sleep(2000)

  playerScoresView.updatePlayerScore("Red", 20)
  frame.validate()
  frame.pack()
