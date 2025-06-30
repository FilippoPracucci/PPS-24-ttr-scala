package view

import javax.swing.JComponent
import scala.swing.*

/** Trait that represents the view of the map, using scala.swing.
  */
trait MapView:
  // TODO
  /** Returns the `scala.swing.Component` of the map view.
    * @return
    *   the `scala.swing.Component` of the map view.
    */
  def component: Component

  /** Adds a new city in the map view.
    * @param name
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
  def addCity(name: String, x: Double, y: Double, width: Double, height: Double): Unit

  /** Adds a new route in the map view.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names as String
    * @param length
    *   the length of the route
    */
  def addRoute(connectedCities: (String, String), length: Int): Unit

object MapView:
  /** Returns the singleton instance of `MapView`.
    * @return
    *   the globally shared `MapView` instance
    */
  def apply(): MapView = MapViewImpl

  private object MapViewImpl extends MapView:
    import com.mxgraph.view.mxGraph
    import com.mxgraph.swing.mxGraphComponent
    import com.mxgraph.model.mxCell
    // Note: The JGraphX library works with vertices and edges as Object (both input and output), although they are
    //       always mxCell. In an effort to maintain decent type checking, every vertex and edge is cast to mxCell.

    private val graph = new mxGraph()
    private val parent = graph.getDefaultParent
    private val graphComponent = new mxGraphComponent(graph)
    override val component: Component = Component.wrap(graphComponent)

    private type City = String
    private type Vertex = mxCell // type of vertices/edges in JGraphX library
    private var vertices: Map[City, Vertex] = Map()

    initView()

    private def initView(): Unit =
      graphComponent.setEnabled(false)
      StyleHelper.setDefaultStyle()
      val graphControl: Component =
        new Component { override lazy val peer: JComponent = graphComponent.getGraphControl }
      graphControl.listenTo(graphControl.mouse.clicks)
      graphControl.reactions += {
        case e: event.MouseReleased =>
          val cell = graphComponent.getCellAt(e.point.x, e.point.y).asInstanceOf[mxCell]
          if (cell != null && graph.getModel.isEdge(cell))
            val edge = cell
            println(s"Edge clicked: $edge")
            changeGraph(graph.setCellStyle("strokeColor=red;dashed=0", Array(edge)))
            val city1 = graph.getModel.getTerminal(edge, true).asInstanceOf[mxCell]
            val city2 = graph.getModel.getTerminal(edge, false).asInstanceOf[mxCell]
            Dialog.showMessage(component, s"Route between ${city1.getId} and ${city2.getId} clicked", title = "Info")
      }

    private object StyleHelper:
      import com.mxgraph.util.mxConstants
      private val blackColor = "#000000"

      def setDefaultStyle(): Unit =

        def setVertexStyle(): Unit =
          val fontSize = 14
          val fontStyle = mxConstants.FONT_BOLD
          val fontColor = blackColor
          val vertexStyle = graph.getStylesheet.getDefaultVertexStyle
          vertexStyle.put(mxConstants.STYLE_FONTSIZE, fontSize)
          vertexStyle.put(mxConstants.STYLE_FONTSTYLE, fontStyle)
          vertexStyle.put(mxConstants.STYLE_FONTCOLOR, fontColor)
          graph.getStylesheet.setDefaultVertexStyle(vertexStyle)

        def setEdgeStyle(): Unit =
          val fontSize = 18
          val fontStyle = mxConstants.FONT_BOLD
          val fontColor = blackColor
          val endArrow = mxConstants.NONE
          val edgeColor = blackColor
          val dashed = true
          val edgeStyle = graph.getStylesheet.getDefaultEdgeStyle
          edgeStyle.put(mxConstants.STYLE_FONTSIZE, fontSize)
          edgeStyle.put(mxConstants.STYLE_FONTSTYLE, fontStyle)
          edgeStyle.put(mxConstants.STYLE_FONTCOLOR, fontColor)
          edgeStyle.put(mxConstants.STYLE_ENDARROW, endArrow)
          edgeStyle.put(mxConstants.STYLE_STROKECOLOR, edgeColor)
          edgeStyle.put(mxConstants.STYLE_DASHED, dashed)
          graph.getStylesheet.setDefaultEdgeStyle(edgeStyle)

        setVertexStyle()
        setEdgeStyle()

    private def changeGraph(change: => Unit): Unit =
      graph.getModel.beginUpdate()
      try
        change
      finally
        graph.getModel.endUpdate()

    override def addCity(name: String, x: Double, y: Double, width: Double, height: Double): Unit =
      changeGraph {
        val vertex = graph.insertVertex(parent, name, name, x, y, width, height).asInstanceOf[mxCell]
        vertices = vertices.updated(name, vertex)
      }

    override def addRoute(connectedCities: (City, City), length: Int): Unit =
      changeGraph(graph.insertEdge(parent, null, length, vertices(connectedCities._1), vertices(connectedCities._2)))
