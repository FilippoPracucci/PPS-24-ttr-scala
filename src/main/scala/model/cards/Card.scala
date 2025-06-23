package model.cards

import model.utils.Color

trait Card:
  def color: Color

object Card:
  def apply(color: Color): Card = CardImpl(color)
  private case class CardImpl(override val color: Color) extends Card
