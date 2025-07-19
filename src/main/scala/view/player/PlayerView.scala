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

  private val _component: TextPane = new TextPane():
    this.initComponent()

  override def component: Component = _component

  override def updateComponentText(text: String): Unit =
    _component.text = title + "\n\n" + text

  extension (component: TextPane)
    private def initComponent(): Unit =
      import scala.swing.Font.Style
      import javax.swing.BorderFactory
      component.editable = false
      component.focusable = false
      component.font = Font("Coursier", Style.Plain, 16)
      component.border = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(java.awt.Color.BLACK, 1, true),
        Swing.EmptyBorder(10)
      )
