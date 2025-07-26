package view.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CitiesLoaderTest extends AnyFlatSpec with Matchers:
  private val MapWidth = 900
  private val MapHeight = 600
  private val IllegalParam = -10

  private object DummyMapView extends MapView:
    import scala.swing.Component
    import view.GameView.City
    import MapView.Color
    override def component: Component = new Component {}
    override def addCity(city: City, x: Double, y: Double, width: Double, height: Double): Unit = ()
    override def addRoute(connectedCities: (City, City), length: Int, color: Color): Unit = ()
    override def updateRoute(connectedCities: (City, City), color: Color): Unit = ()

  private given MapView = DummyMapView

  "A CitiesLoader" should "fail to initialize if the given map width or height aren't positive numbers" in:
    an[IllegalArgumentException] should be thrownBy CitiesLoader(IllegalParam, MapHeight)()
    an[IllegalArgumentException] should be thrownBy CitiesLoader(MapWidth, IllegalParam)()
    an[IllegalArgumentException] should be thrownBy CitiesLoader(IllegalParam, IllegalParam)()

  it should "fail to load the cities if the given config file doesn't exist" in:
    val citiesLoader = CitiesLoader(MapWidth, MapHeight)("nonExistentFile")
    an[IllegalStateException] should be thrownBy citiesLoader.load()
