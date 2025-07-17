package model.map

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import model.SetLoaderTest

class RoutesLoaderTest extends AnyFlatSpec with SetLoaderTest[Route]:
  import config.Loader

  override protected def createLoader(): Loader[Set[Route]] =
    import RoutesLoader.given
    RoutesLoader()

  override protected def createLoaderWith(configFilePath: String): Loader[Set[Route]] =
    RoutesLoader()(using configFilePath)
