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
    import view.GameView

    private val gameMap = GameMap()
    private val gameView = GameView()

    initGameView()

    private def initGameView(): Unit =
      gameMap.routes.foreach(route =>
        gameView.addRoute(
          (route.connectedCities._1.name, route.connectedCities._2.name),
          route.length
        )
      )
      gameView.open()
