package model.player

import utils.{LoaderFromFile, JsonReader}

// TODO to integrate with the real Objective
private case class Objective(citiesToConnect: (String, String), points: Int)

/** Class that represents a loader of objectives from a JSON file.
 * @param configFilePath 
 *   the given path of the JSON config file (starting from 'src/main/resources/', without file extension) containing
 *   the routes
 */
class ObjectivesLoader()(using override val configFilePath: String) extends LoaderFromFile[Set[Objective]]
    with JsonReader:
  import upickle.default.*

  /** Class that represents the structure of an objective in the JSON file.
   * @param citiesToConnect the pair of cities to connect to complete the objective
   * @param points the points the objective gives after it is completed
   */
  protected case class ObjectiveJson(citiesToConnect: (CityJson, CityJson), points: Int)

  /** Class that represents a city in the JSON file.
   * @param name
   *   the name of the city
   */
  protected case class CityJson(name: String)

  override protected type Data = Set[ObjectiveJson]

  protected given ReadWriter[ObjectiveJson] = macroRW
  protected given ReadWriter[CityJson] = macroRW
  override protected given readWriter: ReadWriter[Data] = readwriter[Seq[ObjectiveJson]].bimap[Data](_.toSeq, _.toSet)

  override protected def onSuccess(objectivesJson: Data): Set[Objective] =
    objectivesJson.map(objectiveJson =>
      Objective((objectiveJson.citiesToConnect._1.name, objectiveJson.citiesToConnect._2.name), objectiveJson.points))

object ObjectivesLoader:
  /** The default path of the JSON config file (starting from 'src/main/resources/', without file extension).
   */
  given defaultConfigFilePath: String = "objectives"