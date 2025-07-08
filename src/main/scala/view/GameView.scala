package view

import map.{CitiesLoader, MapView}
import view.cards.HandView

/** Trait that represents the view of the game.
  */
trait GameView:
  // TODO
  /** Opens the view.
    */
  def open(): Unit

  /** Closes the view.
    */
  def close(): Unit

  /** Adds a new route in the game map view.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names as String
    * @param length
    *   the length of the route
    * @param color
    *   the color of the route expressed as the name of the color in lowercase
    */
  def addRoute(connectedCities: (String, String), length: Int, color: String): Unit

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

object GameView:
  /** Returns the singleton instance of `GameView`.
    * @return
    *   the globally shared `GameView` instance
    */
  def apply(): GameView = GameViewSwing

  private object GameViewSwing extends GameView:
    import scala.swing._
    import ScrollPane.BarPolicy.*
    import scala.swing.event.MousePressed
    import controller.GameController

    private val screenSize: Dimension = java.awt.Toolkit.getDefaultToolkit.getScreenSize
    private val mapView = MapView()
    private val panel = new BoxPanel(Orientation.Vertical)
    private val southPanel = new BoxPanel(Orientation.Horizontal)
    private val handPanel = new BoxPanel(Orientation.Horizontal)
    private val scrollPane = new ScrollPane(handPanel)
    private val handButtonPanel = new BoxPanel(Orientation.Vertical)
    private val frame: MainFrame = new MainFrame {
      title = "Ticket to Ride"
      contents = panel
      preferredSize = screenSize
      peer.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
      resizable = false
    }
    private val gameController: GameController = GameController()
    private val drawButton = new Button("Draw")
    private val groupByColorButton = new Button("Group by color")
    configDrawButton()
    configGroupByColorButton()

    override def addHandsView(handsView: List[HandView]): Unit =
      handPanel.contents ++= handsView.map(_.handComponent)

    override def updateHandsView(handsView: List[HandView]): Unit =
      handPanel.contents.clear()
      addHandsView(handsView)
      frame.validate()

    handButtonPanel.contents += drawButton
    handButtonPanel.contents += groupByColorButton

    initMap()

    scrollPane.horizontalScrollBarPolicy = AsNeeded
    scrollPane.verticalScrollBarPolicy = Never
    southPanel.contents += scrollPane
    southPanel.contents += handButtonPanel
    panel.contents += mapView.component
    panel.contents += southPanel
    frame.repaint()

    private def initMap(): Unit =
      val topBarHeight = frame.peer.getInsets.top
      val textHeight = 10
      import CitiesLoader.given
      CitiesLoader(screenSize.width, screenSize.height - topBarHeight - textHeight).load()

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

    export frame.{open, close}
    export mapView.addRoute
