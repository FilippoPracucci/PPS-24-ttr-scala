package view.cards

import scala.swing.*

/** The representation of a train card. */
trait CardView:
  /** The selection state of the card view component.
    *
    * @return
    *   [[true]] if the card view component is selected, [[false]] otherwise
    */
  def selected: Boolean

  /** The card view component.
    *
    * @return
    *   the card view component.
    */
  def cardComponent: Component

/** The factory for [[CardView]] instances. */
object CardView:
  import java.awt.Color as ViewColor

  /** Create a card representation.
    *
    * @param name
    *   the name of the card component.
    * @param color
    *   the color of the card component.
    * @param textColor
    *   the text color of the card component.
    * @return
    *   the card representation created.
    */
  def apply(name: String)(color: ViewColor, textColor: ViewColor): CardView = CardViewImpl(name)(color, textColor)()

  private case class CardViewImpl(name: String)(color: ViewColor, textColor: ViewColor)(private var _selected: Boolean =
        false) extends CardView:
    override def selected: Boolean = _selected

    override def cardComponent: Component =
      val BorderWeight = 5
      val cardLabel: Label = new Label(name):
        foreground = textColor
      val component: FlowPanel = new FlowPanel(FlowPanel.Alignment.Center)(cardLabel):
        background = color
        border = Swing.EmptyBorder(BorderWeight)
      component
