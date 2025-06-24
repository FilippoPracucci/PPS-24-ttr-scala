package model.cards

import model.utils.Color

trait Card:
  def color: Color

object Card:
  def apply(color: Color): Card = CardImpl(color)
  private case class CardImpl(override val color: Color) extends Card

private abstract class Cards(private var _cards: List[Card]):
  def cards: List[Card] = _cards
  def cards_=(cards: List[Card]): Unit = _cards = cards
