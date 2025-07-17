package model.player

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ObjectivesLoaderTest extends AnyFlatSpec with Matchers:
  "An ObjectivesLoader" should "correctly load the objectives" in:
    import ObjectivesLoader.given
    ObjectivesLoader().load() should not be empty

  it should "fail to load the objectives if the given config file doesn't exist" in:
    val objectivesLoader = ObjectivesLoader(using "nonExistentFile")
    an[IllegalStateException] should be thrownBy objectivesLoader.load()
