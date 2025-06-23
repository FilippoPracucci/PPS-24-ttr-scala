package model.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MapTest extends AnyFlatSpec with Matchers:
  "A City" should "be initialized correctly" in:
    val name = "Roma"
    val city = City(name)
    city.name should be(name)
