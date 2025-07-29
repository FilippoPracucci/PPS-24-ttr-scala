package config

/** Trait that represents a loader of data.
  *
  * @tparam A
  *   the type of data loaded
  */
trait Loader[A]:
  /** Loads the data.
    *
    * @return
    *   the loaded data
    */
  def load(): A

/** Trait that represents a loader from file.
  *
  * @tparam A
  *   the type of data loaded
  */
trait LoaderFromFile[A] extends Loader[A] with FileReader:
  import scala.util.{Success, Failure}
  import scala.compiletime.summonFrom

  /** The general error message to display when an error occurs. It can be used as a prefix for the specific error.
    *
    * @return
    *   the error message
    */
  protected def errorMessage: String = "Error loading config file: "

  override def load(): A = readFromFile() match
    case Success(data) => onSuccess(data)
    case Failure(e) => throw new IllegalStateException(errorMessage + e.getMessage, e)

  /** Specifies the actions to perform after obtaining the data read from the file. To be defined in the implementation.
    *
    * @param data
    *   the data read from the file
    * @return
    *   the data that will be returned by the loader
    */
  protected def onSuccess(data: Data): A

/** Trait that represents a file reader. */
trait FileReader:
  import scala.util.{Try, Using}
  import scala.io.Source

  /** The path of the config file (starting from 'src/main/resources/'), without file extension, to be defined in the
    * implementation.
    *
    * @return
    *   the path of the config file
    */
  protected def configFilePath: String

  /** The file extension, to be defined in the implementation, of the file to be read.
    *
    * @return
    *   the file extension
    */
  protected def fileExtension: String

  /** The type of the data read from file, to be defined in the implementation. */
  protected type Data

  /** Reads the data from the specified file.
    *
    * @return
    *   [[scala.util.Success]] containing the data read if no errors occurred, otherwise [[scala.util.Failure]]
    *   containing the thrown exception
    */
  protected def readFromFile(): Try[Data] =
    Using(Source.fromResource(configFilePath + "." + fileExtension))(readFromSource)

  /** Reads the data from the specified source. To be defined in the implementation.
    *
    * @param source
    *   the source to read
    * @return
    *   the data read
    */
  protected def readFromSource(source: Source): Data

/** Trait that represents a JSON file reader. */
trait JsonReader extends FileReader:
  import scala.io.Source
  import upickle.default.*

  final override protected def fileExtension: String = "json"

  /** The given instance to read and write values of type [[Data]] using the upickle library, to be defined in the
    * implementation.
    *
    * @return
    *   the [[ReadWriter]] for values of type [[Data]]
    */
  protected given readWriter: ReadWriter[Data]

  final override protected def readFromSource(source: Source): Data = read[Data](ujson.read(source.mkString))
