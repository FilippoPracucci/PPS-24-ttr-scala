package controller

import model.map.GameMap
import GameController.City
import model.utils.GameError

private trait ViewController:
  def initGameView(gameMap: GameMap): Unit
  def updateRouteView(connectedCities: (City, City)): Unit
  def updateObjectiveView(): Unit
  def updateViewNewTurn(): Unit
  def reportError(gameError: GameError): Unit
  def showRules(): Unit

private object ViewController:
  import model.player.Player
  import view.GameView

  def apply(turnManager: TurnManager, players: List[Player]): ViewController = ViewControllerImpl(turnManager, players)

  private object MapViewColorHelper:
    import model.utils.{Color, PlayerColor}
    extension (color: Color) def toMapViewColor: String = color.toString.toLowerCase()
    extension (playerColor: PlayerColor) def toMapViewColor: String = playerColor.toString.toLowerCase()

  private object ImportHelper:
    export view.cards.{HandView, CardView}
    export CardControllerColor.*
    export model.map.Route
    export GameController.Points
    export MapViewColorHelper.*
    export GameView.PlayerName

  private class ViewControllerImpl(turnManager: TurnManager, players: List[Player]) extends ViewController:
    import ImportHelper.*

    private val handView = HandView(currentHandCardsView)
    private val gameView = GameView()

    override def initGameView(gameMap: GameMap): Unit =
      gameMap.routes.foreach(route =>
        gameView.addRoute(
          (route.connectedCities._1.name, route.connectedCities._2.name),
          route.length,
          route.mechanic match
            case Route.SpecificColor(color) => color.toMapViewColor
            case _ => throw new IllegalStateException("Unhandled mechanic")
        )
      )
      gameView.updatePlayerInfo(currentPlayer.id, currentPlayer.trains)
      gameView.addHandView(handView)
      gameView.updateObjective(currentPlayerObjective)
      gameView.initPlayerScores(currentPlayerScores)
      gameView.open()

    override def updateRouteView(connectedCities: (City, City)): Unit =
      gameView.updateRoute(connectedCities, currentPlayer.id.toMapViewColor)
      gameView.updatePlayerScore(currentPlayer.name, currentPlayer.score)

    override def updateObjectiveView(): Unit =
      gameView.report("Objective completed",
        s"You have completed your objective! You gain ${currentPlayer.objective.points} points!")
      gameView.updatePlayerScore(currentPlayer.name, currentPlayer.score)

    override def updateViewNewTurn(): Unit =
      import GameState.*
      turnManager.gameState match
        case START_LAST_ROUND => gameView.startLastRound()
        case END_GAME => gameView.endGame(currentPlayerScores)
        case _ => ()
      handView.updateHand(currentHandCardsView)
      gameView.updatePlayerInfo(currentPlayer.id, currentPlayer.trains)
      gameView.updateHandView(handView)
      gameView.updateCompletionCheckBox(currentPlayer.objective.completed)
      gameView.updateObjective(currentPlayerObjective)

    override def reportError(gameError: GameError): Unit = gameError match
      case Player.NotEnoughCardsInTheDeck =>
        gameView.report("Error", "Not enough cards in the deck! It's possible only to claim a route!")
      case GameMap.AlreadyClaimedRoute => gameView.report("Error", "Can't claim a route that has already been claimed!")
      case Player.NotEnoughTrains => gameView.report("Error", "Not enough trains to claim this route!")
      case Player.NotEnoughCards => gameView.report("Error", "Not enough cards to claim this route!")
      case _ => throw new IllegalStateException("Unexpected error")

    override def showRules(): Unit = gameView.showRules("RULES!")

    private def currentPlayer: Player = turnManager.currentPlayer

    private def currentPlayerObjective: ((City, City), Points) = currentPlayer.objective.unapply().get

    private def currentPlayerScores: Seq[(PlayerName, Points)] = players.map(player => (player.name, player.score))

    extension (player: Player)
      private def name: String = player.id.toString.head.toUpper.toString + player.id.toString.tail.toLowerCase

    private def currentHandCardsView: List[CardView] =
      currentPlayer.hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor))
