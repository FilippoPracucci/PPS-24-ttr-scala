package view.player

import scala.swing.*

/** The representation of a player. It's possible to update its text. */
trait PlayerView:
  /** The [[Component]] of the player view.
    *
    * @return
    *   the component of the player representation.
    */
  def component: Component

  /** Add a [[Component]] to the inner panel of the view.
    *
    * @param component
    *   the component to add to the inner panel.
    */
  def addComponentToInnerPanel(component: Component): Unit

  /** Update component text.
    *
    * @param text
    *   the text to update the component with.
    */
  def updateComponentText(text: String): Unit

/** A basic representation of a player following the [[PlayerView]] trait.
  *
  * @param title
  *   the title of the component.
  */
protected class BasicPlayerView(title: String) extends PlayerView:
  import javax.swing.BorderFactory
  import java.awt.Color.*

  private val textComponent: TextPane = new TextPane():
    this.initComponent()
  private val panel = new BoxPanel(Orientation.Vertical):
    contents += textComponent
  private val _component: BoxPanel = new BoxPanel(Orientation.Vertical):
    this.initComponent()

  override def component: Component = _component

  override def addComponentToInnerPanel(component: Component): Unit =
    panel.contents += component

  override def updateComponentText(text: String): Unit =
    textComponent.text = text

  extension (component: TextPane)
    private def initComponent(): Unit =
      import scala.swing.Font.Style
      component.editable = false
      component.focusable = false
      component.font = Font("Coursier", Style.Plain, 16)

  extension (component: BoxPanel)
    private def initComponent(): Unit =
      component.contents += new Label(title):
        xLayoutAlignment = 0.5
        font = font.deriveFont(15f)
      component.contents += panel
      component.background = WHITE
      component.border = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BLACK, 1, true),
        Swing.EmptyBorder(10)
      )
