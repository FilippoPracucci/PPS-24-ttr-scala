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
  import scala.swing.event.ButtonClicked
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
      val cardButton: ToggleButton = ToggleButton(name)
      val component: FlowPanel = FlowPanel(FlowPanel.Alignment.Center)(cardButton)
      cardButton.configCardButton()
      cardButton.configCardButtonReactions()
      component

    extension (component: Component)
      private def configCardButton(): Unit =
        component.background = color
        component.foreground = textColor
      private def configCardButtonReactions(): Unit =
        component.listenTo(component.mouse.clicks)
        component.reactions += {
          case _: ButtonClicked => _selected = !_selected
          case _ => ()
        }
