package model.map

trait City:
  def name: String

object City:
  def apply(name: String): City = CityImpl(name)

  private case class CityImpl(override val name: String) extends City

trait Route:
  import Route.Mechanic
  def connectedCities: (City, City)
  def length: Int
  def mechanic: Mechanic

object Route:
  trait Mechanic

  private type Color = String // TODO to be integrated with utils.Color
  trait SpecificColor(color: Color) extends Mechanic
  object SpecificColor:
    def apply(color: String): SpecificColor = SpecificColorImpl(color)
    private case class SpecificColorImpl(color: Color) extends SpecificColor(color)

  def apply(connectedCities: (City, City), length: Int, mechanic: Mechanic): Route =
    RouteImpl(connectedCities, length, mechanic)

  private case class RouteImpl(override val connectedCities: (City, City), override val length: Int,
      override val mechanic: Mechanic) extends Route

type PlayerId = String // TODO to be integrated in the future

trait GameMap:
  def getPlayerClaimingRoute(connectedCities: (City, City)): Option[PlayerId]
  def getRoute(connectedCities: (City, City)): Option[Route]
  def claimRoute(connectedCities: (City, City), player: PlayerId): Unit

object GameMap:
  def apply(routes: Set[Route]): GameMap = GameMapImpl(routes)

  private class GameMapImpl(routes: Set[Route]) extends GameMap:
    private type ClaimedRoute = (Route, Option[PlayerId])
    private val claimedRoutes: Set[ClaimedRoute] = routes.map(r => (r, None))

    private def getClaimedRoute(connectedCities: (City, City)): Option[ClaimedRoute] =
      claimedRoutes.find((r, _) =>
        r.connectedCities == connectedCities || r.connectedCities == connectedCities.swap
      )

    override def getPlayerClaimingRoute(connectedCities: (City, City)): Option[PlayerId] = ???

    override def getRoute(connectedCities: (City, City)): Option[Route] =
      getClaimedRoute(connectedCities).map((r, _) => r)

    override def claimRoute(connectedCities: (City, City), player: PlayerId): Unit = ???
