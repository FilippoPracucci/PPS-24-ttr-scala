package controller

import model.player.Player

/** A players' turn manager, which has the list of players, the current player playing and can switch players turn. */
trait TurnManager:

  /** The singleton instance of the [[Player]] list.
    *
    * @return
    *   the globally shared [[Player]] list instance.
    */
  def players: List[Player]

  /** The current player on turn.
    *
    * @return
    *   the player on turn.
    */
  def currentPlayer: Player

  /** Switch players' turn following the order. */
  def switchTurn(): Unit

object TurnManager:
  def apply(players: List[Player]): TurnManager = TurnManagerImpl(players)

  private class TurnManagerImpl(override val players: List[Player]) extends TurnManager:
    private var _currentPlayer: Player = players.head

    override def currentPlayer: Player = _currentPlayer
    private def currentPlayer_=(player: Player): Unit = _currentPlayer = player

    override def switchTurn(): Unit =
      currentPlayer = players((players.indexOf(currentPlayer) + 1) % players.size)
