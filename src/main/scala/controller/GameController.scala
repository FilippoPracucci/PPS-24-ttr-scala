package controller

/** Trait that represents the controller of the game.
  */
trait GameController:
  import GameController.City

  /** Player action that consists in draw the given amount of the card from the deck.
    *
    * @param n
    *   the amount of train cards to draw.
    */
  def drawCards(n: Int): Unit

  /** Claims the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    */
  def claimRoute(connectedCities: (City, City)): Unit

object GameController:
  /** Type alias that represents the city as String by its name.
    */
  export model.player.{PlayerId, City, Points}

  /** Returns the singleton instance of `GameController`.
    * @return
    *   the globally shared `GameController` instance
    */
  def apply(): GameController = GameControllerImpl

  private object MapViewColorHelper:
    import model.utils.{Color, PlayerColor}
    extension (color: Color) def toMapViewColor: String = color.toString.toLowerCase()
    extension (playerColor: PlayerColor) def toMapViewColor: String = playerColor.toString.toLowerCase()

  private object ImportHelper:
    export CardControllerColor.*
    export model.map.{GameMap, Route}
    export GameMap.given
    export Route.*
    export model.utils.{Color, PlayerColor, GameError}
    export view.GameView
    export GameView.City
    export view.cards.{CardView, HandView}
    export MapViewColorHelper.*
    export model.cards.Deck
    export model.player.{Player, ObjectiveWithCompletion}

  private object GameControllerImpl extends GameController:
    import ImportHelper.*
    import ImportHelper.given

    private val gameMap = GameMap()
    private val deck: Deck = Deck()
    deck.shuffle()

    private val players: List[Player] = initPlayers()
    private val turnManager: TurnManager = TurnManager(players)

    private val handsView = initHandsView()
    private val gameView = GameView()

    initGameView()

    private def initPlayers(): List[Player] =
      import scala.util.Random // TODO: modify with real objective
      val list = List(ObjectiveWithCompletion(("Paris", "Berlin"), 8), ObjectiveWithCompletion(("Paris", "Venezia"), 4))
      var playerList: List[Player] = List.empty
      for
        color <- PlayerColor.values
      yield playerList :+= Player(color, deck, objective = list(Random.nextInt(list.size)))
      playerList

    private def initHandsView(): List[HandView] =
      players.map(p => HandView(p.hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor))))

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
      gameView.updatePlayerInfo(currentPlayer.id, currentPlayer.trains.trainCars)
      gameView.addHandView(handsView(players.indexOf(currentPlayer)))
      gameView.updateObjective(currentPlayerObjective)
      gameView.open()

    override def drawCards(n: Int): Unit =
      val initialHandCards = currentPlayer.hand.cards
      currentPlayer.drawCards(n)
      currentHandView.updateHand(currentPlayer.hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor)))
      switchTurn()

    override def claimRoute(connectedCities: (City, City)): Unit =
      val optionRoute = gameMap.getRoute(connectedCities)
      val route = optionRoute.getOrElse(
        throw new IllegalStateException(s"The route between $connectedCities doesn't exist")
      )
      route.mechanic match
        case SpecificColor(color) => ClaimRouteHelper.claimRoute(connectedCities, route.length)(route.length, color)
        case _ => throw new IllegalStateException("Unhandled mechanic")

    private object ClaimRouteHelper:
      def claimRoute(connectedCities: (City, City), routeLength: Int)(nCards: Int, color: Color): Unit =
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

      private def updateView(connectedCities: (City, City)): Unit =
        gameView.updateRoute(connectedCities, currentPlayer.id.toMapViewColor)
        currentHandView.updateHand(
          currentPlayer.hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor))
        )
        switchTurn()

    private def currentPlayer: Player = turnManager.currentPlayer

    private def currentPlayerObjective: ((City, City), Points) = currentPlayer.objective.unapply().get

    private def currentHandView: HandView = handsView(players.indexOf(currentPlayer))

    private def switchTurn(): Unit =
      import GameState.*
      turnManager.switchTurn()
      turnManager.gameState match
        case START_LAST_ROUND => gameView.startLastRound()
        case END_GAME => gameView.endGame()
        case _ => ()
      gameView.updatePlayerInfo(currentPlayer.id, currentPlayer.trains.trainCars)
      gameView.updateHandView(currentHandView)
      gameView.updateObjective(currentPlayerObjective)
