package model.objective

import config.GameConfig.ObjectivesPath
import config.{JsonReader, LoaderFromFile}

/** Class that represents a loader of objectives from a JSON file.
  *
  * @param configFilePath
  *   the path of the JSON config file (starting from 'src/main/resources/', without file extension) containing the
  *   objectives (default = "objectives")
  */
class ObjectivesLoader(override val configFilePath: String = ObjectivesPath)
    extends LoaderFromFile[Set[ObjectiveWithCompletion]] with JsonReader:
  import upickle.default.*

  /** Class that represents the structure of an objective in the JSON file.
    *
    * @param citiesToConnect
    *   the pair of cities to connect to complete the objective
    * @param points
    *   the points the objective gives after it is completed
    */
  protected case class ObjectiveJson(citiesToConnect: (CityJson, CityJson), points: Int)

  /** Class that represents a city in the JSON file.
    *
    * @param name
    *   the name of the city
    */
  protected case class CityJson(name: String)

  override protected type Data = Set[ObjectiveJson]

  protected given ReadWriter[ObjectiveJson] = macroRW
  protected given ReadWriter[CityJson] = macroRW
  override protected given readWriter: ReadWriter[Data] = readwriter[Seq[ObjectiveJson]].bimap[Data](_.toSeq, _.toSet)

  override protected def onSuccess(objectivesJson: Data): Set[ObjectiveWithCompletion] =
    objectivesJson.map(objectiveJson =>
      ObjectiveWithCompletion(
        (objectiveJson.citiesToConnect._1.name, objectiveJson.citiesToConnect._2.name),
        objectiveJson.points
      )
    )
