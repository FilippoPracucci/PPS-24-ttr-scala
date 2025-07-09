package model.map

import model.utils.GameError
import model.player.PlayerId

/** Trait that represents the game map, composed of a set of routes.
  */
trait GameMap:
  /** Type alias that represents the name of a city as String.
    */
  type CityName = String

  /** Returns the set of routes of the `GameMap`
    * @return
    *   the set of routes of the `GameMap`
    */
  def routes: Set[Route]

  /** Gets the player that claims the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names as String
    * @return
    *   an Option containing the `PlayerId` of the player claiming the route, None if no player claims it or if it
    *   doesn't exist
    */
  def getPlayerClaimingRoute(connectedCities: (CityName, CityName)): Option[PlayerId]

  /** Gets the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names as String
    * @return
    *   an Option containing the requested `Route`, None if the route doesn't exist
    */
  def getRoute(connectedCities: (CityName, CityName)): Option[Route]

  /** Makes the player claim the route connecting the specified cities.
    * @param connectedCities
    *   the pair of cities connected by the route, specifying their names as String
    * @param playerId
    *   the `PlayerId` of the player
    * @return
    *   `Right(())` if the action succeeds, `Left(NonClaimableRoute)` if the route doesn't exist or has already been
    *   claimed
    */
  def claimRoute(connectedCities: (CityName, CityName), playerId: PlayerId): Either[GameError, Unit]

object GameMap:
  /** Error that represents the case in which a route can't be claimed.
    */
  case object NonClaimableRoute extends GameError

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

    private def getClaimedRoute(connectedCities: (CityName, CityName)): Option[ClaimedRoute] =
      claimedRoutes.find((r, _) =>
        (r.connectedCities._1.name, r.connectedCities._2.name) == connectedCities
          || (r.connectedCities._2.name, r.connectedCities._1.name) == connectedCities
      )

    override def getPlayerClaimingRoute(connectedCities: (CityName, CityName)): Option[PlayerId] =
      getClaimedRoute(connectedCities).filter((_, o) => o.nonEmpty).map((_, o) => o.get)

    override def getRoute(connectedCities: (CityName, CityName)): Option[Route] =
      getClaimedRoute(connectedCities).map((r, _) => r)

    override def claimRoute(connectedCities: (CityName, CityName), playerId: PlayerId): Either[GameError, Unit] =
      getClaimedRoute(connectedCities)
        .filter((_, p) => p.isEmpty)
        .map((route, _) => claimedRoutes = claimedRoutes.updated(route, Some(playerId)))
        .toRight(NonClaimableRoute)
