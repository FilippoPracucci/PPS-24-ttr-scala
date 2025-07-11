package model.map

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameMapBasicTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:
  import model.utils.Color
  import model.utils.PlayerColor

  private val connectedCities = ("Roma", "Venezia")
  private val route1 = Route((City(connectedCities._1), City(connectedCities._2)), 2, Route.SpecificColor(Color.BLACK))
  private val route2 = Route((City("Roma"), City("Brindisi")), 3, Route.SpecificColor(Color.WHITE))
  private val route3 = Route((City("Roma"), City("Palermo")), 4, Route.SpecificColor(Color.RED))
  private val notConnectedCities = ("Roma", "Bologna")
  private val player = PlayerColor.GREEN

  private var gameMap: GameMap = GameMap(Set())

  override def beforeEach(): Unit = gameMap = GameMap(Set(route1, route2, route3))

  "A GameMap" should "return the correct set of routes" in:
    gameMap.routes should be(Set(route1, route2, route3))

  it should "return the correct requested route if present" in:
    gameMap.getRoute(connectedCities) should be(Some(route1))
    gameMap.getRoute(connectedCities.swap) should be(Some(route1))
    gameMap.getRoute(notConnectedCities) should be(None)

  it should "claim the requested route" in:
    gameMap.claimRoute(connectedCities, player)
    gameMap.getPlayerClaimingRoute(connectedCities) should be(Right(Some(player)))

  it should "fail to claim a non-existent or already claimed route" in:
    gameMap.claimRoute(notConnectedCities, player) should be(Left(GameMap.NonExistentRoute))
    gameMap.claimRoute(connectedCities, player)
    gameMap.claimRoute(connectedCities, player) should be(Left(GameMap.AlreadyClaimedRoute))

  it should "correctly handle the cases in which the player claims a non-existent or unclaimed route" in:
    gameMap.getPlayerClaimingRoute(notConnectedCities) should be(Left(GameMap.NonExistentRoute))
    gameMap.getPlayerClaimingRoute(connectedCities) should be(Right(None))
end GameMapBasicTest

class GameMapInitFromFileTest extends AnyFlatSpec with Matchers:
  "A GameMap" should "not fail its initialization when created from file" in:
    import GameMap.given
    noException should be thrownBy GameMap()

  it should "fail its initialization when created from a non-existent file" in:
    an[IllegalStateException] should be thrownBy GameMap()(using "nonExistentFile.json")
