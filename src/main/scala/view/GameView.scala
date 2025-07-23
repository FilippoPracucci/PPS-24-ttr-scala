package view

import map.{CitiesLoader, MapView}
import cards.HandView

/** Trait that represents the view of the game.
  */
trait GameView extends PlayerGameView:
  import GameView.{City, Points, Color, PlayerName}

  /** Opens the view.
    */
  def open(): Unit

  /** Closes the view.
    */
  def close(): Unit

  /** Adds a new route in the game map view.
    *
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    * @param length
    *   the length of the route
    * @param color
    *   the color of the route expressed as the name of the color in lowercase
    */
  def addRoute(connectedCities: (City, City), length: Int, color: Color): Unit

  /** Updates the route connecting the specified cities.
    *
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    * @param color
    *   the new color of the route, expressed as the name of the color in lowercase
    */
  def updateRoute(connectedCities: (City, City), color: Color): Unit

  /** Reports a message to the user.
    *
    * @param messageType
    *   the type of the message to report
    * @param message
    *   the message to report
    */
  def report(messageType: String, message: String): Unit

  /** Show the rules of the game.
    *
    * @param description
    *   the description of the rules.
    */
  def showRules(description: String): Unit

  /** Show the last round start message to the user. */
  def startLastRound(): Unit

  /** Show the message of end game to the user and then close the interface. */
  def endGame(playerScores: Seq[(PlayerName, Points)]): Unit

object GameView:
  import controller.GameController

  /** Type alias that represents the city as String by its name, the points as Int and the player id as a Color.
    */
  export GameController.{City, Points, PlayerId}

  /** Type alias that represents the player's name.
    */
  type PlayerName = String // TODO to integrate

  /** Type alias that represents the color as String by its name in lowercase. // TODO
    */
  export MapView.Color

  /** Returns the singleton instance of `GameView`.
    *
    * @return
    *   the globally shared `GameView` instance
    */
  def apply(): GameView = GameViewSwing

  private object GameViewSwing extends GameView:
    import scala.swing._
    import Dialog.Options
    import config.GameViewConfig.*
    import player.{BasicPlayerInfoView, BasicObjectiveView, PlayerScoresView, FinalRankingView}

    private val mapView = MapView()
    private val playerInfoView = BasicPlayerInfoView(PlayerInfoTitle)
    private val objectiveView = BasicObjectiveView(ObjectiveTitle)
    private val playerScoresView = PlayerScoresView(PlayerScoresTitle)
    private val handPanel = new BoxPanel(Orientation.Horizontal)
    private val frame: MainFrame = new MainFrame()
    private val initViewHelper = InitViewHelper(frame, handPanel, mapView, objectiveView,
      playerInfoView, playerScoresView)
    private val playerGameView = PlayerGameView(frame, handPanel, objectiveView, playerInfoView, playerScoresView)

    initViewHelper.setFrame()
    initViewHelper.initPanels()

    override def report(messageType: String, message: String): Unit =
      Dialog.showMessage(frame, message, title = messageType)

    override def showRules(description: String): Unit =
      Dialog.showMessage(frame, description, title = RulesTitle, Dialog.Message.Plain)

    override def startLastRound(): Unit =
      Dialog.showConfirmation(frame, StartLastRoundDescription, title = StartLastRoundTitle, Options.Default)

    override def endGame(playerScores: Seq[(PlayerName, Points)]): Unit =
      import scala.swing.Dialog.Result.*
      val options: Seq[String] = Seq(SeeFinalRanking, Close)
      Dialog.showOptions(frame, EndGameDescription, title = EndGameTitle, Options.Default, Dialog.Message.Plain,
        entries = options, initial = options.indexOf(options.head)) match
        case Yes =>
          frame.contents = FinalRankingView(FinalRankingTitle)(playerScores).component
          frame.centerOnScreen()
        case _ => close(); frame.closeOperation()

    export playerGameView.*
    export frame.{open, close}
    export mapView.{addRoute, updateRoute}
