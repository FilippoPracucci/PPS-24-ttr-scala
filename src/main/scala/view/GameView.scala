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

  /** Add the hands component to the view.
    *
    * @param handsView
    *   the list of hands view to add to the panel.
    */
  def addHandsView(handsView: List[HandView]): Unit

  /** Update the hands view component.
    *
    * @param handsView
    *   the list of hands view component.
    */
  def updateHandsView(handsView: List[HandView]): Unit

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
    import scala.swing.*
    import event.MousePressed
    import ScrollPane.BarPolicy.*
    import java.awt.Toolkit

    private val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
    private val panel = new BoxPanel(Orientation.Vertical)
    private val frame: MainFrame = new MainFrame {
      title = "Ticket to Ride"
      contents = panel
      resizable = false
    }
    private val insets = Toolkit.getDefaultToolkit.getScreenInsets(frame.peer.getGraphicsConfiguration)

    private val southPanel = new BoxPanel(Orientation.Horizontal)
    private val handPanel = new BoxPanel(Orientation.Horizontal)
    private val scrollPane = new ScrollPane(handPanel)
    private val handButtonPanel = new BoxPanel(Orientation.Vertical)
    private val drawButton = new Button("Draw")
    private val groupByColorButton = new Button("Group by color")

    private val mapView = MapView()

    private val gameController: GameController = GameController()

    InitHelper.setFrameSize()
    InitHelper.initPanels()

    private object InitHelper:
      def setFrameSize(): Unit =
        frame.size = new Dimension(screenSize.width - insets.left - insets.right,
          screenSize.height - insets.bottom - insets.top)

      def initPanels(): Unit =
        handButtonPanel.contents += drawButton
        configDrawButton()
        handButtonPanel.contents += groupByColorButton
        configGroupByColorButton()
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
          case _: MousePressed => gameController.drawCards(2)
          case _ => ()
        }

      private def configGroupByColorButton(): Unit =
        groupByColorButton.listenTo(groupByColorButton.mouse.clicks)
        groupByColorButton.reactions += {
          case _: MousePressed => gameController.groupCardsByColor()
          case _ => ()
        }

      private def initMap(): Unit =
        import CitiesLoader.given
        CitiesLoader(frame.size.width,
          frame.size.height - frame.peer.getInsets.top - southPanel.peer.getPreferredSize.getHeight.toInt).load()

    override def addHandsView(handsView: List[HandView]): Unit =
      handPanel.contents ++= handsView.map(_.handComponent)

    override def updateHandsView(handsView: List[HandView]): Unit =
      handPanel.contents.clear()
      addHandsView(handsView)
      frame.validate()

    override def reportError(message: String): Unit = Dialog.showMessage(frame, message, title = "Error")

    export frame.{open, close}
    export mapView.{addRoute, updateRoute}
