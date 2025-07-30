package view.cards

import scala.swing.*

/** The representation of a train card. */
trait CardView:
  /** The [[Component]] of the card view.
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
  def apply(name: String)(color: ViewColor, textColor: ViewColor): CardView = CardViewImpl(name)(color, textColor)

  private case class CardViewImpl(name: String)(color: ViewColor, textColor: ViewColor) extends CardView:
    override def cardComponent: Component =
      import config.GameViewConfig.BorderConfig.*
      val BorderWeight = 5
      val cardLabel: Label = new Label(name):
        foreground = textColor
      val component: FlowPanel = new FlowPanel(FlowPanel.Alignment.Center)(cardLabel):
        background = color
        border = EmptyBorder(BorderWeight)
      component
