package model.player

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import controller.GameController.{City, Points}

private val cities: (City, City) = ("Paris", "Berlin")
private val points: Points = 8

class BasicObjectiveTest extends AnyFlatSpec with Matchers:
  private val basicObjective = BasicObjective(cities, points)

  "A basic objective" should "be created correctly" in:
    basicObjective.citiesToConnect should be(cities)
    basicObjective.points should be(points)

  it should "not be created with illegal values" in:
    a[IllegalArgumentException] should be thrownBy BasicObjective(("Roma", "Roma"), 4)
    a[IllegalArgumentException] should be thrownBy BasicObjective(("Roma", "Paris"), 0)
    a[IllegalArgumentException] should be thrownBy BasicObjective(("Roma", "Roma"), -1)

class ObjectiveWithCompletionTest extends AnyFlatSpec with Matchers:

  private val objectiveWithCompletion: ObjectiveCompletion = ObjectiveWithCompletion(cities, points)

  "An objective with completion" should "be created correctly" in:
    objectiveWithCompletion.citiesToConnect should be(cities)
    objectiveWithCompletion.points should be(points)
    objectiveWithCompletion.completed should be(false)

  it should "not be created with illegal values" in:
    a[IllegalArgumentException] should be thrownBy ObjectiveWithCompletion(("Roma", "Roma"), 4)
    a[IllegalArgumentException] should be thrownBy ObjectiveWithCompletion(("Roma", "Paris"), -2)
    a[IllegalArgumentException] should be thrownBy ObjectiveWithCompletion(("Roma", "Roma"), 0)

  it should "be markable as completed" in:
    objectiveWithCompletion.completed should be(false)
    objectiveWithCompletion.markAsComplete()
    objectiveWithCompletion.completed should be(true)
