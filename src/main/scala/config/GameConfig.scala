package config

import model.map.GameMap.AlreadyClaimedRoute
import model.player.Player.{NotEnoughCards, NotEnoughCardsInTheDeck, NotEnoughTrains}
import model.utils.GameError

case object GameConfig:
  val NumberTrainCars = 45
  val NumCardsPerColor = 12
  val HandInitialSize = 4
  val StandardNumberOfCardsToDraw = 2
  val TrainsToStartLastRound = 2
  val PointsPerRouteLength: Int => Int =
    case 1 => 1
    case 2 => 2
    case 3 => 4
    case 4 => 7
    case 6 => 15
    case 8 => 21
    case _ => throw new NoSuchElementException("Unexpected routes length.")
  val ErrorDescription: GameError => String =
    case AlreadyClaimedRoute => "Can't claim a route that has already been claimed!"
    case NotEnoughTrains => "Not enough trains to claim this route!"
    case NotEnoughCards => "Not enough cards to claim this route!"
    case NotEnoughCardsInTheDeck => "Not enough cards in the deck! It's possible only to claim a route!"
    case _ => throw new IllegalStateException("Unexpected error")
  val RoutesPath = "routes"
  val CitiesPath = "cities"
  val ObjectivesPath = "objectives"
  val RulesDescription = "RULES HERE!" // TODO

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

  case object FontConfig:
    import scala.swing.Font
    import scala.swing.Font.Style
    val ObjectiveFont: Font = Font("Coursier", Style.BoldItalic, 16)
    val FinalRankingFont: Font = Font("Courier", Font.Style.Bold, 18)
    val PlayerFont: Font = Font("Coursier", Style.Plain, 16)
