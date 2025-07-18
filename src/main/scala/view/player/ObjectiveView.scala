package view.player

import scala.swing.*

/** The representation of a player's objective. It's possible to update it. */
trait ObjectiveView:
  import ObjectiveView.{City, Points}

  /** The [[Component]] of the player's objective view.
    *
    * @return
    *   the component of the player's objective representation.
    */
  def component: Component

  /** Update the player's objective representation with a new one, given as a pair of [[City]] and [[Points]].
    *
    * @param objective
    *   the new objective to represent.
    */
  def updateObjective(objective: ((City, City), Points)): Unit

/** The factory for [[ObjectiveView]] instances. */
object ObjectiveView:
  import controller.GameController

  /** Type aliases that represent the city as String by its name and the points as Int.
    */
  export GameController.{City, Points}

  /** Create a player's objective representation.
    *
    * @return
    *   the player's objective representation.
    */
  def apply(): ObjectiveView = ObjectiveViewImpl

  private object ObjectiveViewImpl extends ObjectiveView:
    import Font.Style

    private val _component: TextPane = new TextPane():
      this.initComponent()

    override def component: Component = _component

    override def updateObjective(objective: ((City, City), Points)): Unit =
      _component.text = objectiveText(objective)

    private def objectiveText(objective: ((City, City), Points)): String =
      f"Objective: connect the cities ${objective._1._1} and ${objective._1._2}\n\nPoints: ${objective._2}"

    extension (component: TextPane)
      private def initComponent(): Unit =
        component.editable = false
        component.font = Font("Coursier", Style.Plain, 16)
