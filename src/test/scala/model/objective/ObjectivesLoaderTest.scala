package model.objective

import model.SetLoaderTest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ObjectivesLoaderTest extends AnyFlatSpec with SetLoaderTest[ObjectiveWithCompletion]:
  import config.Loader

  override protected def createLoader(): Loader[Set[ObjectiveWithCompletion]] = ObjectivesLoader()

  override protected def createLoaderWith(configFilePath: String): Loader[Set[ObjectiveWithCompletion]] =
    ObjectivesLoader(configFilePath)
