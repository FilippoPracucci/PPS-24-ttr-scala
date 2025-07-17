package view.player

import scala.swing.*

/** The representation of a player. It's possible to update it. */
trait PlayerView:
  import PlayerView.PlayerId

  /** The [[Component]] of the player view.
    *
    * @return
    *   the component of the player representation.
    */
  def component: Component

  /** Update the player representation with a new one, given as [[PlayerId]] and number of trains left.
    *
    * @param playerId
    *   the new player id to represent.
    * @param trains
    *   the new player number of train cars left.
    */
  def updatePlayer(playerId: PlayerId, trains: Int): Unit

/** The factory for [[PlayerView]] instances. */
object PlayerView:
  import controller.GameController

  /** Type aliases that represent the player identifier as a [[model.utils.PlayerColor]].
    */
  export GameController.PlayerId

  /** Create a player's objective representation.
    *
    * @return
    *   the player's objective representation.
    */
  def apply(): PlayerView = PlayerViewImpl

  private object PlayerViewImpl extends PlayerView:
    import Font.Style

    private val _component: TextPane = new TextPane():
      this.initComponent()

    override def component: Component = _component

    override def updatePlayer(playerId: PlayerId, trains: Int): Unit =
      _component.text = f"Player $playerId has $trains left"

    extension (component: TextPane)
      private def initComponent(): Unit =
        component.editable = false
        component.font = Font("Coursier", Style.Bold, 16)
