package model.player

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import model.SetLoaderTest

class ObjectivesLoaderTest extends AnyFlatSpec with SetLoaderTest[ObjectiveTemp]:
  import config.Loader

  override protected def createLoader(): Loader[Set[ObjectiveTemp]] =
    import ObjectivesLoader.given
    ObjectivesLoader()

  override protected def createLoaderWith(configFilePath: String): Loader[Set[ObjectiveTemp]] =
    ObjectivesLoader()(using configFilePath)
