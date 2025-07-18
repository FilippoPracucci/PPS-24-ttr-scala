package view

import map.{CitiesLoader, MapView}
import view.cards.HandView

/** Trait that represents the view of the game.
  */
trait GameView:
  import GameView.{City, Color, PlayerName}

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
  def initPlayerScores(playerScores: Seq[(PlayerName, Int)]): Unit

  /** Updates the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    * @param color
    *   the new color of the route, expressed as the name of the color in lowercase
    */
  def updateRoute(connectedCities: (City, City), color: Color): Unit

  /** Updates the score of the specified player.
    *
    * @param player
    *   the name of the player whose score to update
    * @param score
    *   the new score of the player
    */
  def updatePlayerScore(player: PlayerName, score: Int): Unit

  /** Reports the error to the user.
    * @param message
    *   the message of the error
    */
  def reportError(message: String): Unit

object GameView:
  import controller.GameController

  /** Type alias that represents the city as String by its name.
    */
  export GameController.City

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
    import scala.swing.event.ButtonClicked
    import controller.GameController

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
    private val handButtonPanel = new BoxPanel(Orientation.Vertical)
    private val drawButton = new Button("Draw")

    private val eastPanel = new BoxPanel(Orientation.Vertical)
    private val scoreboardPanel = new BoxPanel(Orientation.Vertical)
    private var scoreLabels: Map[String, Label] = Map()

    private val mapView = MapView()

    InitHelper.setFrameSize()
    InitHelper.initPanels()

    private object InitHelper:
      def setFrameSize(): Unit =
        frame.size = new Dimension(screenSize.width - insets.left - insets.right,
          screenSize.height - insets.bottom - insets.top)

      def initPanels(): Unit =
        handButtonPanel.contents += drawButton
        configDrawButton()
        scrollPane.horizontalScrollBarPolicy = AsNeeded
        scrollPane.verticalScrollBarPolicy = Never
        southPanel.contents += scrollPane
        southPanel.contents += handButtonPanel
        scoreboardPanel.border = Swing.EmptyBorder(10, 10, 10, 10)
        scoreboardPanel.contents += new BoxPanel(Orientation.Horizontal) {
          contents += new Label("PLAYER SCORES")
        }
        eastPanel.contents += scoreboardPanel
        panel.layout(mapView.component) = BorderPanel.Position.Center
        initMap()
        panel.layout(southPanel) = BorderPanel.Position.South
        panel.layout(eastPanel) = BorderPanel.Position.East
        frame.repaint()

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

    override def initPlayerScores(playerScores: Seq[(PlayerName, Int)]): Unit =
      scoreLabels = playerScores.map((player, score) => (player, new Label(score.toString))).toMap
      scoreLabels.foreach((player, scoreLabel) =>
        scoreboardPanel.contents += new BoxPanel(Orientation.Horizontal) {
          contents += new Label(player + ":")
          contents += Swing.HGlue
          contents += scoreLabel
        }
      )
      scoreboardPanel.contents.foreach(_.updateLabelFont(15f))

    extension (component: Component)
      private def updateLabelFont(size: Float): Unit = component match
        case panel: Panel => panel.contents.foreach {
            case label: Label => label.font = label.font.deriveFont(15f)
            case _ => ()
          }
        case _ => ()

    override def updatePlayerScore(player: PlayerName, score: Int): Unit = scoreLabels(player).text = score.toString

    override def reportError(message: String): Unit = Dialog.showMessage(frame, message, title = "Error")

    export frame.{open, close}
    export mapView.{addRoute, updateRoute}
