package model.cards

import model.utils.Color

/** A player's hand of train cards. It's possible to play and add some cards to it. */
trait Hand extends Cards:
  /** Checks whether the specified number of cards of the specified color can be played (i.e. are present in the
    * player's hand).
    *
    * @param color
    *   the color of the cards
    * @param n
    *   the number of the cards
    * @return
    *   [[true]] if the cards can be played, [[false]] otherwise
    */
  def canPlayCards(color: Color, n: Int): Boolean

  /** Play the specified number of cards of the specified color from the player's hand.
    *
    * @param color
    *   the color of train cards to play.
    * @param n
    *   the number of train cards to play.
    * @return
    *   the list of train cards that has been played.
    */
  def playCards(color: Color, n: Int): List[Card]

  /** Add a list of train cards to the player's hand.
    *
    * @param cardsToAdd
    *   the list of train cards to add.
    */
  def addCards(cardsToAdd: List[Card]): Unit

/** The factory for player's [[Hand]] instances. */
object Hand:
  private var _cardsDeck: Deck = Deck()

  /** A [[Generator]] for type [[Hand]]. */
  abstract class HandGenerator extends Generator[Hand]:
    override def generate(): Hand = HandImpl(generateCards())

  /** Create a player hand from the deck by means of a generator.
    *
    * @param deck
    *   the [[Deck]] of train cards, it must be not empty.
    * @param generator
    *   the [[HandGenerator]].
    * @return
    *   the player's hand created.
    */
  def apply(deck: Deck)(using generator: HandGenerator): Hand =
    require(deck != null && deck.cards.nonEmpty, "The deck has to exist and to not be empty!")
    _cardsDeck = deck
    generator.generate()

  /** The standard hand generator according to the rules of the game.
    *
    * @return
    *   the standard [[HandGenerator]].
    */
  given HandGenerator with
    override def generateCards(): List[Card] =
      import config.GameConfig.HandInitialSize
      _cardsDeck.draw(HandInitialSize)

  private class HandImpl(private val cardsList: List[Card]) extends Hand:
    cards = cardsList
    groupCardsByColor()

    override def canPlayCards(color: Color, n: Int): Boolean =
      require(n > 0, "n must be positive")
      cards.count(_.color == color) >= n

    override def playCards(color: Color, n: Int): List[Card] =
      require(cards.count(_.color == color) >= n, "The cards to play have to be part of the hand!")
      val cardsToPlay = cards.filter(_.color == color).take(n)
      cards = cards diff cardsToPlay
      cardsToPlay

    override def addCards(cardsToAdd: List[Card]): Unit =
      cards = cardsToAdd ++: cards
      groupCardsByColor()

    private def groupCardsByColor(): Unit =
      cards = cards.groupBy(_.color).flatMap(_._2).toList.sortWith(_.color.toString < _.color.toString)
