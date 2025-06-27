package view

import scala.swing.{FlowPanel, *}
import model.cards.Card
import model.utils.Color.*

import scala.swing.event.MousePressed
import java.awt.Color as ViewColor

trait CardView:
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
      case WHITE | PINK | YELLOW => ViewColor.BLACK
      case _ => ViewColor.WHITE
    def cardComponent: Component

object CardView:
  def apply(): CardView = CardViewImpl()

  private case class CardViewImpl() extends CardView:
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
        component.configCardButton(card)
        component.listenTo(component.mouse.clicks)
        component.reactions.+= match
          case _: MousePressed => component.enabled = !component.enabled
          case _ => ()
