package model.cards

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardTest extends AnyFlatSpec with Matchers:
  import model.utils.Color.*

  "A card" should "be created correctly" in:
    val card = Card(WHITE)
    card.color should be(WHITE)
    card.color should not be BLUE
