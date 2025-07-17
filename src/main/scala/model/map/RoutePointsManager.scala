package model.map

/** Trait that represents a manager of route points. It provides a way to calculate the points given by each route.
  */
trait RoutePointsManager:
  /** Returns the number of points given by the specified route.
    */
  extension (route: Route) def points: Int

object RoutePointsManager:
  /** Creates a `RoutePointsManager` that calculates the points based on the length of the route.
    * @return
    *   the created `RoutePointsManager`
    */
  def apply(): RoutePointsManager = RoutePointsManagerByLength()

  private class RoutePointsManagerByLength extends RoutePointsManager:
    private type Length = Int
    private type Points = Int
    private val pointsPerRouteLength: Map[Length, Points] = Map(
      1 -> 1,
      2 -> 2,
      3 -> 4,
      4 -> 7,
      6 -> 15,
      8 -> 21
    )

    extension (route: Route) override def points: Int = pointsPerRouteLength(route.length)
