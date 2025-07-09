package controller

import model.cards.Card
import java.awt.Color as ViewColor

private trait CardController:
  extension (card: Card)
    def cardColor: ViewColor

    def cardTextColor: ViewColor

    def colorName: String

private object CardController:
  def apply(): CardController = CardControllerImpl()

  private case class CardControllerImpl() extends CardController:
    import model.utils.Color.*

    extension (card: Card)
      override def cardColor: ViewColor = card.color match
        case BLACK => ViewColor.BLACK
        case WHITE => ViewColor.WHITE
        case RED => ViewColor.RED.darker()
        case BLUE => ViewColor.BLUE.darker()
        case ORANGE => ViewColor.ORANGE.darker()
        case YELLOW => ViewColor.YELLOW.darker()
        case GREEN => ViewColor.GREEN.darker()
        case PINK => ViewColor.PINK.darker()

      override def cardTextColor: ViewColor = card.color match
        case BLACK | RED | BLUE => ViewColor.WHITE
        case _ => ViewColor.BLACK

      override def colorName: String = card.color.toString
