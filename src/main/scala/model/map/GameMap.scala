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

type PlayerId = String // TODO to be integrated in the future

/** Trait that represents the game map, composed of a set of routes.
  */
trait GameMap:
  /** Gets the player that claims the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route
    * @return
    *   an Option containing the `PlayerId` of the player claiming the route, None if no player claims it or if it
    *   doesn't exist
    */
  def getPlayerClaimingRoute(connectedCities: (City, City)): Option[PlayerId]

  /** Gets the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route
    * @return
    *   an Option containing the requested `Route`, None if the route doesn't exist
    */
  def getRoute(connectedCities: (City, City)): Option[Route]

  /** Makes the player claim the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route
    * @param playerId
    *   the `PlayerId` of the player
    * @throws IllegalArgumentException
    *   if the route doesn't exist or has already been claimed
    */
  def claimRoute(connectedCities: (City, City), playerId: PlayerId): Unit

object GameMap:
  /** Creates a `GameMap` composed of the specified set of routes.
    * @param routes
    *   the set of routes the `GameMap` will contain
    * @return
    *   the created `GameMap`
    */
  def apply(routes: Set[Route]): GameMap = GameMapImpl(routes)

  private class GameMapImpl(routes: Set[Route]) extends GameMap:
    private type ClaimedRoute = (Route, Option[PlayerId])
    private var claimedRoutes = routes.map(r => (r, None): ClaimedRoute).toMap

    private def getClaimedRoute(connectedCities: (City, City)): Option[ClaimedRoute] =
      claimedRoutes.find((r, _) => r.connectedCities == connectedCities || r.connectedCities == connectedCities.swap)

    override def getPlayerClaimingRoute(connectedCities: (City, City)): Option[PlayerId] =
      getClaimedRoute(connectedCities).filter((_, o) => o.nonEmpty).map((_, o) => o.get)

    override def getRoute(connectedCities: (City, City)): Option[Route] =
      getClaimedRoute(connectedCities).map((r, _) => r)

    override def claimRoute(connectedCities: (City, City), playerId: PlayerId): Unit =
      claimedRoutes = claimedRoutes.updated(
        getClaimedRoute(connectedCities)
          .filter((_, o) => o.isEmpty)
          .getOrElse(throw new IllegalArgumentException(s"Route $connectedCities not present or already claimed"))
          ._1,
        Some(playerId)
      )
