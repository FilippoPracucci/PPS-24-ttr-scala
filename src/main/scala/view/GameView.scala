package view

import map.{CitiesLoader, MapView}
import view.cards.HandView

/** Trait that represents the view of the game.
  */
trait GameView:
  import GameView.{City, Color}

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
    import scala.swing.event.ButtonClicked
    import controller.GameController

    private val screenSize: Dimension = java.awt.Toolkit.getDefaultToolkit.getScreenSize
    private val panel = new BoxPanel(Orientation.Vertical)
    private val frame: MainFrame = new MainFrame {
      title = "Ticket to Ride"
      contents = panel
      preferredSize = screenSize
      peer.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
      resizable = false
    }

    private val southPanel = new BoxPanel(Orientation.Horizontal)
    private val handPanel = new BoxPanel(Orientation.Horizontal)
    private val scrollPane = new ScrollPane(handPanel)
    private val handButtonPanel = new BoxPanel(Orientation.Vertical)
    private val drawButton = new Button("Draw")

    private val mapView = MapView()

    private val gameController: GameController = GameController()

    InitHelper.initPanels()

    private object InitHelper:
      def initPanels(): Unit =
        handButtonPanel.contents += drawButton
        configDrawButton()
        scrollPane.horizontalScrollBarPolicy = AsNeeded
        scrollPane.verticalScrollBarPolicy = Never
        southPanel.contents += scrollPane
        southPanel.contents += handButtonPanel
        panel.contents += mapView.component
        initMap()
        panel.contents += southPanel
        frame.repaint()

      private def configDrawButton(): Unit =
        drawButton.listenTo(drawButton.mouse.clicks)
        drawButton.reactions += {
          case _: ButtonClicked => gameController.drawCards(2)
          case _ => ()
        }

      private def initMap(): Unit =
        import CitiesLoader.given
        CitiesLoader(screenSize.width, screenSize.height - southPanel.peer.getPreferredSize.getHeight.toInt).load()

    override def addHandView(handView: HandView): Unit =
      handPanel.contents += handView.handComponent

    override def updateHandView(handView: HandView): Unit =
      handPanel.contents.clear()
      addHandView(handView)
      frame.validate()

    override def reportError(message: String): Unit = Dialog.showMessage(frame, message, title = "Error")

    export frame.{open, close}
    export mapView.{addRoute, updateRoute}
