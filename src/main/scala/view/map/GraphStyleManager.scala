package view.map

import com.mxgraph.swing.mxGraphComponent
import MapView.Color

/** Trait that represents a style manager for a graph component. It provides a way to set the style of graph components.
  */
trait GraphStyleManager:
  /** Sets the style of a graph component. */
  extension (graphComponent: mxGraphComponent) def setDefaultStyle(): Unit

  /** Returns the custom style of an edge.
    *
    * @param color
    *   the color the edge will have
    * @param dashed
    *   indicates whether the edge will be dashed or not
    * @return
    *   the edge style
    */
  def edgeStyle(color: Color, dashed: Boolean): String

object GraphStyleManager:
  /** Creates a [[GraphStyleManager]].
    *
    * @return
    *   the created [[GraphStyleManager]]
    */
  def apply(): GraphStyleManager = MapStyleManager

  private object MapStyleManager extends GraphStyleManager:
    import com.mxgraph.util.mxConstants

    extension (graphComponent: mxGraphComponent)
      override def setDefaultStyle(): Unit =
        val BlackColor = "#000000"
        val graph = graphComponent.getGraph

        def setGraphStyle(): Unit =
          import config.GameViewConfig.BorderConfig.*
          import config.GameViewConfig.ColorConfig.MapBackgroundColor
          val BorderThickness = 1
          graphComponent.getViewport.setOpaque(true)
          graphComponent.getViewport.setBackground(MapBackgroundColor)
          graphComponent.setBorder(CompoundBorder(EmptyBorder(BorderThickness), LineBorder(BorderThickness)))

        def setVertexStyle(): Unit =
          val FontSize = 14
          val FontStyle = mxConstants.FONT_BOLD
          val FontColor = BlackColor
          val vertexStyle = graph.getStylesheet.getDefaultVertexStyle
          vertexStyle.put(mxConstants.STYLE_FONTSIZE, FontSize)
          vertexStyle.put(mxConstants.STYLE_FONTSTYLE, FontStyle)
          vertexStyle.put(mxConstants.STYLE_FONTCOLOR, FontColor)
          graph.getStylesheet.setDefaultVertexStyle(vertexStyle)

        def setEdgeStyle(): Unit =
          val FontSize = 18
          val FontStyle = mxConstants.FONT_BOLD
          val FontColor = BlackColor
          val EndArrow = mxConstants.NONE
          val EdgeColor = BlackColor
          val EdgeWidth = 2
          val edgeStyle = graph.getStylesheet.getDefaultEdgeStyle
          edgeStyle.put(mxConstants.STYLE_FONTSIZE, FontSize)
          edgeStyle.put(mxConstants.STYLE_FONTSTYLE, FontStyle)
          edgeStyle.put(mxConstants.STYLE_FONTCOLOR, FontColor)
          edgeStyle.put(mxConstants.STYLE_ENDARROW, EndArrow)
          edgeStyle.put(mxConstants.STYLE_STROKECOLOR, EdgeColor)
          edgeStyle.put(mxConstants.STYLE_STROKEWIDTH, EdgeWidth)
          graph.getStylesheet.setDefaultEdgeStyle(edgeStyle)

        setGraphStyle()
        setVertexStyle()
        setEdgeStyle()

    override def edgeStyle(color: Color, dashed: Boolean): String =
      extension (color: Color)
        private def toHex: String = f"#${color.getRed}%02X${color.getGreen}%02X${color.getBlue}%02X"
      s"${mxConstants.STYLE_STROKECOLOR}=${color.toHex};${mxConstants.STYLE_DASHED}=$dashed"
