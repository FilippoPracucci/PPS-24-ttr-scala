package model.map

/** Trait that represents a city. */
trait City:
  /** Gets the name of the city.
    *
    * @return
    *   the name of the city
    */
  def name: String

/** Companion object for [[City]]. */
object City:
  /** Creates a [[City]].
    *
    * @param name
    *   the name of the city
    * @return
    *   the created [[City]]
    */
  def apply(name: String): City = CityImpl(name)

  private case class CityImpl(override val name: String) extends City

/** Trait that represents a route between two cities. */
trait Route:
  import Route.Mechanic

  /** Gets the pair of cities connected by the route.
    *
    * @return
    *   the pair of cities connected by the route
    */
  def connectedCities: (City, City)

  /** Gets the length of the route.
    *
    * @return
    *   the integer number representing the length of the route
    */
  def length: Int

  /** Gets the [[Mechanic]] followed by the route.
    *
    * @return
    *   the [[Mechanic]] followed by the route
    */
  def mechanic: Mechanic

/** Companion object for [[Route]]. */
object Route:
  import model.utils.Color

  /** Trait that represents the mechanic of a route. */
  trait Mechanic

  /** Trait that represents the mechanic in which a route has a specific color. */
  trait SpecificColor extends Mechanic:
    /** Returns the color of the route.
      *
      * @return
      *   the color of the route
      */
    def color: Color

  /** Companion object for [[SpecificColor]]. */
  object SpecificColor:
    /** Creates a [[SpecificColor]] mechanic.
      *
      * @param color
      *   the color of the route
      * @return
      *   the created [[SpecificColor]]
      */
    def apply(color: Color): SpecificColor = SpecificColorImpl(color)

    /** Allows instances of the type [[SpecificColor]] to be matched and deconstructed using pattern matching.
      *
      * @param specificColor
      *   the instance to be matched and deconstructed
      * @return
      *   an [[Option]] containing the extracted color if the instance matches, [[None]] otherwise
      */
    def unapply(specificColor: SpecificColor): Option[Color] = Some(specificColor.color)

    private case class SpecificColorImpl(override val color: Color) extends SpecificColor

  /** Creates a [[Route]].
    *
    * @param connectedCities
    *   the pair of cities the route connects
    * @param length
    *   the length of the route
    * @param mechanic
    *   the mechanic of the route
    * @return
    *   the created [[Route]]
    */
  def apply(connectedCities: (City, City), length: Int, mechanic: Mechanic): Route =
    RouteImpl(connectedCities, length, mechanic)

  private case class RouteImpl(override val connectedCities: (City, City), override val length: Int,
      override val mechanic: Mechanic) extends Route:
    require(length > 0, "length must be positive")
