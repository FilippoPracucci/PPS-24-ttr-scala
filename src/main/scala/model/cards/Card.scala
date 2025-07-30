package model.cards

import model.utils.Color

/** A train card characterized by a color. */
trait Card:
  /** The color of the card.
    *
    * @return
    *   the color of the card.
    */
  def color: Color

/** Factory for [[Card]] instances. */
object Card:
  /** Create a card of the given color.
    *
    * @param color
    *   the color of the card.
    * @return
    *   the card created.
    */
  def apply(color: Color): Card = CardImpl(color)
  private case class CardImpl(override val color: Color) extends Card

/** A generic generator with [[Cards]] as upperbound. It's also able to generate a list of train cards.
  *
  * @tparam T
  *   the type generated, with [[Cards]] as upperbound.
  */
trait Generator[T <: Cards]:
  /** Generate a list of train cards.
    *
    * @return
    *   the list of train cards created.
    */
  def generateCards(): List[Card]

  /** Generate an instance of [[T]].
    *
    * @return
    *   the generated [[T]] instance.
    */
  def generate(): T

/** A [[Generator]] for type [[Cards]]. */
abstract class CardsGenerator extends Generator[Cards]:
  override def generate(): Cards = Cards(generateCards())

/** The default [[CardsGenerator]]. It generates an empty list of train cards. */
given CardsGenerator with
  override def generateCards(): List[Card] = List.empty

/** A list of train cards created by means of a generator.
  *
  * @param _cards
  *   the list of cards for the initialization.
  */
class Cards(private var _cards: List[Card] = summon[CardsGenerator].generateCards()):
  def cards: List[Card] = _cards
  protected def cards_=(cards: List[Card]): Unit = _cards = cards
