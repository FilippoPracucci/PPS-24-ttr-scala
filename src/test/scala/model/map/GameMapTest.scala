package model.map

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CityTest extends AnyFlatSpec with Matchers:
  "A City" should "be initialized correctly" in:
    val name = "Roma"
    val city = City(name)
    city.name should be(name)

class RouteTest extends AnyFlatSpec with Matchers:
  "A Route" should "be initialized correctly" in:
    val connectedCities = (City("Roma"), City("Venezia"))
    val length = 2
    val mechanic = Route.SpecificColor("Black") // TODO to be integrated with utils.Color
    val route = Route(connectedCities, length, mechanic)
    route.connectedCities should be(connectedCities)
    route.length should be(length)
    route.mechanic should be(mechanic)

class GameMapTest extends AnyFlatSpec with Matchers with BeforeAndAfterEach:
  private val connectedCities = (City("Roma"), City("Venezia"))
  private val route1 = Route(connectedCities, 2,
    Route.SpecificColor("Black")) // TODO to be integrated with utils.Color
  private val route2 = Route((City("Roma"), City("Brindisi")), 3,
    Route.SpecificColor("White")) // TODO to be integrated with utils.Color
  private val route3 = Route((City("Roma"), City("Palermo")), 4,
    Route.SpecificColor("Red")) // TODO to be integrated with utils.Color
  private val notConnectedCities = (City("Roma"), City("Bologna"))
  private val player: PlayerId = "Green" // TODO to be integrated in the future

  private var gameMap: GameMap = GameMap(Set())

  override def beforeEach(): Unit = gameMap = GameMap(Set(route1, route2, route3))

  "A GameMap" should "be able to return the correct requested route" in:
    gameMap.getRoute(connectedCities) should be(Some(route1))
    gameMap.getRoute(connectedCities.swap) should be(Some(route1))
    gameMap.getRoute(notConnectedCities) should be(None)

  it should "claim the requested route" in:
    gameMap.claimRoute(connectedCities, player)
    gameMap.getPlayerClaimingRoute(connectedCities) should be(Some(player))

  it should "fail to claim a non-existent or already claimed route" in:
    an[IllegalArgumentException] should be thrownBy gameMap.claimRoute(notConnectedCities, player)
    gameMap.claimRoute(connectedCities, player)
    an[IllegalArgumentException] should be thrownBy gameMap.claimRoute(connectedCities, player)

  it should "return None when trying to get the player claiming a non-existent or unclaimed route" in:
    gameMap.getPlayerClaimingRoute(notConnectedCities) should be(None)
    gameMap.getPlayerClaimingRoute(connectedCities) should be(None)
