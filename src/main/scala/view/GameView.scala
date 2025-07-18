package view

import map.{CitiesLoader, MapView}
import cards.HandView

/** Trait that represents the view of the game.
  */
trait GameView:
  import GameView.{City, Points, PlayerId, Color}

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

  /** Reports the error to the user.
    * @param message
    *   the message of the error
    */
  def reportError(message: String): Unit

  /** Show the last round start message to the user. */
  def startLastRound(): Unit

  /** Show the message of end game to the user and then close the interface. */
  def endGame(): Unit

object GameView:
  import controller.GameController

  /** Type alias that represents the city as String by its name, the points as Int and the player id as a Color.
    */
  export GameController.{City, Points, PlayerId}

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
    import player.{BasicPlayerInfoView, BasicObjectiveView}

    private val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
    private val panel = new BorderPanel()
    private val frame: MainFrame = new MainFrame {
      title = "Ticket to Ride"
      contents = panel
      resizable = false
    }
    private val insets = Toolkit.getDefaultToolkit.getScreenInsets(frame.peer.getGraphicsConfiguration)

    private val southPanel = new BoxPanel(Orientation.Horizontal)
    private val handPanel = new BoxPanel(Orientation.Horizontal)
    private val scrollPane = new ScrollPane(handPanel)
    private val eastPanel = new BoxPanel(Orientation.Vertical)
    private val drawButton = new Button("Draw")

    private val mapView = MapView()
    private val playerView = BasicPlayerInfoView()
    private val objectiveView = BasicObjectiveView()

    private val gameController: GameController = GameController()

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
        scrollPane.horizontalScrollBarPolicy = AsNeeded
        scrollPane.verticalScrollBarPolicy = Never
        southPanel.contents += scrollPane
        southPanel.contents += drawButton

      private def configEastPanel(): Unit =
        val EAST_PANEL_WIDTH_RATIO = 0.15
        eastPanel.contents += playerView.component
        eastPanel.contents += objectiveView.component
        eastPanel.preferredSize = new Dimension((frame.size.width * EAST_PANEL_WIDTH_RATIO).toInt, frame.size.height)

      private def configDrawButton(): Unit =
        drawButton.listenTo(drawButton.mouse.clicks)
        drawButton.reactions += {
          case _: ButtonClicked => gameController.drawCards(2)
          case _ => ()
        }

      private def initMap(): Unit =
        import CitiesLoader.given
        CitiesLoader(
          frame.size.width - eastPanel.peer.getPreferredSize.getWidth.toInt,
          frame.size.height - frame.peer.getInsets.top - southPanel.peer.getPreferredSize.getHeight.toInt
        ).load()

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

    override def endGame(): Unit =
      Dialog.showConfirmation(frame, "The game is over!", title = "End game", Options.Default) match
        case _ => close(); frame.closeOperation()

    export frame.{open, close}
    export mapView.{addRoute, updateRoute}
    export objectiveView.updateObjective
    export playerView.updatePlayerInfo
