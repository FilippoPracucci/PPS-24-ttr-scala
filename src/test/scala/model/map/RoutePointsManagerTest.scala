package model.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RoutePointsManagerTest extends AnyFlatSpec with Matchers:
  import model.utils.Color

  private val connectedCities = (City("Roma"), City("Venezia"))
  private val color = Route.SpecificColor(Color.BLACK)

  private val routePointsManager = RoutePointsManager()
  import routePointsManager.points

  "A RoutePointsManager" should "be able to return the points of a route" in:
    val managedLength = 2
    val route = Route(connectedCities, managedLength, color)
    route.points should be > 0

  it should "fail to return the points of an unmanaged route" in:
    val unmanagedLength = 10
    val route = Route(connectedCities, unmanagedLength, color)
    a[NoSuchElementException] should be thrownBy route.points
