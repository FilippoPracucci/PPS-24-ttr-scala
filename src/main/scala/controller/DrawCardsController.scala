package controller

trait DrawCardsController:
  /** Player action that consists in draw the given amount of the card from the deck. */
  def drawCards(): Unit

object DrawCardsController:
  def apply(turnManager: TurnManager, viewController: ViewController): DrawCardsController =
    DrawCardsControllerImpl(turnManager, viewController)

  private class DrawCardsControllerImpl(turnManager: TurnManager, viewController: ViewController)
      extends DrawCardsController:

    override def drawCards(): Unit =
      import config.GameConfig.StandardNumberOfCardsToDraw
      turnManager.currentPlayer.drawCards(StandardNumberOfCardsToDraw) match
        case Right(_) =>
          turnManager.switchTurn()
          viewController.updateViewNewTurn()
        case Left(gameError) => viewController.reportError(gameError)
