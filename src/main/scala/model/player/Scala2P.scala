package model.player

import alice.tuprolog.*

object Scala2P:

  def extractTerm(solveInfo: SolveInfo, i: Integer): Term =
    solveInfo.getSolution.asInstanceOf[Struct].getArg(i).getTerm

  def extractTerm(solveInfo: SolveInfo, s: String): Term =
    solveInfo.getTerm(s)

  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")
  given Conversion[String, Theory] = new Theory(_);

  def mkPrologEngine(theory: Theory): Term => LazyList[SolveInfo] =
    val engine = Prolog()
    engine.setTheory(theory)

    goal =>
      new Iterable[SolveInfo] {

        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo] {
          var solution: Option[SolveInfo] = Some(engine.solve(goal))

          override def hasNext: Boolean = solution.isDefined &&
            (solution.get.isSuccess || solution.get.hasOpenAlternatives)

          override def next() =
            try solution.get
            finally solution = if (solution.get.hasOpenAlternatives) Some(engine.solveNext()) else None
        }
      }.to(LazyList)

  def solveWithSuccess(engine: Term => LazyList[SolveInfo], goal: Term): Boolean =
    engine(goal).map(_.isSuccess).headOption.contains(true)

  def solveOneAndGetTerm(engine: Term => LazyList[SolveInfo], goal: Term, term: String): Term =
    engine(goal).headOption.map(extractTerm(_, term)).get

object TryScala2P extends App:
  import Scala2P.{*, given}

  private val engine: Term => LazyList[SolveInfo] = mkPrologEngine("""
    anypath(G1, N1, N2, P, L) :- anypath(G1, N1, N2, P, [N1], L).

    anypath(G, N1, N2, P, _, [e(N1, N2)]) :- connected(G, N1, N2, P).
    anypath(G, N1, N2, P, V, [e(N1, N3) | L]) :- connected(G, N1, N3, P), nonmember(N3, V),
      anypath(G, N3, N2, P, [N3 | V], L).

    connected(G, N1, N2, P) :- member(e(N1, N2, P), G).
    connected(G, N1, N2, P) :- member(e(N2, N1, P), G).

    nonmember(_, []).
    nonmember(E, [H | T]) :- E \= H, nonmember(E, T).
  """)

  import model.map.GameMap
  import GameMap.given
  val gameMap = GameMap()
  val edges = gameMap.routes.map(_.connectedCities).map(cc => (cc._1.name, cc._2.name))
    .map((c1, c2) =>
      (c1.toLowerCase, c2.toLowerCase,
        gameMap.getPlayerClaimingRoute((c1, c2)) match
          case Right(Some(player)) => player.toString.toLowerCase
          case _ => "none"
      )
    )
    .map((c1, c2, p) => s"e($c1, $c2, $p)")
    .toString
  val graphList = s"[${edges.slice(8, edges.length - 1)}]".toLowerCase
  println(graphList)

  val objective = ObjectiveWithCompletion(("Lisboa", "Danzic"), 20)
  val city1 = objective.citiesToConnect._1.toLowerCase
  val city2 = objective.citiesToConnect._2.toLowerCase
  val player = Player(model.utils.PlayerColor.GREEN, objective = null).id.toString.toLowerCase

  // val goal = s"anypath($graphList, $city1, $city2, $player, L)."
  val goal = s"anypath([e(lisboa, london, green), e(london, danzic, blue), e(danzic, berlin, green), " +
    s"e(berlin, london, green), e(lisboa, danzic, red)], $city1, $city2, $player, L)."
  println(goal)

  engine(goal) foreach println
