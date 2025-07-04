package model.map

/** Trait that represents the loader of the routes.
  */
trait RoutesLoader:
  /** Loads the routes.
    * @return
    *   the loaded set of routes
    */
  def load(): Set[Route]

object RoutesLoader:
  /** The default path of the json config file (starting from 'src/main/resources/').
    */
  given defaultConfigFilePath: String = "routes"

  /** Creates a `RoutesLoader`.
    * @param configFilePath
    *   the given path of the json config file (starting from 'src/main/resources/', without file extension) containing
    *   the routes
    * @return
    *   the created `RoutesLoader`
    */
  def apply()(using configFilePath: String): RoutesLoader = RoutesLoaderImpl(using configFilePath)

  private class RoutesLoaderImpl()(using configFilePath: String) extends RoutesLoader:
    import scala.util._
    import model.utils.Color
    import Color._

    private val errorMessage = "Error loading config file: "
    private val fileExtension = ".json"

    private case class RouteJson(connectedCities: (CityJson, CityJson), length: Int, mechanic: MechanicJson)
    private case class CityJson(name: String)
    private case class MechanicJson(mechanicType: String, value: String)

    override def load(): Set[Route] = readRoutes() match
      case Success(routesJson) =>
        routesJson.map(routeJson =>
          Route(
            (City(routeJson.connectedCities._1.name), City(routeJson.connectedCities._2.name)),
            routeJson.length,
            getMechanicFrom(routeJson)
          )
        )
      case Failure(e) => throw new IllegalStateException(errorMessage + e.getMessage, e)

    private def readRoutes(): Try[Set[RouteJson]] =
      import scala.io.Source
      import upickle.default._

      given ReadWriter[RouteJson] = macroRW
      given ReadWriter[CityJson] = macroRW
      given ReadWriter[MechanicJson] = macroRW

      Using(Source.fromResource(configFilePath + fileExtension)) { source => read[Set[RouteJson]](source.mkString) }

    private def getMechanicFrom(routeJson: RouteJson): Route.Mechanic = routeJson.mechanic.mechanicType match
      case "specificColor" => Route.SpecificColor(getColorFrom(routeJson.mechanic.value))
      case _ => throw new IllegalStateException(s"${errorMessage}illegal mechanic " +
          s"'${routeJson.mechanic.mechanicType}' found")

    private def getColorFrom(color: String): Color = color match
      case "black" => BLACK
      case "white" => WHITE
      case "red" => RED
      case "blue" => BLUE
      case "orange" => ORANGE
      case "yellow" => YELLOW
      case "green" => GREEN
      case "pink" => PINK
      case _ => throw new IllegalStateException(s"${errorMessage}illegal color '$color' found")
