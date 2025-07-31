package controller

import model.map.GameMap
import model.objective.ObjectiveChecker

/** Trait that represents a game controller responsible for claiming a route. */
trait ClaimRouteController:
  import view.GameView.City

  /** Claims the route connecting the specified cities.
    *
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    */
  def claimRoute(connectedCities: (City, City)): Unit

object ClaimRouteController:
  /** Creates a [[ClaimRouteController]].
    *
    * @param turnManager
    *   the turn manager
    * @param viewController
    *   the controller of the view
    * @param gameMap
    *   the map of the game
    * @return
    *   the created [[ClaimRouteController]]
    */
  def apply(turnManager: TurnManager, viewController: ViewController, gameMap: GameMap): ClaimRouteController =
    ClaimRouteControllerImpl(turnManager, viewController, gameMap)

  private object ImportHelper:
    export model.player.Player
    export model.objective.ObjectiveChecker
    export model.utils.{Color, GameError}
    export model.map.Route.SpecificColor
    export view.GameView.{City, Points}
    private val routePointsManager = model.map.RoutePointsManager()
    export routePointsManager.points

  private class ClaimRouteControllerImpl(turnManager: TurnManager, viewController: ViewController, gameMap: GameMap)
      extends ClaimRouteController:
    import ImportHelper.*

    private val objectiveChecker = ObjectiveChecker(gameMap)

    override def claimRoute(connectedCities: (City, City)): Unit = gameMap.getRoute(connectedCities) match
      case Some(route) => route.mechanic match
          case SpecificColor(color) =>
            ClaimRouteHelper.claimRoute(connectedCities, route.length, route.points)(route.length, color)
          case _ => throw new IllegalStateException("Unhandled mechanic")
      case _ => throw new IllegalStateException(s"The route between $connectedCities doesn't exist")

    private object ClaimRouteHelper:
      def claimRoute(connectedCities: (City, City), routeLength: Int, routePoints: Points)(nCards: Int, color: Color)
          : Unit =
        (for
          claimingPlayer <- gameMap.getPlayerClaimingRoute(connectedCities)
          _ <- check(claimingPlayer.isEmpty, GameMap.AlreadyClaimedRoute)
          _ <- check(currentPlayer.canPlaceTrains(routeLength), Player.NotEnoughTrains)
          _ <- check(currentPlayer.canPlayCards(color, nCards), Player.NotEnoughCards)
          _ <- gameMap.claimRoute(connectedCities, currentPlayer.id)
          _ <- currentPlayer.placeTrains(routeLength)
          _ <- currentPlayer.playCards(color, nCards)
        yield ()) match
          case Right(_) =>
            currentPlayer.addPoints(routePoints)
            viewController.updateRouteView(connectedCities)
            checkObjectiveCompletion()
            turnManager.switchTurn()
            viewController.updateViewNewTurn()
          case Left(gameError) => viewController.reportError(gameError)

      private def check(condition: Boolean, err: GameError): Either[GameError, Unit] = Either.cond(condition, (), err)

      private def checkObjectiveCompletion(): Unit =
        Option(currentPlayer.objective)
          .filter(!_.completed)
          .filter(objective => objectiveChecker.check(objective, currentPlayer.id))
          .foreach(objective =>
            objective.markAsComplete()
            currentPlayer.addPoints(objective.points)
            viewController.showObjectiveCompletion()
          )

      private def currentPlayer: Player = turnManager.currentPlayer
