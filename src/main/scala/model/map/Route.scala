package model.map

/** Trait that represents a city.
  */
trait City:
  /** Gets the name of the city.
    * @return
    *   the name of the city
    */
  def name: String

object City:
  /** Creates a `City`.
    * @param name
    *   the name of the city
    * @return
    *   the created `City`
    */
  def apply(name: String): City = CityImpl(name)

  private case class CityImpl(override val name: String) extends City

/** Trait that represents a route between two cities.
  */
trait Route:
  import Route.Mechanic

  /** Gets the pair of cities connected by the route.
    * @return
    *   the pair of cities connected by the route
    */
  def connectedCities: (City, City)

  /** Gets the length of the route.
    * @return
    *   the integer number representing the length of the route
    */
  def length: Int

  /** Gets the `Mechanic` followed by the route.
    * @return
    *   the `Mechanic` followed by the route
    */
  def mechanic: Mechanic

object Route:
  /** Trait that represents the mechanic of a route.
    */
  trait Mechanic

  private type Color = String // TODO to be integrated with utils.Color

  /** Trait that represents the mechanic in which a route has a specific color.
    * @param color
    *   the color of the route
    */
  trait SpecificColor(color: Color) extends Mechanic
  object SpecificColor:
    /** Creates a `SpecificColor` mechanic.
      * @param color
      *   the color of the route
      * @return
      *   the created `SpecificColor`
      */
    def apply(color: String): SpecificColor = SpecificColorImpl(color)

    private case class SpecificColorImpl(color: Color) extends SpecificColor(color)

  /** Creates a `Route`.
    * @param connectedCities
    *   the pair of cities the route connects
    * @param length
    *   the length of the route
    * @param mechanic
    *   the mechanic of the route
    * @return
    *   the created `Route`
    */
  def apply(connectedCities: (City, City), length: Int, mechanic: Mechanic): Route =
    RouteImpl(connectedCities, length, mechanic)

  private case class RouteImpl(override val connectedCities: (City, City), override val length: Int,
      override val mechanic: Mechanic) extends Route
