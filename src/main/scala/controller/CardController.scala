package controller

import model.cards.Card
import java.awt.Color as ViewColor

trait CardController:
  extension (card: Card)
    def cardName: String

trait CardControllerUtils:
  cardController: CardController =>
  import model.utils.Color.*
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

private class BasicCardController extends CardController:
  extension (card: Card) override def cardName: String = card.color.toString

object CardControllerColor extends BasicCardController with CardControllerUtils
