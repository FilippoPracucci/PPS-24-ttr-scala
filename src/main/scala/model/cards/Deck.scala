package model.cards

trait Deck extends Cards:
  import scala.util.Random

  def shuffle(): Unit =
    cards = Random.shuffle(cards)
  def draw(n: Int): List[Card] =
    val result = cards.splitAt(n)
    cards = result._2
    result._1
  def reinsertAtTheBottom(card: Card): Unit =
    cards = cards :+ card

trait DeckGenerator:
  def generate(): List[Card]

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

class CardsDeck()(using generator: DeckGenerator)
    extends Cards(generator.generate())
    with Deck
