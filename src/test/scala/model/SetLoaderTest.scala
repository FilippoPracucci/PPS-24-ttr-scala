package model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** Trait that represents a generic loader of a set of elements from a file.
  * @tparam A
  *   the type of the elements of the set
  */
trait SetLoaderTest[A] extends AnyFlatSpec with Matchers:
  import config.Loader

  "A SetLoader" should "correctly load the set" in:
    lazy val setLoader: Loader[Set[A]] = createLoader()
    noException should be thrownBy setLoader
    setLoader.load() should not be empty

  it should "fail to load the set if the given config file doesn't exist" in:
    val setLoader = createLoaderWith("nonExistentFile")
    an[IllegalStateException] should be thrownBy setLoader.load()

  /** Creates an instance of the loader to test.
    * @return
    *   an instance of the loader to test
    */
  protected def createLoader(): Loader[Set[A]]

  /** Creates an instance of the loader to test that uses the specified config file.
    * @param configFilePath
    *   the path of the config file containing the set of elements to load
    * @return
    *   an instance of the loader to test
    */
  protected def createLoaderWith(configFilePath: String): Loader[Set[A]]
