package model.map

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

class MapTest extends AnyFlatSpec with Matchers:
  private val connectedCities = (City("Roma"), City("Venezia"))
  private val route1 = Route(connectedCities, 2,
    Route.SpecificColor("Black")) // TODO to be integrated with utils.Color
  private val route2 = Route((City("Roma"), City("Brindisi")), 3,
    Route.SpecificColor("White")) // TODO to be integrated with utils.Color
  private val route3 = Route((City("Roma"), City("Palermo")), 4,
    Route.SpecificColor("Red")) // TODO to be integrated with utils.Color
  private val notConnectedCities = (City("Roma"), City("Bologna"))

  private val map = Map(Set(route1, route2, route3))

  "A Map" should "be able to return the correct requested route" in:
    map.getRoute(connectedCities) should be(Some(route1))
    map.getRoute(connectedCities.swap) should be(Some(route1))
    map.getRoute(notConnectedCities) should be(None)
