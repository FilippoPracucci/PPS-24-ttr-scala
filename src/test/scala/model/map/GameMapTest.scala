package model.map

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameMapBasicTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:
  import config.Loader
  import model.utils.Color
  import model.utils.PlayerColor

  private val ConnectedCities = ("Roma", "Venezia")
  private val PlayerId = PlayerColor.GREEN
  private val NotConnectedCities = ("Roma", "Bologna")
  private val route1 = Route((City(ConnectedCities._1), City(ConnectedCities._2)), 2, Route.SpecificColor(Color.BLACK))
  private val route2 = Route((City("Roma"), City("Brindisi")), 3, Route.SpecificColor(Color.WHITE))
  private val route3 = Route((City("Roma"), City("Palermo")), 4, Route.SpecificColor(Color.RED))
  private val routes = Set(route1, route2, route3)

  private val loader: Loader[Set[Route]] = () => routes
  private var gameMap: GameMap = GameMap()(using loader)

  override def beforeEach(): Unit = gameMap = GameMap()(using loader)

  "A GameMap" should "return the correct set of routes" in:
    gameMap.routes should be(routes)

  it should "return the correct requested route if present" in:
    gameMap.getRoute(ConnectedCities) should be(Some(route1))
    gameMap.getRoute(ConnectedCities.swap) should be(Some(route1))
    gameMap.getRoute(NotConnectedCities) should be(None)

  it should "claim the requested route" in:
    gameMap.claimRoute(ConnectedCities, PlayerId)
    gameMap.getPlayerClaimingRoute(ConnectedCities) should be(Right(Some(PlayerId)))

  it should "fail to claim a non-existent or already claimed route" in:
    gameMap.claimRoute(NotConnectedCities, PlayerId) should be(Left(GameMap.NonExistentRoute))
    gameMap.claimRoute(ConnectedCities, PlayerId)
    gameMap.claimRoute(ConnectedCities, PlayerId) should be(Left(GameMap.AlreadyClaimedRoute))

  it should "correctly handle the cases in which the player claims a non-existent or unclaimed route" in:
    gameMap.getPlayerClaimingRoute(NotConnectedCities) should be(Left(GameMap.NonExistentRoute))
    gameMap.getPlayerClaimingRoute(ConnectedCities) should be(Right(None))
end GameMapBasicTest

class GameMapInitTest extends AnyFlatSpec with Matchers:
  "A GameMap" should "correctly initialize using the default loader" in:
    import GameMap.defaultRoutesLoader
    lazy val gameMap = GameMap()
    noException should be thrownBy gameMap
    gameMap.routes should not be empty

  it should "fail its initialization when created from a non-existent file" in:
    an[IllegalStateException] should be thrownBy GameMap()(using RoutesLoader("nonExistentFile"))
