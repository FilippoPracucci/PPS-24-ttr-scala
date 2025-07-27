package model.player

import model.utils.GameError

private object PlayerGameErrors:
  /** Error that represents the case in which the deck doesn't have enough cards. */
  case object NotEnoughCardsInTheDeck extends GameError

  /** Error that represents the case in which a player doesn't have enough cards.
    */
  case object NotEnoughCards extends GameError

  /** Error that represents the case in which a player doesn't have enough trains.
    */
  case object NotEnoughTrains extends GameError
