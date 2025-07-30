package model.objective

import model.objective.{ObjectiveChecker, ObjectiveWithCompletion}
import model.player.Player
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ObjectiveCheckerTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:
  import model.map.GameMap
  import GameMap.given
  import model.utils.PlayerColor

  private val player = Player(PlayerColor.GREEN, objective = ObjectiveWithCompletion(("Paris", "Venezia"), 10))
  private var gameMap = GameMap()
  private var objectiveChecker = ObjectiveChecker(gameMap)

  override def beforeEach(): Unit =
    gameMap = GameMap()
    objectiveChecker = ObjectiveChecker(gameMap)

  "An ObjectiveChecker" should "correctly check whether an objective has been completed" in:
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Paris", "Zurich"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Zurich", "Venezia"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(true)

  it should "correctly check whether an objective has been completed via a non-direct path" in:
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Paris", "Bruxelles"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Bruxelles", "Frankfurt"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Frankfurt", "Munchen"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Munchen", "Venezia"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(true)

  it should "correctly check whether an objective has been completed via a cyclic path" in:
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Paris", "Bruxelles"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Bruxelles", "Frankfurt"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Frankfurt", "Paris"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Frankfurt", "Munchen"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(false)
    gameMap.claimRoute(("Munchen", "Venezia"), player.id)
    objectiveChecker.check(player.objective, player.id) should be(true)
