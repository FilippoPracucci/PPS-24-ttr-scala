package model.objective

import model.map.GameMap
import model.player.Player.PlayerId

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

/** Companion object for [[ObjectiveChecker]]. */
object ObjectiveChecker:
  /** Creates an [[ObjectiveChecker]] that performs checks based on the specified game map.
    *
    * @param gameMap
    *   the [[GameMap]] to use to check the objectives
    * @return
    *   the created [[ObjectiveChecker]]
    */
  def apply(gameMap: GameMap): ObjectiveChecker = ObjectiveCheckerImpl(gameMap)

  private class ObjectiveCheckerImpl(gameMap: GameMap) extends ObjectiveChecker:
    import ConversionHelper.*
    import alice.tuprolog.*
    import model.utils.Scala2P.{*, given}
    import scala.language.implicitConversions

    private val AnyPathPredicate = "any_path"
    private val NoPlayer = "none"
    private val Edge: (Term, Term, Term) => Term = (city1, city2, player) => s"e($city1, $city2, $player)".toLowerCase
    private val Goal: (Term, Term, Term, Term) => Term = (graph, city1, city2, player) =>
      s"$AnyPathPredicate($graph, $city1, $city2, $player, _)."

    private val engine = mkPrologEngine(
      raw"""
        % $AnyPathPredicate(@Graph, ?SourceNode, ?DestinationNode, ?Player, ?ListEdges)
        % ListEdges represents the path between SourceNode and DestinationNode in Graph, with only edges controlled by
        % Player
        $AnyPathPredicate(G1, N1, N2, P, L) :- $AnyPathPredicate(G1, N1, N2, P, [N1], L).

        % $AnyPathPredicate(@Graph, ?SourceNode, ?DestinationNode, ?Player, @ListVisitedNodes, ?ListEdges)
        % ListEdges represents the path between SourceNode and DestinationNode in Graph, with only edges controlled by
        % Player, and without having duplicated nodes. ListVisitedNodes represents the list of visited nodes.
        $AnyPathPredicate(G, N1, N2, P, _, [e(N1, N2)]) :- connected(G, N1, N2, P).
        $AnyPathPredicate(G, N1, N2, P, V, [e(N1, N3) | L]) :- connected(G, N1, N3, P), not_member(N3, V),
          $AnyPathPredicate(G, N3, N2, P, [N3 | V], L).

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
      solveWithSuccess(engine, Goal(gameMap.graph, objective.city1, objective.city2, playerId.toAtom))

    private object ConversionHelper:

      extension (gameMap: GameMap)
        def graph: Term =
          gameMap.routes
            .map(_.connectedCities)
            .map(connectedCities => (connectedCities._1.name, connectedCities._2.name))
            .map((city1, city2) =>
              (city1, city2,
                gameMap.getPlayerClaimingRoute((city1, city2)) match
                  case Right(Some(player)) => player.toString
                  case _ => NoPlayer
              )
            )
            .map((city1, city2, player) => Edge(city1, city2, player))
            .toSeq

      extension (objective: Objective)
        def city1: Term = objective.citiesToConnect._1.toLowerCase
        def city2: Term = objective.citiesToConnect._2.toLowerCase

      extension (playerId: PlayerId) def toAtom: Term = playerId.toString.toLowerCase
