package model.cards

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CardTest extends AnyFlatSpec with Matchers:
  import model.utils.Color.*

  "A card" should "be created correctly" in:
    val card = Card(WHITE)
    card.color should be(WHITE)
    card.color should not be BLUE

  val listWithTwoCards: List[Card] = List(Card(RED), Card(YELLOW))

  "A list of cards" should "be created correctly with standard generator" in:
    val cardsList: Cards = Cards()
    cardsList.cards shouldBe empty
    cardsList.cards = listWithTwoCards
    cardsList.cards should be(listWithTwoCards)

  "A list of cards" should "be created correctly with custom generator" in:
    val customGenerator: CardsGenerator[Cards] = () => listWithTwoCards
    val cardsList: Cards = Cards(using customGenerator)()
    cardsList.cards should have size listWithTwoCards.size
    cardsList.cards should be(listWithTwoCards)
