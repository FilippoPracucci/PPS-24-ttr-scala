package view.cards

import model.cards.Card
import model.utils.Color.*

import java.awt.Color as ViewColor
import scala.swing.event.MousePressed
import scala.swing.*

private trait CardView:
  def selected: Boolean

  extension (card: Card)
    def cardColor: ViewColor = card.color match
      case BLACK => ViewColor.BLACK
      case WHITE => ViewColor.WHITE
      case RED => ViewColor.RED.darker()
      case BLUE => ViewColor.BLUE.darker()
      case ORANGE => ViewColor.ORANGE.darker()
      case YELLOW => ViewColor.YELLOW.darker()
      case GREEN => ViewColor.GREEN.darker()
      case PINK => ViewColor.PINK.darker()
    def cardTextColor: ViewColor = card.color match
      case BLACK | RED | BLUE => ViewColor.WHITE
      case _ => ViewColor.BLACK
    def cardComponent: Component

private object CardView:
  def apply(): CardView = CardViewImpl()

  private case class CardViewImpl(private var _selected: Boolean = false) extends CardView:
    override def selected: Boolean = _selected

    extension (card: Card)
      override def cardComponent: Component =
        val cardButton: ToggleButton = ToggleButton(card.color.toString)
        val component: FlowPanel = FlowPanel(FlowPanel.Alignment.Center)(cardButton)
        cardButton.configCardButton(card)
        cardButton.configCardButtonReactions(card)
        component

    extension (component: Component)
      private def configCardButton(card: Card): Unit =
        component.background = card.cardColor
        component.foreground = card.cardTextColor
      private def configCardButtonReactions(card: Card): Unit =
        component.listenTo(component.mouse.clicks)
        component.reactions += {
          case _: MousePressed => _selected = !_selected
          case _ => ()
        }
