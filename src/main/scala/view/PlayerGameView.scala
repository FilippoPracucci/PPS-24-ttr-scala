package view

import cards.HandView

/** Trait that represents the view about the player. */
trait PlayerGameView:
  import GameView.{City, Points, PlayerName}

  /** Add the hand component to the view.
    *
    * @param handView
    *   the hand view to add to the panel.
    */
  def addHandView(handView: HandView): Unit

  /** Update the hand view component.
    *
    * @param handView
    *   the hand view component to update.
    */
  def updateHandView(handView: HandView): Unit

  /** Initialize player scores.
    *
    * @param playerScores
    *   the list of player scores consisting of pairs [[PlayerName]] and [[Points]].
    */
  def initPlayerScores(playerScores: Seq[(PlayerName, Points)]): Unit

  /** Update the objective view.
    *
    * @param objective
    *   the pair of cities to connect and its value in terms of points.
    */
  def updateObjective(objective: ((City, City), Points)): Unit

  /** Update the completion state of the current player objective.
    *
    * @param state
    *   the new completion state of the objective.
    */
  def updateCompletionCheckBox(state: Boolean): Unit

  /** Update the player information view.
    *
    * @param playerId
    *   the identifier of the player.
    * @param trains
    *   the number of train cars left to the player.
    */
  def updatePlayerInfo(playerId: PlayerName, trains: Int): Unit

  /** Updates the score of the specified player.
    *
    * @param player
    *   the name of the player whose score is to update.
    * @param score
    *   the new score of the player.
    */
  def updatePlayerScore(player: PlayerName, score: Points): Unit

/** Companion object for [[PlayerGameView]]. */
object PlayerGameView:
  import player.{BasicObjectiveView, BasicPlayerInfoView, PlayerScoresView}
  import scala.swing.{BoxPanel, MainFrame}

  /** Creates a [[PlayerGameView]].
    *
    * @param frame
    *   the main frame
    * @param handPanel
    *   the panel of the player hand
    * @param objectiveView
    *   the view of the objective
    * @param playerInfoView
    *   the view of the player info
    * @param playerScoresView
    *   the view of the player scores
    * @return
    *   the created [[PlayerGameView]]
    */
  def apply(frame: MainFrame, handPanel: BoxPanel, objectiveView: BasicObjectiveView,
      playerInfoView: BasicPlayerInfoView, playerScoresView: PlayerScoresView): PlayerGameView =
    PlayerGameViewImpl(frame, handPanel, objectiveView, playerInfoView, playerScoresView)

  private case class PlayerGameViewImpl(frame: MainFrame, handPanel: BoxPanel, objectiveView: BasicObjectiveView,
      playerInfoView: BasicPlayerInfoView, playerScoresView: PlayerScoresView) extends PlayerGameView:

    override def addHandView(handView: HandView): Unit =
      handPanel.contents += handView.handComponent

    override def updateHandView(handView: HandView): Unit =
      handPanel.contents.clear()
      addHandView(handView)
      frame.validate()

    export objectiveView.{updateObjective, updateCompletionCheckBox}
    export playerInfoView.updatePlayerInfo
    export playerScoresView.{initPlayerScores, updatePlayerScore}
