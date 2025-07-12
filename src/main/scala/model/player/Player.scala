package model.player

import model.cards.{Deck, Hand}
import model.utils._

type PlayerId = model.utils.PlayerColor
type Objective = String //TODO

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

  /** The reference to the deck of cards.
    *
    * @return
    *   the reference to the deck of cards.
    */
  def deck: Deck // reference to the deck

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

  /** Draw the given amount of cards from the deck and put them in the player's hand.
    *
    * @param n
    *   the number of cards to draw.
    */
  def drawCards(n: Int): Unit

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

/** The factory for [[Player]] instances. */
object Player:
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

  /** Create a player, with the given identifier.
    *
    * @param playerId
    *   the player's id.
    * @param deck
    *   the reference to the deck.
    * @param trains
    *   the player's train cars.
    * @return
    *   the player created.
    */
  def apply(playerId: PlayerId, deck: Deck = Deck(), trains: TrainCars = TrainCars(NUMBER_TRAIN_CARS)): Player =
    PlayerImpl(playerId, deck, trains)

  private case class PlayerImpl(override val id: PlayerId, override val deck: Deck,
      override val trains: TrainCars) extends Player:
    import scala.util._

    override val objective: Objective = "" // TODO
    override val hand: Hand = Hand(deck)

    override def drawCards(n: Int): Unit = hand.addCards(deck.draw(n))

    export hand.canPlayCards

    override def playCards(color: Color, n: Int): Either[GameError, Unit] =
      require(n > 0, "n must be positive")
      Try(hand.playCards(color, n)).toEither.left.map(_ => NotEnoughCards)

    override def placeTrains(n: Int): Either[GameError, Unit] = // TODO to review
      require(n > 0, "n must be positive")
      Try(trains.placeTrainCars(n)).toEither.left.map(_ => NotEnoughTrains)
