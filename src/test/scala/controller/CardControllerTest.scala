package controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardControllerTest extends AnyFlatSpec with Matchers:
  import model.cards.Card
  import model.utils.Color
  import java.awt.Color as ViewColor

  var cards: List[Card] = List.empty
  for
    color <- Color.values
  yield cards +:= Card(color)

  val viewColors: List[ViewColor] =
    List(
      ViewColor.BLACK,
      ViewColor.WHITE,
      ViewColor.RED.darker(),
      ViewColor.BLUE.darker(),
      ViewColor.ORANGE.darker(),
      ViewColor.YELLOW.darker(),
      ViewColor.GREEN.darker(),
      ViewColor.PINK.darker()
    )

  import CardControllerColor.*
  "A card" should "have the right ViewColor" in:
    cards.map(_.cardColor) should contain theSameElementsInOrderAs viewColors.reverse
    cards.map(_.cardTextColor) should contain only (ViewColor.BLACK, ViewColor.WHITE)

  it should "have the right color name" in:
    cards.map(_.cardName) should contain theSameElementsInOrderAs Color.values.map(_.toString).reverse
