package view.cards

import scala.swing.event.MousePressed
import scala.swing.*
import java.awt.Color as ViewColor

trait CardView:
  def selected: Boolean

  def color: ViewColor

  def textColor: ViewColor

  def cardComponent: Component

object CardView:
  def apply(name: String)(color: ViewColor, textColor: ViewColor): CardView = CardViewImpl(name)(color, textColor)

  private case class CardViewImpl(name: String)(override val color: ViewColor,
      override val textColor: ViewColor,
      private var _selected: Boolean = false) extends CardView:
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
          case _: MousePressed => _selected = !_selected
          case _ => ()
        }
