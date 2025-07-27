package view

import map.MapView
import cards.HandView

/** Trait that represents the view of the game.
  *
  * @tparam Color
  *   the type of the colors
  */
trait GameView[Color] extends PlayerGameView:
  import GameView.{City, PlayerName, Points, MessageType}

  /** Opens the view.
    */
  def open(): Unit

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

  /** Shows a message to the user.
    *
    * @param message
    *   the message to show
    * @param title
    *   the title of the message
    * @param messageType
    *   the type of the message
    */
  def show(message: String, title: String, messageType: MessageType): Unit

  /** Show the message of end game to the user and then close the interface. */
  def endGame(playerScores: Seq[(PlayerName, Points)]): Unit

object GameView:
  /** Type alias that represents a city as String by its name.
    */
  type City = String

  /** Type alias that represents a player's name as String.
    */
  type PlayerName = String

  /** Type alias that represents points as Int.
    */
  type Points = Int

  /** Trait that represents a type of message to show.
    */
  trait MessageType
  object MessageType:
    /** Type used to show information.
      */
    case object Info extends MessageType

    /** Type used to report errors.
      */
    case object Error extends MessageType

    /** Type used to show responses.
      */
    case object Response extends MessageType

  /** Returns the singleton instance of `GameView`.
    *
    * @return
    *   the globally shared `GameView` instance
    */
  def apply(): GameView[java.awt.Color] = GameViewSwing

  private object GameViewSwing extends GameView[java.awt.Color]:
    import scala.swing.*
    import player.{BasicPlayerInfoView, BasicObjectiveView, PlayerScoresView, FinalRankingView}
    import MessageType.*
    import config.GameViewConfig.*

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

    override def show(message: String, title: String, messageType: MessageType): Unit =
      Dialog.showMessage(frame, message, title, messageType.toIcon)

    extension (messageType: MessageType)
      private def toIcon = messageType match
        case Info => Dialog.Message.Info
        case Error => Dialog.Message.Error
        case Response => Dialog.Message.Plain
        case _ => throw new IllegalArgumentException("Unexpected message type")

    override def endGame(playerScores: Seq[(PlayerName, Points)]): Unit =
      import scala.swing.Dialog.Result.*
      val options: Seq[String] = Seq(SeeFinalRanking, Close)
      Dialog.showOptions(frame, EndGameDescription, title = EndGameTitle, Dialog.Options.Default, Dialog.Message.Plain,
        entries = options, initial = options.indexOf(options.head)) match
        case Yes =>
          frame.contents = FinalRankingView(FinalRankingTitle)(playerScores).component
          frame.centerOnScreen()
        case _ => frame.close(); frame.closeOperation()

    export playerGameView.*
    export frame.open
    export mapView.{addRoute, updateRoute}
