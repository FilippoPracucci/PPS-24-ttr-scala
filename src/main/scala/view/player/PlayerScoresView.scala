package view.player

import scala.swing.*

/** The representation of the player scores. */
trait PlayerScoresView:
  import PlayerScoresView.{PlayerName, Points}

  /** The [[Component]] of the player scores view.
    *
    * @return
    *   the component of the player scores representation.
    */
  def component: Component

  /** Initialize player scores.
    *
    * @param playerScores
    *   the list of player scores consisting of pairs "player's name; score"
    */
  def initPlayerScores(playerScores: Seq[(PlayerName, Points)]): Unit

  /** Update the score of the specified player.
    *
    * @param player
    *   the name of the player whose score is to update.
    * @param score
    *   the new score of the player.
    */
  def updatePlayerScore(player: PlayerName, score: Points): Unit

/** The factory for [[PlayerScoresView]] instances. */
object PlayerScoresView:
  /** Create a [[PlayerScoresView]] with the title given.
    *
    * @param title
    *   the title of the player scores view.
    * @return
    *   the player scores view created.
    */
  def apply(title: String): PlayerScoresView = PlayerScoresViewImpl(title)

  import view.GameView
  export GameView.{PlayerName, Points}

  private case class PlayerScoresViewImpl(title: String) extends PlayerScoresView:
    import java.awt.Color.*

    private val _component: BoxPanel = new BoxPanel(Orientation.Vertical):
      contents += new BoxPanel(Orientation.Horizontal):
        contents += new Label(title)
        background = WHITE
      this.initComponent()
    private var scoreLabels: Map[String, Label] = Map()

    override def component: Component = _component

    override def initPlayerScores(playerScores: Seq[(PlayerName, Points)]): Unit =
      scoreLabels = playerScores.map((player, score) => (player, new Label(score.toString))).toMap
      scoreLabels.foreach((player, scoreLabel) =>
        _component.contents += new BoxPanel(Orientation.Horizontal) {
          contents += new Label(player + ":")
          contents += Swing.HGlue
          contents += scoreLabel
          background = WHITE
        }
      )
      _component.contents.foreach(_.updateLabelFont(15f))

    override def updatePlayerScore(player: PlayerName, score: Points): Unit =
      scoreLabels(player).text = score.toString

    extension (component: Component)
      private def initComponent(): Unit =
        import javax.swing.BorderFactory
        component.focusable = false
        component.background = WHITE
        component.border = BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(BLACK, 1, true),
          Swing.EmptyBorder(10)
        )

      private def updateLabelFont(size: Float): Unit = component match
        case panel: Panel => panel.contents.foreach {
            case label: Label => label.font = label.font.deriveFont(15f)
            case _ => ()
          }
        case _ => ()
