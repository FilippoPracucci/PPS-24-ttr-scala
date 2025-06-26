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
    *   the color of the card created.
    * @return
    *   the card created.
    */
  def apply(color: Color): Card = CardImpl(color)
  private case class CardImpl(override val color: Color) extends Card

/** A generator of an empty list of train cards. */
trait CardsGenerator:
  /** Generate an empty list of train cards.
    *
    * @return
    *   the empty list of train cards created.
    */
  def generate(): List[Card] = List.empty

/** Factory for [[CardsGenerator]] instances. */
object CardsGenerator:
  /** Create a train card generator.
    *
    * @return
    *   the train card generator.
    */
  def apply(): CardsGenerator = CardsGeneratorImpl()
  private case class CardsGeneratorImpl() extends CardsGenerator

/** The standard train card generator.
  *
  * @return
  *   the given instance of the standard train card generator.
  */
given CardsGenerator = CardsGenerator()

private class Cards(using generator: CardsGenerator)(private var _cards: List[Card] = generator.generate()):
  def cards: List[Card] = _cards
  def cards_=(cards: List[Card]): Unit = _cards = cards
