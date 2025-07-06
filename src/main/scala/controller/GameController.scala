package controller

import model.cards.Deck
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

  /** Player action that consists in draw the given amount of the card from the deck.
    *
    * @param n
    *   the amount of train cards to draw.
    */
  def drawCards(n: Int): Unit

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
    import model.utils.{Color, PlayerColor}
    import Color._
    import view.GameView
    import view.cards.HandView

    override val deck: Deck = Deck()
    deck.shuffle()

    override val players: List[Player] = initPlayers()

    private val gameMap = GameMap()
    private val gameView = GameView()
    private val handsView = initHandsView()

    override def drawCards(n: Int): Unit =
      val initialHandCards = players.head.hand.cards
      players.head.drawCards(n)
      handsView.head.addCardsComponent(players.head.hand.cards diff initialHandCards)
      gameView.updateHandsView(handsView)

    // for the moment a single player
    private def initPlayers(): List[Player] =
      List(Player(PlayerColor.GREEN, deck))

    private def initHandsView(): List[HandView] =
      players.map(p => HandView(p.hand))

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
      gameView.addHandsView(handsView)
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
