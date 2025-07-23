package controller

trait DrawCardsController:
  /** Player action that consists in draw the given amount of the card from the deck.
    *
    * @param n
    *   the amount of train cards to draw.
    */
  def drawCards(n: Int): Unit

object DrawCardsController:
  def apply(turnManager: TurnManager, viewController: ViewController): DrawCardsController =
    DrawCardsControllerImpl(turnManager, viewController)

  private class DrawCardsControllerImpl(turnManager: TurnManager, viewController: ViewController)
      extends DrawCardsController:

    override def drawCards(n: Int): Unit =
      turnManager.currentPlayer.drawCards(n) match
        case Right(_) =>
          turnManager.switchTurn()
          viewController.updateViewNewTurn()
        case Left(gameError) => viewController.reportError(gameError)
