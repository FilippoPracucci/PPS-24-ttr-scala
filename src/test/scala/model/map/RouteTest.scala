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

  private val ConnectedCities = (City("Roma"), City("Venezia"))
  private val BlackColor = BLACK
  private val mechanic: Route.Mechanic = Route.SpecificColor(BlackColor)

  "A Route" should "be initialized correctly" in:
    val length = 2
    val route = Route(ConnectedCities, length, mechanic)
    route.connectedCities should be(ConnectedCities)
    route.length should be(length)
    route.mechanic should be(mechanic)

  it should "fail to initialize if the specified length isn't a positive number" in:
    val illegalLength = -5
    an[IllegalArgumentException] should be thrownBy Route(ConnectedCities, illegalLength, mechanic)

  "A Route.SpecificColor" should "be able to be matched and deconstructed" in:
    mechanic match
      case Route.SpecificColor(color) => color should be(BlackColor)
      case _ => fail("Should have matched Route.SpecificColor(color)")
