package model.map

trait City:
  def name: String

object City:
  def apply(name: String): City = CityImpl(name)

  private case class CityImpl(override val name: String) extends City
