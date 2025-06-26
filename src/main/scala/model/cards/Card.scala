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

private class Cards(private var _cards: List[Card]):
  def cards: List[Card] = _cards
  def cards_=(cards: List[Card]): Unit = _cards = cards
