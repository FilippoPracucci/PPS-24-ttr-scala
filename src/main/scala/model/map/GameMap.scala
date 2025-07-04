package model.map

import model.player.PlayerId

/** Trait that represents the game map, composed of a set of routes.
  */
trait GameMap:
  /** Returns the set of routes of the `GameMap`
    * @return
    *   the set of routes of the `GameMap`
    */
  def routes: Set[Route]

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
  /** The default path of the config file (exported from `RoutesLoader`).
    */
  export RoutesLoader.given String

  /** Creates a `GameMap` composed of the specified set of routes.
    * @param routes
    *   the set of routes it will contain
    * @return
    *   the created `GameMap`
    */
  def apply(routes: Set[Route]): GameMap = GameMapImpl(routes)

  /** Creates a `GameMap` composed of the routes specified in the config file.
    * @param configFilePath
    *   the given path of the json config file (starting from 'src/main/resources/', without file extension) containing
    *   the routes
    * @return
    *   the created `GameMap`
    */
  def apply()(using configFilePath: String): GameMap = GameMapImpl(RoutesLoader()(using configFilePath).load())

  private class GameMapImpl(override val routes: Set[Route]) extends GameMap:
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
