package view.map

import com.mxgraph.swing.mxGraphComponent

/** Trait that represents a style manager for a graph component. It provides a way to set the style of graph components.
 */
trait GraphStyleManager:
  /** Sets the style of a graph component.
   */
  extension (graphComponent: mxGraphComponent) def setStyle(): Unit

object GraphStyleManager:
  /** Creates a `GraphStyleManager`.
   *
   * @return the created `GraphStyleManager`
   */
  def apply(): GraphStyleManager = MapStyleManager

  private object MapStyleManager extends GraphStyleManager:
    extension (graphComponent: mxGraphComponent) override def setStyle(): Unit =
      import com.mxgraph.util.mxConstants
      val BlackColor = "#000000"
      val graph = graphComponent.getGraph

      def setGraphStyle(): Unit =
        import scala.swing.Swing
        import javax.swing.BorderFactory
        import java.awt.Color.*
        val BackgroundColor = LIGHT_GRAY
        val BorderColor = BLACK
        val BorderThickness = 1
        graphComponent.getViewport.setOpaque(true)
        graphComponent.getViewport.setBackground(BackgroundColor)
        graphComponent.setBorder(
          Swing.CompoundBorder(Swing.EmptyBorder(BorderThickness),
            BorderFactory.createLineBorder(BorderColor, BorderThickness, true))
        )

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
        val Dashed = true
        val EdgeWidth = 2
        val edgeStyle = graph.getStylesheet.getDefaultEdgeStyle
        edgeStyle.put(mxConstants.STYLE_FONTSIZE, FontSize)
        edgeStyle.put(mxConstants.STYLE_FONTSTYLE, FontStyle)
        edgeStyle.put(mxConstants.STYLE_FONTCOLOR, FontColor)
        edgeStyle.put(mxConstants.STYLE_ENDARROW, EndArrow)
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, EdgeColor)
        edgeStyle.put(mxConstants.STYLE_DASHED, Dashed)
        edgeStyle.put(mxConstants.STYLE_STROKEWIDTH, EdgeWidth)
        graph.getStylesheet.setDefaultEdgeStyle(edgeStyle)

      setGraphStyle()
      setVertexStyle()
      setEdgeStyle()
