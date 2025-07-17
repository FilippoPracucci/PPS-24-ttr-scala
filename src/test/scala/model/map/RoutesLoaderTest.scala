package model.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RoutesLoaderTest extends AnyFlatSpec with Matchers:
  "A RoutesLoader" should "correctly load the routes" in:
    import RoutesLoader.given
    lazy val routesLoader = RoutesLoader()
    noException should be thrownBy routesLoader
    routesLoader.load() should not be empty

  it should "fail to load the routes if the given config file doesn't exist" in:
    val routesLoader = RoutesLoader(using "nonExistentFile")
    an[IllegalStateException] should be thrownBy routesLoader.load()
