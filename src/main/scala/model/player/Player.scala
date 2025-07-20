package model.player

import model.cards.{Deck, Hand}
import model.utils.{Color, PlayerColor, GameError}

type PlayerId = PlayerColor

/** A player, that has an id, an objective, a hand of train cards, a number of train cars and a reference to the deck.
  * The player can draw cards and claim a route.
  */
trait Player:
  /** The player's identifier.
    *
    * @return
    *   the player's identifier.
    */
  def id: PlayerId

  /** The player's objective.
    *
    * @return
    *   the player's objective.
    */
  def objective: Objective

  /** The player's train cards hand.
    *
    * @return
    *   the player's hand.
    */
  def hand: Hand

  private type Trains = model.player.Player.TrainCars

  /** The player's train cars.
    *
    * @return
    *   the player's train cars.
    */
  def trains: Trains

  /** The player's score.
    *
    * @return
    *   the player's score
    */
  def score: Int

  /** Draw the given amount of cards from the deck and put them in the player's hand.
    *
    * @param n
    *   the number of cards to draw.
    * @return
    *   `Right(())` if the action succeeds, `Left(NotEnoughCardsInTheDeck)` if the there are not enough cards in the
    *   deck.
    */
  def drawCards(n: Int): Either[GameError, Unit]

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

  /** Plays the specified number of cards of the specified color.
    *
    * @param color
    *   the color of the cards to play
    * @param n
    *   the number of the cards to play
    * @return
    *   `Right(())` if the action succeeds, `Left(NotEnoughCards)` if the player doesn't have enough cards
    */
  def playCards(color: Color, n: Int): Either[GameError, Unit]

  /** Places the specified number of trains.
    *
    * @param n
    *   the number of trains to place
    * @return
    *   `Right(())` if the action succeeds, `Left(NotEnoughTrains)` if the player doesn't have enough trains
    */
  def placeTrains(n: Int): Either[GameError, Unit]

  /** Adds the specified number of points to the player's score.
    *
    * @param points
    *   the number of points to add
    */
  def addPoints(points: Int): Unit

/** The factory for [[Player]] instances. */
object Player:
  /** Error that represents the case in which the deck doesn't have enough cards. */
  case object NotEnoughCardsInTheDeck extends GameError

  /** Error that represents the case in which a player doesn't have enough cards.
    */
  case object NotEnoughCards extends GameError

  /** Error that represents the case in which a player doesn't have enough trains.
    */
  case object NotEnoughTrains extends GameError

  /** The train cars, which are the number owned by the player with the possibility to set it or place some of them. */
  trait TrainCars:
    /** Number of player train cars left.
      *
      * @return
      *   the number of train cars left.
      */
    def trainCars: Int

    /** Place the given amount of player train cars, if they are sufficient.
      *
      * @param n
      *   the number of train cars to place.
      */
    def placeTrainCars(n: Int): Unit

  private object TrainCars:
    def apply(numberTrainCars: Int): TrainCars = TrainCarsImpl(numberTrainCars)

    private case class TrainCarsImpl(private var _trainCars: Int) extends TrainCars:
      override def trainCars: Int = _trainCars

      override def placeTrainCars(n: Int): Unit =
        require(trainCars >= n, "Not enough train cars to place the amount given.")
        _trainCars -= n

  private val NUMBER_TRAIN_CARS = 45

  /** Create a player, with the given identifier and objective.
    *
    * @param playerId
    *   the player's id.
    * @param deck
    *   the reference to the deck.
    * @param trains
    *   the player's train cars.
    * @param objective
    *   the player's objective.
    * @return
    *   the player created.
    */
  def apply(playerId: PlayerId, deck: Deck = Deck(), trains: TrainCars = TrainCars(NUMBER_TRAIN_CARS),
      objective: Objective): Player =
    PlayerImpl(playerId, deck, trains, objective)

  private case class PlayerImpl(override val id: PlayerId, deck: Deck,
      override val trains: TrainCars, override val objective: Objective) extends Player:
    import scala.util.*

    override val hand: Hand = Hand(deck)
    private var _score: Int = 0

    override def drawCards(n: Int): Either[GameError, Unit] =
      Try(deck.draw(n)).toEither.left.map(_ => NotEnoughCardsInTheDeck).map(hand.addCards)

    export hand.canPlayCards

    override def playCards(color: Color, n: Int): Either[GameError, Unit] =
      require(n > 0, "n must be positive")
      Try(hand.playCards(color, n)).toEither.left.map(_ => NotEnoughCards).map(_.foreach(deck.reinsertAtTheBottom))

    override def placeTrains(n: Int): Either[GameError, Unit] = // TODO to review
      require(n > 0, "n must be positive")
      Try(trains.placeTrainCars(n)).toEither.left.map(_ => NotEnoughTrains)

    override def score: Int = _score

    override def addPoints(points: Int): Unit =
      require(points > 0, "points must be positive")
      _score += points
