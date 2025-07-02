package view.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CitiesLoaderTest extends AnyFlatSpec with Matchers:
  private val mapWidth = 900
  private val mapHeight = 600
  private val illegalParam = -10
  import CitiesLoader.given

  "A CitiesLoader" should "load the cities" in:
    val citiesLoader = CitiesLoader(mapWidth, mapHeight)
    noException should be thrownBy citiesLoader.load()

  it should "fail to load the cities if the given config file doesn't exist" in:
    val citiesLoader = CitiesLoader(mapWidth, mapHeight)(using "nonExistentFile.json")
    an[IllegalStateException] should be thrownBy citiesLoader.load()

  it should "fail to initialize if the given map width or height aren't positive numbers" in:
    an[IllegalArgumentException] should be thrownBy CitiesLoader(illegalParam, mapHeight)
    an[IllegalArgumentException] should be thrownBy CitiesLoader(mapWidth, illegalParam)
    an[IllegalArgumentException] should be thrownBy CitiesLoader(illegalParam, illegalParam)
