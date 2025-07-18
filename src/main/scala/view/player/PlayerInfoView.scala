package view.player

/** The representation of the information of a player. */
trait PlayerInfoView:
  playerView: PlayerView =>

  import controller.GameController
  export GameController.PlayerId

  /** Update the representation of the player information with new ones, given as [[PlayerId]] and number of train cars
    * left.
    *
    * @param playerId
    *   the new player id to represent.
    * @param trains
    *   the new player number of train cars left.
    */
  def updatePlayerInfo(playerId: PlayerId, trains: Int): Unit =
    playerView.updateComponentText(f"Player $playerId has $trains train cars left")

import scala.swing.Font
import scala.swing.Font.Style

/** A basic representation of a player with in addition the player information, so following the [[PlayerInfoView]]
  * trait.
  */
class BasicPlayerInfoView extends BasicPlayerView(using Font("Coursier", Style.Bold, 16)) with PlayerInfoView
