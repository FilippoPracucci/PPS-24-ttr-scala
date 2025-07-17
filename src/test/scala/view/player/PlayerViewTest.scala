package view.player

import scala.swing.*

/** A simple test consisting in launching a frame with a player to show its representation and its update. */
object PlayerViewTest extends App:
  private val playerView: PlayerView = PlayerView()
  private val frame = new MainFrame() {
    peer.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    visible = true
    centerOnScreen()
  }
  private val panel = new BoxPanel(Orientation.Vertical)

  import model.utils.PlayerColor.*
  playerView.updatePlayer(GREEN, 40)

  panel.contents += playerView.component
  frame.contents = panel
  frame.pack()

  Thread.sleep(2000)

  playerView.updatePlayer(YELLOW, 30)
  frame.validate()
  frame.pack()
