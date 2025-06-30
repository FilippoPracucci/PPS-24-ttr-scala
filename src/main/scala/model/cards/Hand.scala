package model.cards

/** A player's hand of train cards. It's possible to play some cards, add some cards or reorder them grouping by color.
  */
trait Hand extends Cards:
  /** Play a list of train cards from the player's hand.
    *
    * @param cardsToPlay
    *   the list of train cards to play.
    */
  def playCards(cardsToPlay: List[Card]): Unit

  /** Add a list of train cards to the player's hand.
    *
    * @param cardsToAdd
    *   the list of train cards to add.
    */
  def addCards(cardsToAdd: List[Card]): Unit

  /** Reorder the train cards of the player's hand, grouping them by color. */
  def groupCardsByColor(): Unit

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

    override def playCards(cardsToPlay: List[Card]): Unit =
      cards = cards.filter(!cardsToPlay.contains(_))

    override def addCards(cardsToAdd: List[Card]): Unit =
      cards = cards :++ cardsToAdd

    override def groupCardsByColor(): Unit =
      cards = cards.groupBy(_.color).flatMap(_._2).toList
