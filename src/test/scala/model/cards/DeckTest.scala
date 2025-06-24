package model.cards

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DeckTest extends AnyFlatSpec with Matchers:

  import model.utils.Color
  import model.utils.Color.*

  val standardDeck: Deck = CardsDeck()
  val NUM_CARDS_PER_COLOR = 12

  "A deck" should "be created correctly with the standard generator" in:
    standardDeck.cards should have size (Color.values.length * NUM_CARDS_PER_COLOR)
    standardDeck.cards.filter(_.color == RED) should have size NUM_CARDS_PER_COLOR

  val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLACK), Card(BLUE))
  val fixedGenerator = new DeckGenerator:
    override def generate(): List[Card] = fixedList

  val deckFixed: Deck = CardsDeck()(using fixedGenerator)

  "A deck" should "be created correctly with specific generator" in:
    deckFixed.cards should have size fixedList.size
    deckFixed.cards.filter(_.color == RED) should have size fixedList.count(_.color == RED)

  "A deck" should "draw correctly" in:
    deckFixed.draw(2) should be(List(Card(RED), Card(YELLOW)))
    deckFixed.cards should be(List(Card(RED), Card(BLACK), Card(BLUE)))

  "A deck" should "reinsert a card on the bottom correctly" in:
    deckFixed.reinsertAtTheBottom(Card(PINK))
    deckFixed.cards should be(List(Card(RED), Card(BLACK), Card(BLUE), Card(PINK)))

  "A deck" should "shuffle correctly" in:
    deckFixed.cards should have size (fixedList.size - 1)
    deckFixed.shuffle() should not be List(Card(RED), Card(BLACK), Card(BLUE), Card(PINK))
    deckFixed.cards should have size (fixedList.size - 1)
