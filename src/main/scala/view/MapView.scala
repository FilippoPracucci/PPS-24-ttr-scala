package view

trait GameView:
  // TODO
  def open(): Unit
  def close(): Unit
  def addRoute(connectedCities: (String, String), length: Int): Unit

object GameView:
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

import scala.swing._
trait MapView:
  // TODO
  def component: Component
  def addCity(name: String, x: Double, y: Double, width: Double, height: Double): Unit
  def addRoute(connectedCities: (String, String), length: Int): Unit

object MapView:
  def apply(): MapView = MapViewImpl

  private object MapViewImpl extends MapView:
    import com.mxgraph.view.mxGraph
    import com.mxgraph.swing.mxGraphComponent

    private val graph = new mxGraph()
    private val parent = graph.getDefaultParent
    private val graphComponent = new mxGraphComponent(graph)
    graphComponent.setEnabled(false)
    override val component: Component = Component.wrap(graphComponent)

    private type City = String
    private type Vertex = AnyRef // type of vertices/edges in JGraphX library. It should be com.mxgraph.model.mxCell
    private var vertices: Map[City, Vertex] = Map()

    setDefaultStyle()

    private def setDefaultStyle(): Unit =
      import com.mxgraph.util.mxConstants
      val vertexStyle = graph.getStylesheet.getDefaultVertexStyle
      vertexStyle.put(mxConstants.STYLE_FONTSIZE, 14)
      vertexStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD)
      vertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000")
      graph.getStylesheet.setDefaultVertexStyle(vertexStyle)
      val edgeStyle = graph.getStylesheet.getDefaultEdgeStyle
      edgeStyle.put(mxConstants.STYLE_FONTSIZE, 18)
      edgeStyle.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD)
      edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000")
      edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE)
      edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000")
      edgeStyle.put(mxConstants.STYLE_DASHED, true)
      graph.getStylesheet.setDefaultEdgeStyle(edgeStyle)

    override def addCity(name: String, x: Double, y: Double, width: Double, height: Double): Unit =
      graph.getModel.beginUpdate()
      try
        val vertex = graph.insertVertex(parent, name, name, x, y, width, height)
        vertices = vertices.updated(name, vertex)
      finally
        graph.getModel.endUpdate()

    override def addRoute(connectedCities: (City, City), length: Int): Unit =
      graph.getModel.beginUpdate()
      try
        val edge = graph.insertEdge(parent, null, length, vertices(connectedCities._1), vertices(connectedCities._2))
      finally
        graph.getModel.endUpdate()

    import java.awt.event.MouseAdapter
    import java.awt.event.MouseEvent
    graphComponent.getGraphControl.addMouseListener(new MouseAdapter {
      override def mouseReleased(e: MouseEvent): Unit =
        val cell = graphComponent.getCellAt(e.getX, e.getY)
        if (cell != null && graph.getModel.isEdge(cell))
          println(s"Edge clicked: $cell")
          graph.getModel.beginUpdate()
          try
            graph.setCellStyle("strokeColor=red;dashed=0", Array(cell))
          finally
            graph.getModel.endUpdate()

          import com.mxgraph.model.mxCell
          val city1 = graph.getModel.getTerminal(cell, true).asInstanceOf[mxCell]
          val city2 = graph.getModel.getTerminal(cell, false).asInstanceOf[mxCell]
          Dialog.showMessage(component, s"Route between ${city1.getId} and ${city2.getId} clicked", title = "Info")
    })

@main def launchView(): Unit =
  val gameView = GameView()
  gameView.addRoute(("Roma", "Palermo"), 4)
  gameView.addRoute(("Roma", "Venezia"), 2)
  gameView.addRoute(("Roma", "Brindisi"), 2)
  gameView.addRoute(("Palermo", "Brindisi"), 3)
  gameView.open()
