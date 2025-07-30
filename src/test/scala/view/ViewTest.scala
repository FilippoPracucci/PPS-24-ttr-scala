package view

import scala.swing.*

/** Trait that represents the main frame that will be used by every view test. */
trait ViewTest:
  lazy val frame: MainFrame = new MainFrame():
    visible = true
    pack()
    centerOnScreen()

  import javax.swing.WindowConstants.EXIT_ON_CLOSE
  frame.peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

  /** Set the contents of the frame.
    *
    * @param component
    *   the component that will be set as frame content.
    */
  def setFrameContents(component: Component): Unit = frame.contents = component

  /** Update the view, so the frame. */
  def updateView(): Unit =
    frame.validate()
    frame.pack()
