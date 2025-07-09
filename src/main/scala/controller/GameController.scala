package controller

import model.utils._

/** Trait that represents the controller of the game.
  */
trait GameController:
  // TODO
  def claimRoute(connectedCities: (String, String)): Unit

object GameController:
  /** Returns the singleton instance of `GameController`.
    * @return
    *   the globally shared `GameController` instance
    */
  def apply(): GameController = GameControllerImpl

  private object GameControllerImpl extends GameController:
    import scala.util._

    import model.map.GameMap
    import GameMap.given
    import model.map.Route
    import Route._
    import Color._
    import view.GameView
    import model.player.Player

    private val gameMap = GameMap()
    private val gameView = GameView()
    private val player = Player(PlayerColor.BLUE)

    initGameView()

    private def initGameView(): Unit =
      gameMap.routes.foreach(route =>
        gameView.addRoute(
          (route.connectedCities._1.name, route.connectedCities._2.name),
          route.length,
          route.mechanic match
            case Route.SpecificColor(color) => getMapViewColorFrom(color)
            case _ => throw new IllegalStateException("Unhandled mechanic")
        )
      )
      gameView.open()

    private def getMapViewColorFrom(color: Color): String = color match
      case BLACK => "black"
      case WHITE => "white"
      case RED => "red"
      case BLUE => "blue"
      case ORANGE => "orange"
      case YELLOW => "yellow"
      case GREEN => "green"
      case PINK => "pink"

    override def claimRoute(connectedCities: (String, String)): Unit =
      val optionRoute = gameMap.getRoute(connectedCities)
      val route = optionRoute.getOrElse(
        throw new IllegalStateException(s"The route between $connectedCities doesn't exist")
      )
      route.mechanic match
        case SpecificColor(color) => claimRoute(connectedCities, route.length, route.length, color)
        case _ => throw new IllegalStateException("Unhandled mechanic")

    private def claimRoute(connectedCities: (String, String), routeLength: Int, nCards: Int, color: Color): Unit =
      (for
        // TODO check cards
        _ <- check(player.trains.trainCars >= routeLength, Player.NotEnoughTrains)
        claimingPlayer <- gameMap.getPlayerClaimingRoute(connectedCities)
        _ <- check(claimingPlayer.isEmpty, GameMap.AlreadyClaimedRoute)
        _ <- player.playCards(color, nCards)
        _ <- player.placeTrains(routeLength)
        _ <- gameMap.claimRoute(connectedCities, player.id)
      // TODO update view
      yield ()) match
        case Right(_) => ()
        case Left(Player.NotEnoughCards) => println("NotEnoughCards") // TODO dialog in view: not enough cards
        case Left(Player.NotEnoughTrains) => println("NotEnoughTrains") // TODO dialog in view: not enough trains
        case Left(GameMap.AlreadyClaimedRoute) =>
          println("AlreadyClaimedRoute") // TODO dialog in view: already claimed route
        case Left(_) => throw new IllegalStateException("Unexpected error")

    private def check(condition: Boolean, error: GameError): Either[GameError, Unit] = Either.cond(condition, (), error)
