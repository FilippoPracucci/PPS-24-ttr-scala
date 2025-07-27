package view.player

import config.GameViewConfig.ObjectiveDescription

/** The representation of a player's objective. */
trait ObjectiveView:
  playerView: PlayerView =>

  import view.GameView.{City, Points}

  /** Update the player's objective representation with a new one, given as a pair of [[City]] and [[Points]].
    *
    * @param objective
    *   the new objective to represent.
    */
  def updateObjective(objective: ((City, City), Points)): Unit =
    playerView.updateComponentText(ObjectiveDescription(objective._1._1, objective._1._2, objective._2))

/** The representation of a player's objective with in addition completion status. */
trait ObjectiveViewWithCompletion:
  playerView: PlayerView =>

  import scala.swing.*
  import config.GameViewConfig.FontConfig.ObjectiveFont
  import config.GameViewConfig.ColorConfig.BackgroundColor

  private val CompletedLabelText = "Completed:"
  private val checkBox: CheckBox = new CheckBox():
    background = BackgroundColor
    enabled = false
  private val panel: BoxPanel = new BoxPanel(Orientation.Horizontal):
    background = BackgroundColor
    val label: Label = new Label(CompletedLabelText):
      font = ObjectiveFont
    contents ++= List(label, Swing.HGlue, checkBox)

  /** Add a completion checkbox to the objective view. */
  def addCompletionCheckBox(): Unit = playerView.addComponentToInnerPanel(panel)

  /** Update the completion checkbox of the objective view following the given state.
    *
    * @param state
    *   the updated completion state.
    */
  def updateCompletionCheckBox(state: Boolean): Unit = checkBox.selected = state

/** A basic representation of a player with in addition the player's objective and its completion status, so following
  * the [[ObjectiveView]] and [[ObjectiveViewWithCompletion]] traits.
  *
  * @param title
  *   the title of the view.
  */
class BasicObjectiveView(title: String)
    extends BasicPlayerView(title)
    with ObjectiveView
    with ObjectiveViewWithCompletion:

  addCompletionCheckBox()
