package controller

import model.utils.{Color, PlayerColor}
import java.awt.Color as ViewColor

/** Object that provides conversion methods for the colors of the game map (from model to view). */
object MapColorConverter:

  /** Returns the view color of the route color.
    *
    * @return
    *   the view color
    */
  extension (routeColor: Color)
    def viewColor: ViewColor = routeColor match
      case Color.BLACK => ViewColor.BLACK
      case Color.WHITE => ViewColor.WHITE
      case Color.RED => ViewColor.RED
      case Color.BLUE => ViewColor.BLUE
      case Color.ORANGE => ViewColor.ORANGE
      case Color.YELLOW => ViewColor.YELLOW
      case Color.GREEN => ViewColor.GREEN.darker()
      case Color.PINK => ViewColor.PINK

  /** Returns the view color of the player color.
    *
    * @return
    *   the view color
    */
  extension (playerColor: PlayerColor)
    def viewColor: ViewColor = playerColor match
      case PlayerColor.BLUE => ViewColor.BLUE
      case PlayerColor.RED => ViewColor.RED
      case PlayerColor.YELLOW => ViewColor.YELLOW
      case PlayerColor.GREEN => ViewColor.GREEN.darker()
