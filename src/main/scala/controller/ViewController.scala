package controller

import model.map.GameMap
import view.GameView.City
import model.utils.GameError

/** Trait that represents a controller responsible for interactions with the view. */
trait ViewController:
  /** Initializes the game view.
    *
    * @param gameMap
    *   the game map to display in the game view
    */
  def initGameView(gameMap: GameMap): Unit

  /** Updates the view of a route.
    *
    * @param connectedCities
    *   the pair of cities that identifies the route to be updated
    */
  def updateRouteView(connectedCities: (City, City)): Unit

  /** Shows the player that the objective has been completed. */
  def showObjectiveCompletion(): Unit

  /** Updates the view for the new turn. */
  def updateViewNewTurn(): Unit

  /** Reports an error to the player.
    *
    * @param gameError
    *   the error to report
    */
  def reportError(gameError: GameError): Unit

  /** Shows the game rules to the player. */
  def showRules(): Unit

object ViewController:
  import model.player.Player

  /** Creates a [[ViewController]].
    *
    * @param turnManager
    *   the turn manager
    * @param players
    *   the list of players
    * @return
    *   the created [[ViewController]]
    */
  def apply(turnManager: TurnManager, players: List[Player]): ViewController = ViewControllerImpl(turnManager, players)

  private object ImportHelper:
    export view.cards.{HandView, CardView}
    export CardControllerColor.*
    export model.map.Route
    export MapColorConverter.*
    export view.GameView
    export GameView.{PlayerName, Points, MessageType}
    export config.GameViewConfig.*
    export config.GameConfig.{ErrorDescription, RulesDescription}

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
            case Route.SpecificColor(color) => color.viewColor
            case _ => throw new IllegalStateException("Unhandled mechanic")
        )
      )
      gameView.updatePlayerInfo(currentPlayer.name, currentPlayer.trains)
      gameView.addHandView(handView)
      gameView.updateObjective(currentPlayerObjective)
      gameView.initPlayerScores(currentPlayerScores)
      gameView.open()

    override def updateRouteView(connectedCities: (City, City)): Unit =
      gameView.updateRoute(connectedCities, currentPlayer.id.viewColor)
      gameView.updatePlayerScore(currentPlayer.name, currentPlayer.score)

    override def showObjectiveCompletion(): Unit =
      gameView.show(ObjectiveCompletedDescription(currentPlayer.objective.points), ObjectiveCompletedTitle,
        MessageType.Info)
      gameView.updatePlayerScore(currentPlayer.name, currentPlayer.score)

    override def updateViewNewTurn(): Unit =
      import GameState.*
      turnManager.gameState match
        case START_LAST_ROUND => gameView.show(StartLastRoundDescription, StartLastRoundTitle, MessageType.Info)
        case END_GAME => gameView.endGame(currentPlayerScores)
        case _ => ()
      handView.updateHand(currentHandCardsView)
      gameView.updatePlayerInfo(currentPlayer.name, currentPlayer.trains)
      gameView.updateHandView(handView)
      gameView.updateCompletionCheckBox(currentPlayer.objective.completed)
      gameView.updateObjective(currentPlayerObjective)

    override def reportError(gameError: GameError): Unit =
      gameView.show(ErrorDescription(gameError), ReportErrorTitle, MessageType.Error)

    override def showRules(): Unit = gameView.show(RulesDescription, RulesTitle, MessageType.Response)

    private def currentPlayer: Player = turnManager.currentPlayer

    private def currentPlayerObjective: ((City, City), Points) = currentPlayer.objective.unapply().get

    private def currentPlayerScores: Seq[(PlayerName, Points)] = players.map(player => (player.name, player.score))

    extension (player: Player)
      private def name: String = player.id.toString.head.toUpper.toString + player.id.toString.tail.toLowerCase

    private def currentHandCardsView: List[CardView] =
      currentPlayer.hand.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor))
