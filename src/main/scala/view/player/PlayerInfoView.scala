package view.player

/** The representation of the information of a player. */
trait PlayerInfoView:
  playerView: PlayerView =>

  import config.GameViewConfig.PlayerInfoDescription
  import view.GameView.PlayerName

  /** Update the representation of the player information with new ones, given as [[PlayerName]] and number of train
    * cars left.
    *
    * @param playerName
    *   the player new name to represent.
    * @param trains
    *   the player new number of train cars left.
    */
  def updatePlayerInfo(playerName: PlayerName, trains: Int): Unit =
    playerView.updateComponentText(PlayerInfoDescription(playerName, trains))

/** A basic representation of a player with in addition the player information, so following the [[PlayerInfoView]]
  * trait.
  *
  * @param title
  *   the title of the view.
  */
class BasicPlayerInfoView(title: String) extends BasicPlayerView(title) with PlayerInfoView
