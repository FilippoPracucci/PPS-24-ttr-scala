package controller

import model.player.Player

/** The different game states. */
enum GameState:
  case IN_GAME, START_LAST_ROUND, LAST_ROUND, END_GAME

/** A players' turn manager, which has the list of players, the current player playing and can switch players turn. */
trait TurnManager:
  /** The current player on turn.
    *
    * @return
    *   the player on turn.
    */
  def currentPlayer: Player

  /** Switch players' turn following the order. */
  def switchTurn(): Unit

  /** The actual game state.
    *
    * @return
    *   the actual game state.
    */
  def gameState: GameState

/** The factory for [[TurnManager]] instances. */
object TurnManager:
  /** Create a [[TurnManager]], which manages the given list of players.
    *
    * @param players
    *   the list of players.
    * @return
    *   the turn manager created.
    */
  def apply(players: List[Player]): TurnManager = TurnManagerImpl(players)

  private class TurnManagerImpl(players: List[Player]) extends TurnManager:
    import controller.GameState.*

    private var _currentPlayer: Player = players.head
    private var _gameState: GameState = IN_GAME
    private var _playerStartedLastRound: Option[Player] = Option.empty

    override def currentPlayer: Player = _currentPlayer
    private def currentPlayer_=(player: Player): Unit = _currentPlayer = player

    override def gameState: GameState = _gameState

    override def switchTurn(): Unit =
      updateGameState()
      currentPlayer = players((players.indexOf(currentPlayer) + 1) % players.size)

    private def updateGameState(): Unit = _playerStartedLastRound match
      case Some(value) => _gameState = if currentPlayer == value then END_GAME else LAST_ROUND
      case None if currentPlayer.trains.trainCars <= 2 =>
        _gameState = START_LAST_ROUND
        _playerStartedLastRound = Option(currentPlayer)
      case _ => _gameState = IN_GAME
