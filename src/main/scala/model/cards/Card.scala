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

/** A generic list of train cards generator.
  *
  * @tparam T
  *   the type the generator will be used for.
  */
trait CardsGenerator[T]:
  /** Generate a list of train cards. */
  def generate(): List[Card]

/** The default [[CardsGenerator]] for type [[Cards]]. It generates an empty list of train cards. */
given CardsGenerator[Cards] = () => List.empty

/** A list of train cards created by means of a generator.
  *
  * @param generator
  *   the [[CardsGenerator]] for type [[Cards]].
  * @param _cards
  *   the list of cards for the initialization.
  */
class Cards(using generator: CardsGenerator[Cards])(private var _cards: List[Card] = generator.generate()):
  def cards: List[Card] = _cards
  protected def cards_=(cards: List[Card]): Unit = _cards = cards
