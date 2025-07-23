package view.map

import config.{LoaderFromFile, JsonReader}
import config.GameConfig.CitiesPath

/** Class that represents a loader of cities from a JSON file, loading them into the map view.
  * @param mapWidth
  *   the width (in pixel) of the map
  * @param mapHeight
  *   the height (in pixel) of the map
  * @param configFilePath
  *   the path of the JSON config file (starting from 'src/main/resources/', without file extension) containing
  *   information on the cities (default = "cities")
  */
class CitiesLoader(mapWidth: Int, mapHeight: Int)(override val configFilePath: String = CitiesPath)
    extends LoaderFromFile[Unit] with JsonReader:
  require(mapWidth > 0, "mapWidth must be positive")
  require(mapHeight > 0, "mapHeight must be positive")

  import upickle.default.*

  private val CityWidth = 0
  private val CityHeight = 0

  /** Class that represents the config data contained in the JSON file.
    * @param scaleWidth
    *   the max value of the width (x coordinates), used for scaling
    * @param scaleHeight
    *   the max value of the height (y coordinates), used for scaling
    * @param cities
    *   the set of cities
    */
  protected case class ConfigData(scaleWidth: Double, scaleHeight: Double, cities: Set[City])

  /** Class that represents a city in the JSON file.
    * @param name
    *   the name of the city
    * @param x
    *   the x coordinate of the city
    * @param y
    *   the y coordinate of the city
    */
  protected case class City(name: String, x: Int, y: Int)

  override protected type Data = ConfigData

  protected given ReadWriter[City] = macroRW
  override protected given readWriter: ReadWriter[Data] = macroRW

  override protected def onSuccess(data: Data): Unit = data.cities.foreach(city => addCity(city)(data))

  private def addCity(city: City)(data: Data): Unit =
    MapView().addCity(city.name, city.x / data.scaleWidth * mapWidth, city.y / data.scaleHeight * mapHeight,
      CityWidth, CityHeight)
