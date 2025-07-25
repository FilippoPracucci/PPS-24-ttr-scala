package view.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CitiesLoaderTest extends AnyFlatSpec with Matchers:
  private val mapWidth = 900
  private val mapHeight = 600
  private val illegalParam = -10

  import MapView.{City, Color}
  private object DummyMapView extends MapView:
    import scala.swing.Component
    override def component: Component = new Component {}
    override def addCity(city: City, x: Double, y: Double, width: Double, height: Double): Unit = ()
    override def addRoute(connectedCities: (City, City), length: Int, color: Color): Unit = ()
    override def updateRoute(connectedCities: (City, City), color: Color): Unit = ()

  private given MapView = DummyMapView

  "A CitiesLoader" should "fail to initialize if the given map width or height aren't positive numbers" in:
    an[IllegalArgumentException] should be thrownBy CitiesLoader(illegalParam, mapHeight)()
    an[IllegalArgumentException] should be thrownBy CitiesLoader(mapWidth, illegalParam)()
    an[IllegalArgumentException] should be thrownBy CitiesLoader(illegalParam, illegalParam)()

  it should "fail to load the cities if the given config file doesn't exist" in:
    val citiesLoader = CitiesLoader(mapWidth, mapHeight)("nonExistentFile")
    an[IllegalStateException] should be thrownBy citiesLoader.load()
