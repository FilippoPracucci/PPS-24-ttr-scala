package model.cards

/** A deck of train cards, which can be shuffled and cards can be drawn from it. */
trait Deck extends Cards:
  import scala.util.Random

  /** Shuffle the deck to reorder it randomly. */
  def shuffle(): Unit =
    cards = Random.shuffle(cards)

  /** Draw the given amount of cards from the top of the deck.
    *
    * @param n
    *   the number of cards to draw.
    * @return
    *   the list of cards drawn.
    */
  def draw(n: Int): List[Card] =
    val result = cards.splitAt(n)
    cards = result._2
    result._1

  /** Reinsert the given card at the bottom of the deck.
    *
    * @param card
    *   the card to reinsert at the bottom of the deck.
    */
  def reinsertAtTheBottom(card: Card): Unit =
    cards = cards :+ card

/** A generator of a deck of train cards, that generates the list of train cards composing the deck. */
trait DeckGenerator:
  /** Generate the list of train cards composing the deck.
    *
    * @return
    *   the list of train cards composing the deck.
    */
  def generate(): List[Card]

/** The standard deck generator according to the rules. */
given defaultDeckGenerator: DeckGenerator with
  override def generate(): List[Card] =
    import model.utils.Color

    val NUM_CARDS_PER_COLOR = 12
    var list: List[Card] = List.empty
    for
      color <- Color.values
      i <- 0 until NUM_CARDS_PER_COLOR
    yield list = list :+ Card(color)
    list

/** A deck of train cards.
  *
  * @param generator
  *   the deck generator.
  */
class CardsDeck()(using generator: DeckGenerator)
    extends Cards(generator.generate())
    with Deck
