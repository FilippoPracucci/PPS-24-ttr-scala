package model.map

import model.objective.Points

/** Trait that represents a manager of route points. It provides a way to calculate the points given by each route. */
trait RoutePointsManager:
  /** Returns the number of points given by the specified route.
    *
    * @return
    *   the number of points given by the specified route
    */
  extension (route: Route) def points: Points

/** Companion object for [[RoutePointsManager]]. */
object RoutePointsManager:
  /** Creates a [[RoutePointsManager]] that calculates the points based on the length of the route.
    *
    * @return
    *   the created [[RoutePointsManager]]
    */
  def apply(): RoutePointsManager = RoutePointsManagerByLength

  private object RoutePointsManagerByLength extends RoutePointsManager:
    import config.GameConfig.PointsPerRouteLength

    extension (route: Route) override def points: Points = PointsPerRouteLength(route.length)
