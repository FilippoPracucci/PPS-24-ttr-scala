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

import scala.swing.Font
import scala.swing.Font.Style
given Font = Font("Coursier", Style.Plain, 16)

/** A basic representation of a player following the [[PlayerView]] trait. */
protected class BasicPlayerView(using componentFont: Font) extends PlayerView:

  private val _component: TextPane = new TextPane():
    this.initComponent(componentFont)

  override def component: Component = _component

  override def updateComponentText(text: String): Unit =
    _component.text = text

  extension (component: TextPane)
    private def initComponent(font: Font): Unit =
      component.editable = false
      component.focusable = false
      component.font = font
