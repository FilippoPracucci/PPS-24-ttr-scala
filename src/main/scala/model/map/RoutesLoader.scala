package model.map

import utils.{LoaderFromFile, JsonReader}

/** Class that represents a loader of routes from a JSON file.
  * @param configFilePath
  *   the given path of the JSON config file (starting from 'src/main/resources/', without file extension) containing
  *   the routes
  */
class RoutesLoader()(using override val configFilePath: String) extends LoaderFromFile[Set[Route]]
    with JsonReader:
  import upickle.default.*
  import ConversionHelper.routeMechanic

  /** Class that represents the structure of a route in the JSON file.
    * @param connectedCities
    *   the pair of cities connected by the route
    * @param length
    *   the length of the route
    * @param mechanic
    *   the mechanic of the route
    */
  protected case class RouteJson(connectedCities: (CityJson, CityJson), length: Int, mechanic: MechanicJson)

  /** Class that represents a city in the JSON file.
    * @param name
    *   the name of the city
    */
  protected case class CityJson(name: String)

  /** Class that represents the structure of a route mechanic in the JSON file.
    * @param mechanicType
    *   the type of the mechanic expressed as String
    * @param value
    *   the value of the specified type of mechanic expressed as String
    */
  protected case class MechanicJson(mechanicType: String, value: String)

  override protected type Data = Set[RouteJson]

  protected given ReadWriter[RouteJson] = macroRW
  protected given ReadWriter[CityJson] = macroRW
  protected given ReadWriter[MechanicJson] = macroRW
  override protected given readWriter: ReadWriter[Data] = readwriter[Seq[RouteJson]].bimap[Data](_.toSeq, _.toSet)

  override protected def onSuccess(routesJson: Data): Set[Route] =
    routesJson.map(routeJson =>
      Route(
        (City(routeJson.connectedCities._1.name), City(routeJson.connectedCities._2.name)),
        routeJson.length,
        routeJson.routeMechanic
      )
    )

  private object ConversionHelper:
    import model.utils.Color
    import Color.*

    extension (routeJson: RouteJson)
      def routeMechanic: Route.Mechanic = routeJson.mechanic.mechanicType match
        case "specificColor" => Route.SpecificColor(routeJson.mechanic.value.toColor)
        case _ => throw new IllegalStateException(s"${errorMessage}illegal mechanic " +
            s"'${routeJson.mechanic.mechanicType}' found")

    extension (color: String)
      private def toColor: Color = color match
        case "black" => BLACK
        case "white" => WHITE
        case "red" => RED
        case "blue" => BLUE
        case "orange" => ORANGE
        case "yellow" => YELLOW
        case "green" => GREEN
        case "pink" => PINK
        case _ => throw new IllegalStateException(s"${errorMessage}illegal color '$color' found")

object RoutesLoader:
  /** The default path of the JSON config file (starting from 'src/main/resources/', without file extension).
    */
  given defaultConfigFilePath: String = "routes"
