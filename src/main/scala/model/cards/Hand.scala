package model.cards

import model.utils.Color

/** A player's hand of train cards. It's possible to play some cards, add some cards or reorder them grouping by color.
  */
trait Hand extends Cards:
  /** Checks whether the specified number of cards of the specified color can be played (i.e. are present in the
    * player's hand).
    *
    * @param color
    *   the color of the cards
    * @param n
    *   the number of the cards
    * @return
    *   true if the cards can be played, false otherwise
    */
  def canPlayCards(color: Color, n: Int): Boolean

  /** Play a number of train cards of the given color from the player's hand.
    *
    * @param color
    *   the color of train cards to play.
    * @param n
    *   the number of train cards to play.
    */
  def playCards(color: Color, n: Int): Unit

  /** Add a list of train cards to the player's hand.
    *
    * @param cardsToAdd
    *   the list of train cards to add.
    */
  def addCards(cardsToAdd: List[Card]): Unit

/** The factory for player's [[Hand]] instances. */
object Hand:
  private var cardsDeck: Deck = Deck()

  /** Create a player hand from the deck by means of a generator.
    *
    * @param deck
    *   the [[Deck]] of train cards.
    * @param generator
    *   the [[CardsGenerator]] of type [[Hand]].
    * @return
    *   the player's hand created.
    */
  def apply(deck: Deck)(using generator: CardsGenerator[Hand]): Hand =
    require(deck != null && deck.cards.nonEmpty, "The deck has to exist and to not be empty!")
    cardsDeck = deck
    HandImpl(generator.generate())

  /** The standard hand generator according to the rules.
    *
    * @return
    *   the standard [[CardsGenerator]] of type [[Hand]].
    */
  given CardsGenerator[Hand] = () =>
    val HAND_INITIAL_SIZE = 4
    cardsDeck.draw(HAND_INITIAL_SIZE)

  private class HandImpl(private val cardsList: List[Card]) extends Hand:
    cards = cardsList

    override def canPlayCards(color: Color, n: Int): Boolean =
      require(n > 0, "n must be positive")
      cards.count(_.color == color) >= n

    override def playCards(color: Color, n: Int): Unit =
      require(cards.count(_.color == color) >= n, "The cards to play have to be part of the hand!")
      cards = cards diff cards.filter(_.color == color).take(n)

    override def addCards(cardsToAdd: List[Card]): Unit =
      cards = cards :++ cardsToAdd
