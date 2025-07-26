package view.map

import scala.swing.*
import view.GameView.City

/** Trait that represents the view of the map, using scala.swing.
  */
trait MapView:
  import MapView.Color

  /** Returns the `scala.swing.Component` of the map view.
    *
    * @return
    *   the `scala.swing.Component` of the map view.
    */
  def component: Component

  /** Adds a new city in the map view.
    *
    * @param city
    *   the name of the city
    * @param x
    *   the x position of the city
    * @param y
    *   the y position of the city
    * @param width
    *   the width of the point indicating the city
    * @param height
    *   the height of the point indicating the city
    */
  def addCity(city: City, x: Double, y: Double, width: Double, height: Double): Unit

  /** Adds a new route in the map view.
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

object MapView:
  import controller.GameController

  /** Type alias that represents a color as java.awt.Color.
    */
  type Color = java.awt.Color

  /** Returns the singleton instance of `MapView`.
    *
    * @return
    *   the globally shared `MapView` instance
    */
  def apply(): MapView = MapViewImpl

  private object MapViewImpl extends MapView:
    import event.MouseReleased
    import com.mxgraph.view.mxGraph
    import com.mxgraph.swing.mxGraphComponent
    import com.mxgraph.model.mxCell
    // Note: The JGraphX library works with vertices and edges as Object (both input and output), although they are
    //       always mxCell. In an effort to maintain decent type checking, every vertex and edge is cast to mxCell.

    private val graph = new mxGraph()
    private val graphStyleManager = GraphStyleManager()
    private val parent = graph.getDefaultParent
    private val graphComponent = new mxGraphComponent(graph)
    override val component: Component = Component.wrap(graphComponent)

    private type Vertex = mxCell // type of vertices/edges in JGraphX library
    private var vertices: Map[City, Vertex] = Map()

    initView()

    private def initView(): Unit =
      import graphStyleManager.setDefaultStyle

      def onMouseReleased(handler: MouseReleased => Unit): Unit =
        import javax.swing.JComponent
        val graphControl = new Component { override lazy val peer: JComponent = graphComponent.getGraphControl }
        graphControl.listenTo(graphControl.mouse.clicks)
        graphControl.reactions += { case e: MouseReleased => handler(e) }

      graphComponent.setDefaultStyle()
      onMouseReleased(e =>
        Option(graphComponent.getCellAt(e.point.x, e.point.y))
          .map(_.asInstanceOf[mxCell])
          .filter(graph.getModel.isEdge)
          .map(edge =>
            val getCityName: Boolean => City = graph.getModel.getTerminal(edge, _).asInstanceOf[mxCell].getId
            (getCityName(true), getCityName(false))
          )
          .foreach(GameController().claimRoute)
      )
      graphComponent.setEnabled(false)

    private def changeGraph(change: => Unit): Unit =
      graph.getModel.beginUpdate()
      try change
      finally graph.getModel.endUpdate()

    override def addCity(city: City, x: Double, y: Double, width: Double, height: Double): Unit =
      changeGraph:
        vertices =
          vertices.updated(city, graph.insertVertex(parent, city, city, x, y, width, height).asInstanceOf[mxCell])

    override def addRoute(connectedCities: (City, City), length: Int, color: Color): Unit =
      val AutomaticId: String = null // note: null required by JGraphX library to indicate automatic id generation
      val Dashed = true
      changeGraph:
        graph.setCellStyle(
          graphStyleManager.edgeStyle(color, Dashed),
          Array(
            graph.insertEdge(parent, AutomaticId, length, vertices(connectedCities._1), vertices(connectedCities._2))
          )
        )

    override def updateRoute(connectedCities: (City, City), color: Color): Unit =
      val Dashed = false
      val FirstEdge = 0
      changeGraph:
        graph.setCellStyle(
          graphStyleManager.edgeStyle(color, Dashed),
          Array(graph.getEdgesBetween(vertices(connectedCities._1), vertices(connectedCities._2))(FirstEdge))
        )
