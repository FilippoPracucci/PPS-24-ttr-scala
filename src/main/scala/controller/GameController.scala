package controller

/** Trait that represents the controller of the game.
  */
trait GameController extends DrawCardsController with ClaimRouteController:
  /** Show the rules of the game. */
  def showRules(): Unit

object GameController:
  /** Type alias that represents the city as String by its name.
    */
  export model.player.{PlayerId, City, Points}

  /** Returns the singleton instance of `GameController`.
    *
    * @return
    *   the globally shared `GameController` instance
    */
  def apply(): GameController = GameControllerImpl

  private object ImportHelper:
    export model.map.GameMap
    export GameMap.defaultRoutesLoader
    export model.utils.PlayerColor
    export model.cards.Deck
    export model.player.{Player, ObjectiveWithCompletion, ObjectivesLoader}

  private object GameControllerImpl extends GameController:
    import ImportHelper.*
    import ImportHelper.given

    private val gameMap = GameMap()
    private val deck: Deck = Deck()
    deck.shuffle()

    private val players: List[Player] = initPlayers()
    private val turnManager: TurnManager = TurnManager(players)

    private val viewController = ViewController(turnManager, players)

    private val drawCardsController = DrawCardsController(turnManager, viewController)
    private val claimRouteController = ClaimRouteController(turnManager, viewController, gameMap)

    viewController.initGameView(gameMap)

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

    export drawCardsController.drawCards
    export claimRouteController.claimRoute
    export viewController.showRules
