package model.player

import controller.GameController.City
import controller.GameController.Points

/** An objective that has a pair of cities to connect and a number of points it assigns if completed or taken away
  * otherwise.
  */
trait Objective:
  /** The pair of cities to connect in order to complete the objective.
    *
    * @return
    *   the pair of cities to connect.
    */
  def citiesToConnect: (City, City)

  /** The points assigned if the objective is completed or taken away otherwise.
    *
    * @return
    *   the points corresponding to the objective.
    */
  def points: Points

/** The factory for [[Objective]] instances. */
object Objective:
  /** Create an [[Objective]] by its cities to connect and value in terms of points.
    *
    * @param cities
    *   the cities to connect.
    * @param points
    *   the value in terms of points.
    * @return
    *   the objective created.
    */
  def apply(cities: (City, City), points: Points): Objective = BasicObjective(cities, points)

  extension (objective: Objective)
    /** Allow instances of type [[Objective]] to be matched and deconstructed using pattern matching.
      *
      * @return
      *   an [[Option]] containing the cities to connect and the points assigned to the objective if the instance match,
      *   [[None]] otherwise.
      */
    def unapply(): Option[((City, City), Points)] =
      Some(objective.citiesToConnect, objective.points)

/** A basic objective following the [[Objective]] trait.
  *
  * @param citiesToConnect
  *   the cities to connect.
  * @param points
  *   the points corresponding to the objective.
  */
case class BasicObjective(override val citiesToConnect: (City, City), override val points: Points) extends Objective:
  // TODO: check the existence of the cities
  require(citiesToConnect._1 != citiesToConnect._2, "The cities to connect cannot be the same!")
  require(points > 0, "The points of an objective must be positive!")

/** An objective that can also be marked as completed and has the state of his completion. */
trait ObjectiveCompletion extends Objective:
  private var _completed = false

  /** Objective completion state.
    *
    * @return
    *   [[true]] if the objective is completed, [[false]] otherwise.
    */
  def completed: Boolean = _completed

  /** Mark the objective as complete. */
  def markAsComplete(): Unit = _completed = true

/** A basic objective with in addition the completion feature, so following the [[ObjectiveCompletion]] trait.
  *
  * @param cities
  *   the cities to connect.
  * @param points
  *   the points corresponding to the objective.
  */
class ObjectiveWithCompletion(cities: (City, City), points: Points)
    extends BasicObjective(cities, points)
    with ObjectiveCompletion
