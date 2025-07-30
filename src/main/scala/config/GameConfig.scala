package config

import model.map.GameMap.AlreadyClaimedRoute
import model.player.Player.{NotEnoughCards, NotEnoughCardsInTheDeck, NotEnoughTrains}
import model.utils.GameError

/** Object that contains constant values following the rules of the game. */
case object GameConfig:
  val NumberTrainCars = 45
  val NumCardsPerColor = 12
  val HandInitialSize = 4
  val StandardNumberOfCardsToDraw = 2
  val TrainsToStartLastRound = 2
  val InitialScore = 0
  val PointsPerRouteLength: Int => Int =
    case 1 => 1
    case 2 => 2
    case 3 => 4
    case 4 => 7
    case 6 => 15
    case 8 => 21
    case _ => throw new NoSuchElementException("Unexpected route length.")
  val ErrorDescription: GameError => String =
    case AlreadyClaimedRoute => "Can't claim a route that has already been claimed!"
    case NotEnoughTrains => "Not enough trains to claim this route!"
    case NotEnoughCards => "Not enough cards to claim this route!"
    case NotEnoughCardsInTheDeck => "Not enough cards in the deck! It's possible only to claim a route!"
    case _ => throw new IllegalStateException("Unexpected error")
  val RoutesPath = "routes"
  val CitiesPath = "cities"
  val ObjectivesPath = "objectives"
  val RulesDescription: String =
    s"""
      |Ticket to Ride is a board game in which four players compete to obtain points, that are gained by claiming routes
      |and completing objectives. During a turn a player can draw 2 cards from the deck (pressing the "Draw" button) or
      |claim a route (clicking the route on the map). After clicking on a route, a number of cards of the route's color
      |corresponding to the route's length, will be automatically discarded from the player's hand. Each player has 45
      |train cars, which are placed to claim routes and when a player remains with less than 3 train cars starts the
      |last round. At the end of the game the player with the most points wins.
      |
      |Map legend:
      |- Dashed line of color C => unclaimed route, with C = color of the route
      |- Solid line of color C => claimed route, with C = color of the player claiming the route
      |
      |Route scoring table (route length => points gained):
      |1 => ${PointsPerRouteLength(1)}
      |2 => ${PointsPerRouteLength(2)}
      |3 => ${PointsPerRouteLength(3)}
      |4 => ${PointsPerRouteLength(4)}
      |6 => ${PointsPerRouteLength(6)}
      |8 => ${PointsPerRouteLength(8)}
      |""".stripMargin

/** Object that contains constant values used in the game view. */
case object GameViewConfig:
  val FrameTitle = "Ticket to Ride"
  val DrawButtonText = "Draw"
  val RulesButtonText = "Rules"
  val PlayerInfoTitle = "PLAYER INFO"
  val ObjectiveTitle = "OBJECTIVE"
  val PlayerScoresTitle = "PLAYER SCORES"
  val StartLastRoundDescription = "Start of the final round, so last turn for each player!"
  val StartLastRoundTitle = "Last round"
  val SeeFinalRanking = "See the final ranking"
  val Close = "Close"
  val EndGameDescription = "The game is over!"
  val EndGameTitle = "End game"
  val ReportErrorTitle = "Error"
  val FinalRankingTitle = "PLAYERS RANKING"
  val ObjectiveDescription: (String, String, Int) => String =
    (city1: String, city2: String, points: Int) => f"Connect the cities $city1 and $city2\n\nPoints: $points"
  val PlayerInfoDescription: (String, Int) => String =
    (player: String, trains: Int) => f"Player $player has $trains train cars left"
  val ObjectiveCompletedDescription: Int => String =
    points => s"You have completed your objective! You gain $points points!"
  val ObjectiveCompletedTitle = "Objective completed"
  val RulesTitle = "Rules of the game"

  case object BorderConfig:
    import scala.swing.*
    import javax.swing.border.Border
    import javax.swing.BorderFactory
    import ColorConfig.BorderColor
    val EmptyBorder: Int => Border = Swing.EmptyBorder
    val LineBorder: Int => Border = BorderFactory.createLineBorder(BorderColor, _, true)
    val CompoundBorder: (Border, Border) => Border = Swing.CompoundBorder

  case object ColorConfig:
    import java.awt.Color
    import Color.*
    val BackgroundColor: Color = WHITE
    val MapBackgroundColor: Color = LIGHT_GRAY
    val BorderColor: Color = BLACK

  case object FontConfig:
    import scala.swing.Font
    import scala.swing.Font.Style
    val ObjectiveFont: Font = Font("Coursier", Style.BoldItalic, 16)
    val FinalRankingFont: Font = Font("Courier", Font.Style.Bold, 18)
    val PlayerFont: Font = Font("Coursier", Style.Plain, 16)
    val DerivedFont: Font => Font = _.deriveFont(16f)
