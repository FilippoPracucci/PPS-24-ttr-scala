package model.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CityTest extends AnyFlatSpec with Matchers:
  "A City" should "be initialized correctly" in:
    val name = "Roma"
    val city = City(name)
    city.name should be(name)

class RouteTest extends AnyFlatSpec with Matchers:
  import model.utils.Color
  import Color._

  "A Route" should "be initialized correctly" in:
    val connectedCities = (City("Roma"), City("Venezia"))
    val length = 2
    val mechanic = Route.SpecificColor(BLACK)
    val route = Route(connectedCities, length, mechanic)
    route.connectedCities should be(connectedCities)
    route.length should be(length)
    route.mechanic should be(mechanic)
