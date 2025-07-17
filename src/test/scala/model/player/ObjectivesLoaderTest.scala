package model.player

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import model.SetLoaderTest

class ObjectivesLoaderTest extends AnyFlatSpec with SetLoaderTest[ObjectiveWithCompletion]:
  import config.Loader

  override protected def createLoader(): Loader[Set[ObjectiveWithCompletion]] =
    import ObjectivesLoader.given
    ObjectivesLoader()

  override protected def createLoaderWith(configFilePath: String): Loader[Set[ObjectiveWithCompletion]] =
    ObjectivesLoader()(using configFilePath)
