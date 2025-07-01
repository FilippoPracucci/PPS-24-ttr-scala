package view

/** Trait that represents the loader of the cities in the map view.
  */
trait CitiesLoader:
  /** Loads the cities in the map view.
    */
  def load(): Unit

object CitiesLoader:
  /** The default name of the config file (contained in 'src/main/resources/').
    */
  given defaultConfigFileName: String = "cities.json"

  /** Creates a `CitiesLoader`.
    * @param mapWidth
    *   the width (in pixel) of the map
    * @param mapHeight
    *   the height (in pixel) of the map
    * @param configFileName
    *   the given name of the config file (in 'src/main/resources/') containing information on the cities
    * @return
    *   the created `CitiesLoader`
    */
  def apply(mapWidth: Int, mapHeight: Int)(using configFileName: String): CitiesLoader =
    CitiesLoaderImpl(mapWidth, mapHeight)(using configFileName)

  private class CitiesLoaderImpl(mapWidth: Int, mapHeight: Int)(using configFileName: String) extends CitiesLoader:
    import scala.util._

    private val cityWidth = 0
    private val cityHeight = 0

    private case class City(name: String, x: Int, y: Int)
    private case class ConfigData(scaleWidth: Double, scaleHeight: Double, cities: Seq[City])

    override def load(): Unit = readConfigData() match
      case Success(configData) => configData.cities.foreach(city => addCity(configData, city))
      case Failure(e) => println(s"Error loading config file: ${e.getMessage}")

    private def readConfigData(): Try[ConfigData] =
      import scala.io.Source
      import upickle.default._

      given ReadWriter[City] = macroRW
      given ReadWriter[ConfigData] = macroRW

      Using(Source.fromResource(configFileName)) { source => read[ConfigData](source.mkString) }

    private def addCity(configData: ConfigData, city: City): Unit =
      MapView().addCity(city.name, city.x / configData.scaleWidth * mapWidth,
        city.y / configData.scaleHeight * mapHeight, cityWidth, cityHeight)
