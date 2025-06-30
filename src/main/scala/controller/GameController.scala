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
    import view.GameView
    private val gameView = GameView()

    initGameView()

    private def initGameView(): Unit =
      gameView.addRoute(("Roma", "Palermo"), 4)
      gameView.addRoute(("Roma", "Venezia"), 2)
      gameView.addRoute(("Roma", "Brindisi"), 2)
      gameView.addRoute(("Palermo", "Brindisi"), 3)
      gameView.open()
