package view

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
    */
  def addRoute(connectedCities: (String, String), length: Int): Unit

object GameView:
  /** Returns the singleton instance of `GameView`.
    * @return
    *   the globally shared `GameView` instance
    */
  def apply(): GameView = GameViewSwing

  private object GameViewSwing extends GameView:
    import scala.swing._

    private val screenSize: Dimension = java.awt.Toolkit.getDefaultToolkit.getScreenSize
    private val mapView = MapView()
    private val frame: MainFrame = new MainFrame {
      title = "Game"
      contents = mapView.component
      preferredSize = screenSize
      peer.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
      resizable = false
    }

    initMap()

    private def initMap(): Unit =
      val topBarHeight = frame.peer.getInsets.top
      val textHeight = 10
      mapView.addCity("Venezia", 590, 440, 0, 0)
      mapView.addCity("Roma", 600, 520, 0, 0)
      mapView.addCity("Brindisi", 680, 540, 0, 0)
      mapView.addCity("Palermo", screenSize.width / 2, screenSize.height - topBarHeight - textHeight, 0, 0)

    export frame.{open, close}
    export mapView.addRoute
