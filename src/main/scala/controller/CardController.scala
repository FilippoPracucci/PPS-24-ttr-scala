package controller

import model.cards.Card
import java.awt.Color as ViewColor

/** The controller about the card. */
trait CardController:
  extension (card: Card)
    /** The name of the card.
      *
      * @return
      *   the name of the card.
      */
    def cardName: String

/** Trait that decorates a [[CardController]] with some utilities for the card. */
trait CardControllerUtils:
  cardController: CardController =>
  import model.utils.Color.*
  extension (card: Card)
    /** Convert the color of the card into its color for the representation.
      *
      * @return
      *   the [[ViewColor]] correspondent to the card color.
      */
    def cardColor: ViewColor = card.color match
      case BLACK => ViewColor.BLACK
      case WHITE => ViewColor.WHITE
      case RED => ViewColor.RED.darker()
      case BLUE => ViewColor.BLUE.darker()
      case ORANGE => ViewColor.ORANGE.darker()
      case YELLOW => ViewColor.YELLOW.darker()
      case GREEN => ViewColor.GREEN.darker()
      case PINK => ViewColor.PINK.darker()

    /** The card's text color due to card color.
      *
      * @return
      *   the card's text color.
      */
    def cardTextColor: ViewColor = card.color match
      case BLACK | RED | BLUE => ViewColor.WHITE
      case _ => ViewColor.BLACK

private class BasicCardController extends CardController:
  extension (card: Card) override def cardName: String = card.color.toString

/** A basic [[CardController]] with in addition the utilities about card color, so following [[CardControllerUtils]]. */
object CardControllerColor extends BasicCardController with CardControllerUtils
