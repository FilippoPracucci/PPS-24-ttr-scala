package view.player

import scala.swing.*

/** The representation of the final player rankings. */
trait FinalRankingView:
  /** The [[Component]] of the final player rankings view.
    *
    * @return
    *   the component of the final player rankings representation.
    */
  def component: Component

/** The factory for [[FinalRankingView]] instances. */
object FinalRankingView:
  /** Create a [[FinalRankingView]] with the title and the player scores given.
    *
    * @param title
    *   the title of the final player rankings view.
    * @param playerScores
    *   the list of player scores consisting of pairs "player's name; score"
    * @return
    */
  def apply(title: String)(playerScores: Seq[(PlayerName, Points)]): FinalRankingView =
    FinalRankingViewImpl(title)(playerScores)

  import view.GameView

  /** Type aliases that represent the player's name and points. */
  export GameView.{PlayerName, Points}

  private class FinalRankingViewImpl(title: String)(playerScores: Seq[(PlayerName, Points)]) extends FinalRankingView:
    import config.GameViewConfig.FontConfig.FinalRankingFont
    import java.awt.Color.*

    private val BorderWeight = 20

    private val _component: BoxPanel = new BoxPanel(Orientation.Vertical):
      contents += new BoxPanel(Orientation.Horizontal):
        contents += new Label(title):
          font = FinalRankingFont
        background = WHITE
        border = Swing.EmptyBorder(BorderWeight)
    private var scoreLabels: Map[String, Label] = Map()

    override def component: Component =
      finalRanking()
      _component

    private def finalRanking(): Unit =
      val ScoreLabelFontSize = 16f
      scoreLabels = playerScores.sortBy(-_._2).map((player, score) => (player, new Label(score.toString))).toMap
      scoreLabels.foreach((player, scoreLabel) =>
        _component.contents += new BoxPanel(Orientation.Horizontal):
          contents ++= List(new Label(player + ":"), Swing.HGlue, scoreLabel)
          background = WHITE
          border = Swing.EmptyBorder(BorderWeight)
          contents.foreach(c => c.font = c.font.deriveFont(ScoreLabelFontSize))
      )
