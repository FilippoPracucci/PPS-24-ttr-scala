package view.player

/** The representation of the information of a player. */
trait PlayerInfoView:
  playerView: PlayerView =>

  import config.GameViewConfig.PlayerInfoDescription
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
    playerView.updateComponentText(PlayerInfoDescription(playerId.toString, trains))

/** A basic representation of a player with in addition the player information, so following the [[PlayerInfoView]]
  * trait.
  *
  * @param title
  *   the title of the view.
  */
class BasicPlayerInfoView(title: String) extends BasicPlayerView(title) with PlayerInfoView
