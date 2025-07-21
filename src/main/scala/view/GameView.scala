package view

import map.{CitiesLoader, MapView}
import cards.HandView

import javax.swing.BorderFactory

/** Trait that represents the view of the game.
  */
trait GameView:
  import GameView.{City, Points, PlayerId, Color, PlayerName}

  /** Opens the view.
    */
  def open(): Unit

  /** Closes the view.
    */
  def close(): Unit

  /** Adds a new route in the game map view.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    * @param length
    *   the length of the route
    * @param color
    *   the color of the route expressed as the name of the color in lowercase
    */
  def addRoute(connectedCities: (City, City), length: Int, color: Color): Unit

  /** Add the hand component to the view.
    *
    * @param handView
    *   the hand view to add to the panel.
    */
  def addHandView(handView: HandView): Unit

  /** Update the hand view component.
    *
    * @param handView
    *   the hand view component to update.
    */
  def updateHandView(handView: HandView): Unit

  /** Initialize player scores.
    *
    * @param playerScores
    *   the list of player scores consisting of pairs "player's name; score"
    */
  def initPlayerScores(playerScores: Seq[(PlayerName, Points)]): Unit

  /** Updates the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    * @param color
    *   the new color of the route, expressed as the name of the color in lowercase
    */
  def updateRoute(connectedCities: (City, City), color: Color): Unit

  /** Update the objective view.
    *
    * @param objective
    *   the pair of cities to connect and its value in terms of points.
    */
  def updateObjective(objective: ((City, City), Points)): Unit

  /** Update the player information view.
    *
    * @param playerId
    *   the identifier of the player.
    * @param trains
    *   the number of train cars left to the player.
    */
  def updatePlayerInfo(playerId: PlayerId, trains: Int): Unit

  /** Updates the score of the specified player.
    *
    * @param player
    *   the name of the player whose score to update
    * @param score
    *   the new score of the player
    */
  def updatePlayerScore(player: PlayerName, score: Points): Unit

  /** Reports the error to the user.
    * @param message
    *   the message of the error
    */
  def reportError(message: String): Unit

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
  type PlayerName = String

  /** Type alias that represents the color as String by its name in lowercase. // TODO
    */
  export MapView.Color

  /** Returns the singleton instance of `GameView`.
    * @return
    *   the globally shared `GameView` instance
    */
  def apply(): GameView = GameViewSwing

  private object GameViewSwing extends GameView:
    import scala.swing._
    import ScrollPane.BarPolicy.*
    import java.awt.Toolkit
    import Dialog.Options
    import event.ButtonClicked
    import controller.GameController
    import player.{BasicPlayerInfoView, BasicObjectiveView, PlayerScoresView, FinalRankingView}

    private val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
    private val panel = new BorderPanel()
    private val frame: MainFrame = new MainFrame {
      title = "Ticket to Ride"
      contents = panel
      resizable = false
    }
    private val insets = Toolkit.getDefaultToolkit.getScreenInsets(frame.peer.getGraphicsConfiguration)

    private val gameController: GameController = GameController()

    private val southPanel = new BoxPanel(Orientation.Horizontal)
    private val handPanel = new BoxPanel(Orientation.Horizontal)
    private val scrollPane = new ScrollPane(handPanel)
    private val eastPanel = new BoxPanel(Orientation.Vertical)
    private val drawButton = new Button("Draw")

    private val mapView = MapView()
    private val playerInfoView = BasicPlayerInfoView("PLAYER INFO")
    private val objectiveView = BasicObjectiveView("OBJECTIVE")
    private val playerScoresView = PlayerScoresView("PLAYER SCORES")

    InitHelper.setFrameSize()
    InitHelper.initPanels()

    private object InitHelper:
      def setFrameSize(): Unit =
        frame.size = new Dimension(screenSize.width - insets.left - insets.right,
          screenSize.height - insets.bottom - insets.top)

      def initPanels(): Unit =
        configDrawButton()
        configSouthPanel()
        configEastPanel()
        panel.layout ++= List((mapView.component, BorderPanel.Position.Center),
          (southPanel, BorderPanel.Position.South), (eastPanel, BorderPanel.Position.East))
        initMap()
        frame.repaint()

      private def configSouthPanel(): Unit =
        import java.awt.Color.*
        val borderWeight = 5
        val borderThickness = 1
        val borderColor = LIGHT_GRAY
        scrollPane.horizontalScrollBarPolicy = AsNeeded
        scrollPane.verticalScrollBarPolicy = Never
        scrollPane.border = Swing.CompoundBorder(
          Swing.EmptyBorder(borderWeight),
          BorderFactory.createLineBorder(borderColor, borderThickness, true)
        )
        southPanel.contents += scrollPane
        southPanel.contents += drawButton
        southPanel.border = Swing.EmptyBorder(borderWeight)

      private def configEastPanel(): Unit =
        val eastPanelWidthRatio = 0.15
        val objectiveViewHeightRatio = 0.25
        val playerScoresViewHeightRatio = 0.4
        val borderWeight = 1
        eastPanel.preferredSize = new Dimension((frame.size.width * eastPanelWidthRatio).toInt, frame.size.height)
        eastPanel.contents += playerInfoView.component
        eastPanel.contents += objectiveView.component
        eastPanel.contents += playerScoresView.component
        eastPanel.border = Swing.EmptyBorder(borderWeight)
        objectiveView.component.preferredSize = new Dimension(eastPanel.preferredSize.width,
          (eastPanel.preferredSize.height * objectiveViewHeightRatio).toInt)
        playerScoresView.component.preferredSize = new Dimension(eastPanel.preferredSize.width,
          (eastPanel.preferredSize.height * playerScoresViewHeightRatio).toInt)

      private def configDrawButton(): Unit =
        drawButton.listenTo(drawButton.mouse.clicks)
        drawButton.reactions += {
          case _: ButtonClicked => gameController.drawCards(2)
          case _ => ()
        }

      private def initMap(): Unit =
        CitiesLoader(
          frame.size.width - eastPanel.peer.getPreferredSize.getWidth.toInt,
          frame.size.height - frame.peer.getInsets.top - southPanel.peer.getPreferredSize.getHeight.toInt
        )().load()

    override def addHandView(handView: HandView): Unit =
      handPanel.contents += handView.handComponent

    override def updateHandView(handView: HandView): Unit =
      handPanel.contents.clear()
      addHandView(handView)
      frame.validate()

    override def reportError(message: String): Unit = Dialog.showMessage(frame, message, title = "Error")

    override def startLastRound(): Unit =
      Dialog.showConfirmation(frame, "Start of the final round, so last turn for each player!",
        title = "Last round", Options.Default)

    override def endGame(playerScores: Seq[(PlayerName, Points)]): Unit =
      import scala.swing.Dialog.Result.*
      val options: Seq[String] = Seq("See the final ranking", "Close")
      Dialog.showOptions(frame, "The game is over!", title = "End game", Options.Default, Dialog.Message.Plain,
        entries = options, initial = options.indexOf(options.head)) match
        case Yes =>
          frame.contents = FinalRankingView("PLAYERS RANKING")(playerScores).component
          frame.centerOnScreen()
        case _ => close(); frame.closeOperation()

    export frame.{open, close}
    export mapView.{addRoute, updateRoute}
    export objectiveView.updateObjective
    export playerInfoView.updatePlayerInfo
    export playerScoresView.{initPlayerScores, updatePlayerScore}
