package model.player

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import controller.GameController.{City, Points}

private val cities: (City, City) = ("Paris", "Berlin")
private val points: Points = 8

class BasicObjectiveTest extends AnyFlatSpec with Matchers:
  private val basicObjective = BasicObjective(cities, points)

  "A basic objective" should "be created correctly" in:
    noException should be thrownBy basicObjective
    basicObjective should be(Objective(cities, points))
    basicObjective.unapply() should be(Option(cities, points))

  it should "not be created with illegal values" in:
    a[IllegalArgumentException] should be thrownBy allOf(
      BasicObjective(("Roma", "Roma"), 4),
      BasicObjective(("Roma", "Paris"), 0),
      BasicObjective(("Roma", "Roma"), -1)
    )

class ObjectiveWithCompletionTest extends AnyFlatSpec with Matchers:

  private val objectiveWithCompletion: ObjectiveCompletion = ObjectiveWithCompletion(cities, points)

  "An objective with completion" should "be created correctly" in:
    noException should be thrownBy objectiveWithCompletion
    objectiveWithCompletion.citiesToConnect should be(cities)
    objectiveWithCompletion.points should be(points)
    objectiveWithCompletion.completed should be(false)

  it should "not be created with illegal values" in:
    a[IllegalArgumentException] should be thrownBy allOf(
      ObjectiveWithCompletion(("Roma", "Roma"), 4),
      ObjectiveWithCompletion(("Roma", "Paris"), -2),
      ObjectiveWithCompletion(("Roma", "Roma"), 0)
    )

  it should "be able to be marked as completed" in:
    objectiveWithCompletion.completed should be(false)
    objectiveWithCompletion.markAsComplete()
    objectiveWithCompletion.completed should be(true)
