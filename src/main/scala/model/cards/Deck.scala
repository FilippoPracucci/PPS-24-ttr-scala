package model.cards

/** A deck of train cards, which can be shuffled, cards can be drawn from it and reinserted at the bottom of it. */
trait Deck extends Cards:

  /** Shuffle the deck to reorder it randomly. */
  def shuffle(): Unit

  /** Draw the given amount of cards from the top of the deck.
    *
    * @param n
    *   the number of cards to draw, which must be less or equal of the cards remaining in the deck.
    * @return
    *   the list of cards drawn.
    */
  def draw(n: Int): List[Card]

  /** Reinsert the given card at the bottom of the deck.
    *
    * @param card
    *   the card to reinsert at the bottom of the deck.
    */
  def reinsertAtTheBottom(card: Card): Unit

/** The factory for [[Deck]] instances. */
object Deck:
  /** Create a deck by means of a generator.
    *
    * @param generator
    *   the [[CardsGenerator]] for type [[Deck]].
    * @return
    *   the deck created.
    */
  def apply()(using generator: CardsGenerator[Deck]): Deck = DeckImpl(generator.generate())

  /** The standard deck generator according to the rules of the game.
    *
    * @return
    *   the standard [[CardsGenerator]] for type [[Deck]].
    */
  given standardDeckGenerator: CardsGenerator[Deck] = () =>
    import model.utils.Color
    import config.GameConfig.NumCardsPerColor

    var list: List[Card] = List.empty
    for
      color <- Color.values
      i <- 0 until NumCardsPerColor
    yield list = list :+ Card(color)
    list

  private case class DeckImpl(private val cardsList: List[Card]) extends Deck:
    cards = cardsList

    override def shuffle(): Unit =
      import scala.util.Random
      cards = Random.shuffle(cards)

    override def draw(n: Int): List[Card] =
      require(cards.size >= n, "Not enough cards in the deck to draw the amount given!")
      val result = cards.splitAt(n)
      cards = result._2
      result._1

    override def reinsertAtTheBottom(card: Card): Unit =
      cards = cards :+ card
