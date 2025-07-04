package view.map

/** Trait that represents the loader of the cities in the map view.
  */
trait CitiesLoader:
  /** Loads the cities in the map view.
    */
  def load(): Unit

object CitiesLoader:
  /** The default path of the config file (starting from 'src/main/resources/').
    */
  given defaultConfigFilePath: String = "cities"

  /** Creates a `CitiesLoader`.
    * @param mapWidth
    *   the width (in pixel) of the map
    * @param mapHeight
    *   the height (in pixel) of the map
    * @param configFilePath
    *   the given path of the json config file (starting from 'src/main/resources/', without file extension) containing
    *   information on the cities
    * @return
    *   the created `CitiesLoader`
    */
  def apply(mapWidth: Int, mapHeight: Int)(using configFilePath: String): CitiesLoader =
    CitiesLoaderImpl(mapWidth, mapHeight)(using configFilePath)

  private class CitiesLoaderImpl(mapWidth: Int, mapHeight: Int)(using configFilePath: String) extends CitiesLoader:
    require(mapWidth > 0, "mapWidth must be positive")
    require(mapHeight > 0, "mapHeight must be positive")

    import scala.util._

    private val fileExtension = ".json"
    private val errorMessage = "Error loading config file: "
    private val cityWidth = 0
    private val cityHeight = 0

    private case class City(name: String, x: Int, y: Int)
    private case class ConfigData(scaleWidth: Double, scaleHeight: Double, cities: Set[City])

    override def load(): Unit = readConfigData() match
      case Success(configData) => configData.cities.foreach(city => addCity(configData, city))
      case Failure(e) => throw new IllegalStateException(errorMessage + e.getMessage, e)

    private def readConfigData(): Try[ConfigData] =
      import scala.io.Source
      import upickle.default._

      given ReadWriter[City] = macroRW
      given ReadWriter[ConfigData] = macroRW

      Using(Source.fromResource(configFilePath + fileExtension)) { source => read[ConfigData](source.mkString) }

    private def addCity(configData: ConfigData, city: City): Unit =
      MapView().addCity(city.name, city.x / configData.scaleWidth * mapWidth,
        city.y / configData.scaleHeight * mapHeight, cityWidth, cityHeight)
