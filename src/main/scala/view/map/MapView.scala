package view.map

import scala.swing.*
import javax.swing.JComponent
import view.GameView.City

/** Trait that represents the view of the map, using scala.swing.
  */
trait MapView:
  import MapView.Color

  /** Returns the `scala.swing.Component` of the map view.
    * @return
    *   the `scala.swing.Component` of the map view.
    */
  def component: Component

  /** Adds a new city in the map view.
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
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    * @param length
    *   the length of the route
    * @param color
    *   the color of the route expressed as the name of the color in lowercase
    */
  def addRoute(connectedCities: (City, City), length: Int, color: Color): Unit

  /** Updates the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names
    * @param color
    *   the new color of the route, expressed as the name of the color in lowercase
    */
  def updateRoute(connectedCities: (City, City), color: Color): Unit

object MapView:
  import controller.GameController

  /** Type alias that represents the color as String by its name in lowercase. // TODO
    */
  type Color = String

  /** Returns the singleton instance of `MapView`.
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
    private val parent = graph.getDefaultParent
    private val graphComponent = new mxGraphComponent(graph)
    override val component: Component = Component.wrap(graphComponent)

    private type Vertex = mxCell // type of vertices/edges in JGraphX library
    private var vertices: Map[City, Vertex] = Map()

    initView()

    private def initView(): Unit =
      def setDefaultStyle(): Unit =
        val graphStyleManager = GraphStyleManager()
        import graphStyleManager.setStyle
        graphComponent.setStyle()

      def onMouseReleased(handler: MouseReleased => Unit): Unit =
        val graphControl = new Component { override lazy val peer: JComponent = graphComponent.getGraphControl }
        graphControl.listenTo(graphControl.mouse.clicks)
        graphControl.reactions += { case e: MouseReleased => handler(e) }

      setDefaultStyle()
      onMouseReleased(e =>
        Option(graphComponent.getCellAt(e.point.x, e.point.y))
          .map(_.asInstanceOf[mxCell])
          .filter(graph.getModel.isEdge)
          .map(edge =>
            val city1 = graph.getModel.getTerminal(edge, true).asInstanceOf[mxCell]
            val city2 = graph.getModel.getTerminal(edge, false).asInstanceOf[mxCell]
            (city1.getId, city2.getId)
          )
          .foreach(GameController().claimRoute)
      )
      graphComponent.setEnabled(false)

    private def changeGraph(change: => Unit): Unit =
      graph.getModel.beginUpdate()
      try
        change
      finally
        graph.getModel.endUpdate()

    override def addCity(city: City, x: Double, y: Double, width: Double, height: Double): Unit =
      changeGraph {
        val vertex = graph.insertVertex(parent, city, city, x, y, width, height).asInstanceOf[mxCell]
        vertices = vertices.updated(city, vertex)
      }

    override def addRoute(connectedCities: (City, City), length: Int, color: Color): Unit =
      changeGraph {
        val edge = graph.insertEdge(parent, null, length, vertices(connectedCities._1), vertices(connectedCities._2))
        graph.setCellStyle(s"strokeColor=$color", Array(edge))
      }

    override def updateRoute(connectedCities: (City, City), color: Color): Unit =
      changeGraph {
        val edge = graph.getEdgesBetween(vertices(connectedCities._1), vertices(connectedCities._2))(0)
        graph.setCellStyle(s"strokeColor=$color;dashed=false", Array(edge))
      }
