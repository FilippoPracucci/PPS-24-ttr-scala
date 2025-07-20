package view.player

/** The representation of a player's objective. */
trait ObjectiveView:
  playerView: PlayerView =>

  import controller.GameController
  export GameController.{City, Points}

  /** Update the player's objective representation with a new one, given as a pair of [[City]] and [[Points]].
    *
    * @param objective
    *   the new objective to represent.
    */
  def updateObjective(objective: ((City, City), Points)): Unit =
    playerView.updateComponentText(
      f"Objective: connect the cities ${objective._1._1} and ${objective._1._2}\n\nPoints: ${objective._2}"
    )

/** A basic representation of a player with in addition the player's objective, so following the [[ObjectiveView]]
  * trait.
  */
class BasicObjectiveView extends BasicPlayerView with ObjectiveView
