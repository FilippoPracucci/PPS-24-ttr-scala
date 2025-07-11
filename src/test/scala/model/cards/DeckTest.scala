package model.cards

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DeckTest extends AnyFlatSpec with Matchers:

  import model.utils.Color
  import model.utils.Color.*

  val standardDeck: Deck = Deck()
  val NUM_CARDS_PER_COLOR = 12

  "A deck" should "be created correctly with the standard generator" in:
    standardDeck.cards should have size (Color.values.length * NUM_CARDS_PER_COLOR)
    standardDeck.cards.filter(_.color == RED) should have size NUM_CARDS_PER_COLOR

  val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLACK), Card(BLUE))
  val fixedDeckGenerator: CardsGenerator[Deck] = () => fixedList
  val deckFixed: Deck = Deck()(using fixedDeckGenerator)

  it should "be created correctly with specific generator" in:
    deckFixed.cards should have size fixedList.size
    deckFixed.cards.filter(_.color == RED) should have size fixedList.count(_.color == RED)

  it should "draw correctly" in:
    deckFixed.draw(2) should be(fixedList.take(2))
    deckFixed.cards should be(fixedList.takeRight(fixedList.size - 2))

  val cardToAdd: Card = Card(PINK)
  it should "reinsert a card on the bottom correctly" in:
    deckFixed.reinsertAtTheBottom(cardToAdd)
    deckFixed.cards should be(fixedList.takeRight(fixedList.size - 2) :+ cardToAdd)

  it should "shuffle correctly" in:
    deckFixed.cards should have size (fixedList.size - 1)
    deckFixed.shuffle() should not be (fixedList.takeRight(fixedList.size - 2) :+ cardToAdd)
    deckFixed.cards should have size (fixedList.size - 1)
