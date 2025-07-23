package model.player

import model.map.GameMap

/** Trait that represents an objective checker that tells whether a certain objective has been completed or not by a
  * certain player.
  */
trait ObjectiveChecker:
  /** Checks whether the specified objective has been completed by the specified player.
    *
    * @param objective
    *   the objective to check
    * @param playerId
    *   the id of the player
    * @return
    *   true if the objective has been completed, false otherwise
    */
  def check(objective: Objective, playerId: PlayerId): Boolean

object ObjectiveChecker:
  /** Creates an `ObjectiveChecker` that performs checks based on the specified game map.
    *
    * @param gameMap
    *   the game map to use to check the objectives
    * @return
    *   the created `ObjectiveChecker`
    */
  def apply(gameMap: GameMap): ObjectiveChecker = ObjectiveCheckerImpl(gameMap)

  private class ObjectiveCheckerImpl(gameMap: GameMap) extends ObjectiveChecker:
    import alice.tuprolog.*
    import scala.language.implicitConversions
    import model.utils.Scala2P.{*, given}
    import ConversionHelper.*

    private val engine = mkPrologEngine(
      """
        % any_path(@Graph, ?SourceNode, ?DestinationNode, ?Player, ?ListEdges)
        % ListEdges represents the path between SourceNode and DestinationNode in Graph, with only edges controlled by
        % Player
        any_path(G1, N1, N2, P, L) :- any_path(G1, N1, N2, P, [N1], L).

        % any_path(@Graph, ?SourceNode, ?DestinationNode, ?Player, @ListVisitedNodes, ?ListEdges)
        % ListEdges represents the path between SourceNode and DestinationNode in Graph, with only edges controlled by
        % Player, and without having duplicated nodes. ListVisitedNodes represents the list of visited nodes.
        any_path(G, N1, N2, P, _, [e(N1, N2)]) :- connected(G, N1, N2, P).
        any_path(G, N1, N2, P, V, [e(N1, N3) | L]) :- connected(G, N1, N3, P), not_member(N3, V),
          any_path(G, N3, N2, P, [N3 | V], L).

        % connected(@Graph, ?Node1, ?Node2, ?Player)
        % It's satisfied if an edge between Node1 and Node2 controlled by Player exists in Graph.
        connected(G, N1, N2, P) :- member(e(N1, N2, P), G).
        connected(G, N1, N2, P) :- member(e(N2, N1, P), G).

        % not_member(@Element, @List)
        % It's satisfied if Element is not a member of List.
        not_member(_, []).
        not_member(E, [H | T]) :- E \= H, not_member(E, T).
      """
    )

    override def check(objective: Objective, playerId: PlayerId): Boolean =
      solveWithSuccess(engine, createGoalFor(objective, playerId))

    private object ConversionHelper:
      def createGoalFor(objective: Objective, playerId: PlayerId): Term =
        s"any_path($graph, ${objective.city1}, ${objective.city2}, ${playerId.toAtom}, _)."

      private def graph: Term =
        gameMap.routes
          .map(_.connectedCities)
          .map(connectedCities => (connectedCities._1.name, connectedCities._2.name))
          .map((city1, city2) =>
            (city1, city2,
              gameMap.getPlayerClaimingRoute((city1, city2)) match
                case Right(Some(player)) => player.toString
                case _ => "none"
            )
          )
          .map((city1, city2, player) => s"e($city1, $city2, $player)".toLowerCase)
          .toSeq

      extension (objective: Objective) private def city1: Term = objective.citiesToConnect._1.toLowerCase

      extension (objective: Objective) private def city2: Term = objective.citiesToConnect._2.toLowerCase

      extension (playerId: PlayerId) private def toAtom: Term = playerId.toString.toLowerCase
