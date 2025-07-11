package model.cards

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HandTest extends AnyFlatSpec with Matchers:
  import model.utils.Color
  import Color.*

  val fixedList: List[Card] = List(Card(RED), Card(YELLOW), Card(RED), Card(BLUE))
  val deckFixed: Deck = Deck()(using () => fixedList)

  val hand: Hand = Hand(deckFixed)
  val HAND_INITIAL_SIZE = 4

  "A hand" should "not be created if the deck does not meet the requirements" in:
    val emptyDeck: CardsGenerator[Deck] = () => List.empty
    a[IllegalArgumentException] should be thrownBy Hand(null)
    a[IllegalArgumentException] should be thrownBy Hand(Deck()(using emptyDeck))

  it should "be created correctly" in:
    hand.cards should have size HAND_INITIAL_SIZE

  it should "play correctly cards" in:
    val numberOfCardsToPlay = 2
    val colorToPlay = RED
    hand.playCards(colorToPlay, numberOfCardsToPlay)
    hand.cards should be(fixedList diff fixedList.filter(_.color == colorToPlay).take(numberOfCardsToPlay))
    a[IllegalArgumentException] should be thrownBy hand.playCards(YELLOW, numberOfCardsToPlay)
    a[IllegalArgumentException] should be thrownBy hand.playCards(PINK, 1)

  it should "add correctly cards" in:
    val cardsToAdd: List[Card] = List(Card(ORANGE), Card(WHITE))
    hand.cards = fixedList
    hand.addCards(cardsToAdd)
    hand.cards should be(fixedList :++ cardsToAdd)
