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
    private case class SpecificColorImpl(color: Color)
        extends SpecificColor(color)

  def apply(
      connectedCities: (City, City),
      length: Int,
      mechanic: Mechanic
  ): Route =
    RouteImpl(connectedCities, length, mechanic)

  private case class RouteImpl(
      override val connectedCities: (City, City),
      override val length: Int,
      override val mechanic: Mechanic
  ) extends Route
