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

  /** Reorder the representation of train cards of the player's hand, grouping them by color. */
  def groupCardsByColor(): Unit

  /** Claims the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names as String
    */
  def claimRoute(connectedCities: (String, String)): Unit

object GameController:
  /** Returns the singleton instance of `GameController`.
    * @return
    *   the globally shared `GameController` instance
    */
  def apply(): GameController = GameControllerImpl

  private val cardController = CardController()
  export cardController.*

  private object MapViewColorHelper:
    import model.utils.{Color, PlayerColor}
    extension (color: Color) def toMapViewColor: String = color.toString.toLowerCase()
    extension (playerColor: PlayerColor) def toMapViewColor: String = playerColor.toString.toLowerCase()

  private object ImportHelper:
    export model.map.{GameMap, Route}
    export GameMap.given
    export Route.*
    export model.utils.{Color, PlayerColor, GameError}
    export view.GameView
    export view.cards.{CardView, HandView}
    export MapViewColorHelper.*

  private object GameControllerImpl extends GameController:
    import ImportHelper.*
    import ImportHelper.given

    private val gameMap = GameMap()
    override val deck: Deck = Deck()
    deck.shuffle()

    override val players: List[Player] = initPlayers()
    private val currentPlayer: Player = players.head

    private val handsView = initHandsView()
    private val gameView = GameView()

    initGameView()

    // for the moment a single player
    private def initPlayers(): List[Player] =
      List(Player(PlayerColor.GREEN, deck))

    private def initHandsView(): List[HandView] =
      players.map(p => HandView(p.hand.cards.map(c => CardView(c.colorName)(c.cardColor, c.cardTextColor))))

    private def initGameView(): Unit =
      gameMap.routes.foreach(route =>
        gameView.addRoute(
          (route.connectedCities._1.name, route.connectedCities._2.name),
          route.length,
          route.mechanic match
            case Route.SpecificColor(color) => color.toMapViewColor
            case _ => throw new IllegalStateException("Unhandled mechanic")
        )
      )
      gameView.addHandsView(handsView)
      gameView.open()

    override def drawCards(n: Int): Unit =
      val initialHandCards = currentPlayer.hand.cards
      currentPlayer.drawCards(n) // TODO: change for the list of players
      handsView.head.updateHand(currentPlayer.hand.cards.map(c => CardView(c.colorName)(c.cardColor, c.cardTextColor)))
      gameView.updateHandsView(handsView)

    override def groupCardsByColor(): Unit =
      handsView.head.groupCardsComponentByColor()
      gameView.updateHandsView(handsView)

    override def claimRoute(connectedCities: (String, String)): Unit =
      val optionRoute = gameMap.getRoute(connectedCities)
      val route = optionRoute.getOrElse(
        throw new IllegalStateException(s"The route between $connectedCities doesn't exist")
      )
      route.mechanic match
        case SpecificColor(color) => ClaimRouteHelper.claimRoute(connectedCities, route.length)(route.length, color)
        case _ => throw new IllegalStateException("Unhandled mechanic")

    private object ClaimRouteHelper:
      def claimRoute(connectedCities: (String, String), routeLength: Int)(nCards: Int, color: Color): Unit =
        (for
          claimingPlayer <- gameMap.getPlayerClaimingRoute(connectedCities)
          _ <- check(claimingPlayer.isEmpty, GameMap.AlreadyClaimedRoute)
          _ <- check(currentPlayer.trains.trainCars >= routeLength, Player.NotEnoughTrains)
          _ <- check(currentPlayer.canPlayCards(color, nCards), Player.NotEnoughCards)
          _ <- gameMap.claimRoute(connectedCities, currentPlayer.id)
          _ <- currentPlayer.placeTrains(routeLength)
          _ <- currentPlayer.playCards(color, nCards)
        yield ()) match
          case Right(_) => updateView(connectedCities)
          case Left(GameMap.AlreadyClaimedRoute) =>
            gameView.reportError("Can't claim a route that has already been claimed!")
          case Left(Player.NotEnoughTrains) => gameView.reportError("Not enough trains to claim this route!")
          case Left(Player.NotEnoughCards) => gameView.reportError("Not enough cards to claim this route!")
          case Left(_) => throw new IllegalStateException("Unexpected error")

      private def check(condition: Boolean, err: GameError): Either[GameError, Unit] = Either.cond(condition, (), err)

      private def updateView(connectedCities: (String, String)): Unit =
        handsView.head.updateHand(
          currentPlayer.hand.cards.map(c => CardView(c.colorName)(c.cardColor, c.cardTextColor))
        )
        gameView.updateHandsView(handsView)
        gameView.updateRoute(connectedCities, currentPlayer.id.toMapViewColor)
