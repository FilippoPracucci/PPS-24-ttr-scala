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
    *
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    */
  def claimRoute(connectedCities: (City, City)): Unit

  /** Show the rules of the game. */
  def showRules(): Unit

object GameController:
  /** Type aliases that represent the player identifier as Color, the city as String by its name and the points as Int.
    */
  export model.player.{PlayerId, City, Points}

  /** Type alias that represents the player as String by its name. */
  export view.GameView.PlayerName

  /** Returns the singleton instance of `GameController`.
    *
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
    export GameMap.defaultRoutesLoader
    export Route.*
    export model.utils.{Color, PlayerColor, GameError}
    export view.GameView
    export GameView.City
    export view.cards.{CardView, HandView}
    export MapViewColorHelper.*
    export model.cards.Deck
    export model.player.{Player, ObjectiveWithCompletion, ObjectivesLoader, ObjectiveChecker}
    private val routePointsManager = model.map.RoutePointsManager()
    export routePointsManager.points

  private object GameControllerImpl extends GameController:
    import ImportHelper.*
    import ImportHelper.given

    private val gameMap = GameMap()
    private val deck: Deck = Deck()
    deck.shuffle()

    private val players: List[Player] = initPlayers()
    private val turnManager: TurnManager = TurnManager(players)

    private val handView = HandView(currentHandCardsView)
    private val gameView = GameView()

    private val objectiveChecker = ObjectiveChecker(gameMap)

    initGameView()

    private def initPlayers(): List[Player] =
      import scala.util.Random
      var objectives = ObjectivesLoader().load()
      var objToAssign: Option[ObjectiveWithCompletion] = Option.empty
      var playerList: List[Player] = List.empty
      for
        color <- PlayerColor.values
      yield
        objToAssign = Option(objectives.toList(Random.nextInt(objectives.size)))
        objectives = objectives.excl(objToAssign.get)
        playerList :+= Player(color, deck, objective = objToAssign.get)
      playerList

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
      gameView.addHandView(handView)
      gameView.updateObjective(currentPlayerObjective)
      gameView.initPlayerScores(currentPlayerScores)
      gameView.open()

    extension (player: Player)
      private def name: String = player.id.toString.head.toUpper.toString + player.id.toString.tail.toLowerCase

    override def drawCards(n: Int): Unit =
      currentPlayer.drawCards(n) match
        case Right(_) => switchTurn()
        case _ => gameView.report("Error", "Not enough cards in the deck! It's possible only to claim a route!")

    override def claimRoute(connectedCities: (City, City)): Unit = gameMap.getRoute(connectedCities) match
      case Some(route) => route.mechanic match
          case SpecificColor(color) =>
            ClaimRouteHelper.claimRoute(connectedCities, route.length, route.points)(route.length, color)
          case _ => throw new IllegalStateException("Unhandled mechanic")
      case _ => throw new IllegalStateException(s"The route between $connectedCities doesn't exist")

    override def showRules(): Unit = gameView.showRules("RULES!")

    private object ClaimRouteHelper:
      def claimRoute(connectedCities: (City, City), routeLength: Int, routePoints: Points)(nCards: Int, color: Color)
          : Unit =
        (for
          claimingPlayer <- gameMap.getPlayerClaimingRoute(connectedCities)
          _ <- check(claimingPlayer.isEmpty, GameMap.AlreadyClaimedRoute)
          _ <- check(currentPlayer.trains.trainCars >= routeLength, Player.NotEnoughTrains)
          _ <- check(currentPlayer.canPlayCards(color, nCards), Player.NotEnoughCards)
          _ <- gameMap.claimRoute(connectedCities, currentPlayer.id)
          _ <- currentPlayer.placeTrains(routeLength)
          _ <- currentPlayer.playCards(color, nCards)
        yield ()) match
          case Right(_) =>
            currentPlayer.addPoints(routePoints)
            updateRouteView(connectedCities)
            checkObjectiveCompletion()
            switchTurn()
          case Left(GameMap.AlreadyClaimedRoute) =>
            gameView.report("Error", "Can't claim a route that has already been claimed!")
          case Left(Player.NotEnoughTrains) => gameView.report("Error", "Not enough trains to claim this route!")
          case Left(Player.NotEnoughCards) => gameView.report("Error", "Not enough cards to claim this route!")
          case Left(_) => throw new IllegalStateException("Unexpected error")

      private def check(condition: Boolean, err: GameError): Either[GameError, Unit] = Either.cond(condition, (), err)

      private def updateRouteView(connectedCities: (City, City)): Unit =
        gameView.updateRoute(connectedCities, currentPlayer.id.toMapViewColor)
        gameView.updatePlayerScore(currentPlayer.name, currentPlayer.score)

      private def checkObjectiveCompletion(): Unit =
        Option(currentPlayer.objective)
          .filter(!_.completed)
          .filter(objective => objectiveChecker.check(objective, currentPlayer.id))
          .foreach(objective =>
            objective.markAsComplete()
            currentPlayer.addPoints(objective.points)
            updateObjectiveView()
          )

      private def updateObjectiveView(): Unit =
        gameView.report("Objective completed",
          s"You have completed your objective! You gain ${currentPlayer.objective.points} points!")
        // TODO update objective checkbox
        gameView.updatePlayerScore(currentPlayer.name, currentPlayer.score)

    private def currentPlayer: Player = turnManager.currentPlayer

    private def currentPlayerObjective: ((City, City), Points) = currentPlayer.objective.unapply().get

    private def currentPlayerScores: Seq[(PlayerName, Points)] = players.map(player => (player.name, player.score))

    private def switchTurn(): Unit =
      import GameState.*
      turnManager.switchTurn()
      turnManager.gameState match
        case START_LAST_ROUND => gameView.startLastRound()
        case END_GAME => gameView.endGame(currentPlayerScores)
        case _ => ()
      handView.updateHand(currentHandCardsView)
      gameView.updatePlayerInfo(currentPlayer.id, currentPlayer.trains.trainCars)
      gameView.updateHandView(handView)
      gameView.updateCompletionCheckBox(currentPlayer.objective.completed)
      gameView.updateObjective(currentPlayerObjective)

    private def currentHandCardsView: List[CardView] =
      currentPlayer.hand.cards.map(c => CardView(c.cardName)(c.cardColor, c.cardTextColor))
