package model.utils

/** Object that provides utility methods to set theories and resolve Prolog goals in Scala. Adapted from
  * https://github.com/unibo-pps/pps-lab12/blob/master/src/main/scala/it/unibo/u12lab/code/Scala2P.scala
  */
object Scala2P:
  import alice.tuprolog.*
  import scala.language.implicitConversions

  /** Implicit conversion from `String` to `Term`.
    */
  given Conversion[String, Term] = Term.createTerm(_)

  /** Implicit conversion from `Seq[_]` to `Term`.
    */
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")

  /** Implicit conversion from `String` to `Theory`.
    */
  given Conversion[String, Theory] = Theory.parseWithStandardOperators(_)

  /** Creates a Prolog engine from the specified theory.
    *
    * @param theory
    *   the Prolog theory
    * @return
    *   the created Prolog engine
    */
  def mkPrologEngine(theory: Theory): Term => LazyList[SolveInfo] =
    val engine = Prolog()
    engine.setTheory(theory)

    goal =>
      new Iterable[SolveInfo] {

        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo] {
          var solution: Option[SolveInfo] = Some(engine.solve(goal))

          override def hasNext: Boolean = solution.isDefined &&
            (solution.get.isSuccess || solution.get.hasOpenAlternatives)

          override def next(): SolveInfo =
            try solution.get
            finally solution = if (solution.get.hasOpenAlternatives) Some(engine.solveNext()) else None
        }
      }.to(LazyList)

  /** Solves the specified goal using the specified Prolog engine.
    *
    * @param engine
    *   the Prolog engine to use
    * @param goal
    *   the goal to solve
    * @return
    *   true if the goal can be resolved successfully, false otherwise
    */
  def solveWithSuccess(engine: Term => LazyList[SolveInfo], goal: Term): Boolean =
    engine(goal).map(_.isSuccess).headOption.contains(true)
