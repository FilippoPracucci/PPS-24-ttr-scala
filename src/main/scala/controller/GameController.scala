package controller

import model.cards.{Card, Deck}
import model.player.Player

/** Trait that represents the controller of the game.
  */
trait GameController:
// TODO
  /** The singleton instance of [[Deck]].
    *
    * @return
    *   the globally shared [[Deck]] instance.
    */
  def deck: Deck

  /** The singleton instance of the [[Player]] list.
    *
    * @return
    *   the globally shared [[Player]] list instance.
    */
  def players: List[Player]

  /** The singleton instance of the [[TurnManager]].
    *
    * @return
    *   the globally shared [[TurnManager]] instance.
    */
  def turnManager: TurnManager

  /** Player action that consists in draw the given amount of the card from the deck.
    *
    * @param n
    *   the amount of train cards to draw.
    */
  def drawCards(n: Int): Unit

  /** Reorder the representation of train cards of the player's hand, grouping them by color. */
  def groupCardsByColor(): Unit

object GameController:
  /** Returns the singleton instance of `GameController`.
    * @return
    *   the globally shared `GameController` instance
    */
  def apply(): GameController = GameControllerImpl

  private val cardController = CardController()
  export cardController.*

  private object GameControllerImpl extends GameController:
    import model.map.GameMap
    import GameMap.given
    import model.map.Route
    import model.utils.{Color, PlayerColor}
    import Color._
    import view.GameView
    import view.cards.{CardView, HandView}

    override val deck: Deck = Deck()
    deck.shuffle()

    override val players: List[Player] = initPlayers()

    override val turnManager: TurnManager = TurnManager(players)

    private val gameMap = GameMap()
    private val gameView = GameView()
    private val handsView = initHandsView()

    override def drawCards(n: Int): Unit =
      val initialHandCards = currentPlayer.hand.cards
      currentPlayer.drawCards(n)
      val cardsToAdd = currentPlayer.hand.cards diff initialHandCards
      currentHandView.addCardsComponent(cardsToAdd.map(c => CardView(c.colorName)(c.cardColor, c.cardTextColor)))
      turnManager.switchTurn()
      gameView.updateHandView(currentHandView)

    override def groupCardsByColor(): Unit =
      currentHandView.groupCardsComponentByColor()
      gameView.updateHandView(currentHandView)

    private def initPlayers(): List[Player] =
      var playerList: List[Player] = List.empty
      for
        color <- PlayerColor.values
      yield playerList :+= Player(color, deck)
      playerList

    private def initHandsView(): List[HandView] =
      players.map(p => HandView(p.hand.cards.map(c => CardView(c.colorName)(c.cardColor, c.cardTextColor))))

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
      gameView.addHandView(handsView(players.indexOf(currentPlayer)))
      gameView.open()

    private def currentPlayer: Player = turnManager.currentPlayer

    private def currentHandView: HandView = handsView(players.indexOf(currentPlayer))

    private def getMapViewColorFrom(color: Color): String = color match
      case BLACK => "black"
      case WHITE => "white"
      case RED => "red"
      case BLUE => "blue"
      case ORANGE => "orange"
      case YELLOW => "yellow"
      case GREEN => "green"
      case PINK => "pink"
