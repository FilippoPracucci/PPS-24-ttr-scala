package model.cards

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HandTest extends AnyFlatSpec with Matchers:
  import model.utils.Color
  import Color.*

  val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(BLACK), Card(BLUE))
  val fixedDeckGenerator: CardsGenerator[Deck] = () => fixedList
  val deckFixed: Deck = Deck()(using fixedDeckGenerator)

  val hand: Hand = Hand(deckFixed)
  val HAND_INITIAL_SIZE = 4

  "A hand" should "not be created if the deck does not meet the requirements" in:
    val emptyDeck: CardsGenerator[Deck] = () => List.empty
    a[IllegalArgumentException] should be thrownBy Hand(null)
    a[IllegalArgumentException] should be thrownBy Hand(Deck()(using emptyDeck))

  "A hand" should "be created correctly" in:
    hand.cards should have size HAND_INITIAL_SIZE

  "A hand" should "play correctly cards" in:
    hand.playCards(fixedList.take(2))
    hand.cards should be(fixedList.takeRight(fixedList.size - 2))

  "A hand" should "add correctly cards" in:
    val cardsToAdd: List[Card] = List(Card(ORANGE), Card(WHITE))
    hand.cards = fixedList
    hand.addCards(cardsToAdd)
    hand.cards should be(fixedList.appendedAll(cardsToAdd))

  "A hand" should "group correctly the cards by color" in:
    val fixedListWithRepetitions = fixedList.appendedAll(List(Card(RED), Card(BLACK), Card(RED)))
    hand.cards = fixedListWithRepetitions
    hand.groupCardsByColor()
    hand.cards should be(fixedListWithRepetitions.groupBy(_.color).flatMap(_._2).toList)
