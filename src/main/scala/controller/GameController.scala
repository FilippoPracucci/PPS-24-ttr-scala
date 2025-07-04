package controller

/** Trait that represents the controller of the game.
  */
trait GameController
// TODO

object GameController:
  /** Returns the singleton instance of `GameController`.
    * @return
    *   the globally shared `GameController` instance
    */
  def apply(): GameController = GameControllerImpl

  private object GameControllerImpl extends GameController:
    import model.map.GameMap
    import GameMap.given
    import model.map.Route
    import model.utils.Color
    import Color._
    import view.GameView

    private val gameMap = GameMap()
    private val gameView = GameView()

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
