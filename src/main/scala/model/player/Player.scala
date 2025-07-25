package model.player

import model.cards.{Deck, Hand}
import model.utils.{Color, PlayerColor, GameError}

type PlayerId = PlayerColor

/** A player, that has an id, an objective, a hand of train cards, a number of train cars and his score. The player can
  * draw cards, play cards, place train cars, add points to his actual score and check the possibility to play an amount
  * of cards of a given color.
  */
trait Player:
  import Player.Trains

  /** The player's identifier.
    *
    * @return
    *   the player's identifier.
    */
  def id: PlayerId

  /** The player's objective with completion features.
    *
    * @return
    *   the player's objective.
    */
  def objective: ObjectiveCompletion

  /** The player's train cards hand.
    *
    * @return
    *   the player's hand.
    */
  def hand: Hand

  /** The player's number of train cars.
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

  /** Draw the given amount of cards from the deck and put them in the player's hand if it's possible, otherwise return
    * a [[GameError]].
    *
    * @param n
    *   the number of cards to draw.
    * @return
    *   `Right(())` if the action succeeds, `Left(NotEnoughCardsInTheDeck)` if there are not enough cards in the deck.
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
  private type Trains = Int

  /** Error that represents the case in which the deck doesn't have enough cards. */
  case object NotEnoughCardsInTheDeck extends GameError

  /** Error that represents the case in which a player doesn't have enough cards.
    */
  case object NotEnoughCards extends GameError

  /** Error that represents the case in which a player doesn't have enough trains.
    */
  case object NotEnoughTrains extends GameError

  /** Create a player, with the given identifier and objective.
    *
    * @param playerId
    *   the player's id.
    * @param deck
    *   the reference to the deck, otherwise it creates a new standard Deck.
    * @param objective
    *   the player's objective.
    * @return
    *   the player created.
    */
  def apply(playerId: PlayerId, deck: Deck = Deck(), objective: ObjectiveCompletion): Player =
    PlayerImpl(playerId, deck, objective)

  private case class PlayerImpl(override val id: PlayerId, deck: Deck, override val objective: ObjectiveCompletion)
      extends Player:

    import scala.util.*
    import config.GameConfig.NumberTrainCars

    private val trainCars: TrainCars = TrainCars(NumberTrainCars)
    override val hand: Hand = Hand(deck)
    private var _score: Int = 0

    override def trains: Trains = trainCars.trainCars

    override def drawCards(n: Int): Either[GameError, Unit] =
      Try(deck.draw(n)).toEither.left.map(_ => NotEnoughCardsInTheDeck).map(hand.addCards)

    export hand.canPlayCards

    override def playCards(color: Color, n: Int): Either[GameError, Unit] =
      require(n > 0, "n must be positive")
      Try(hand.playCards(color, n)).toEither.left.map(_ => NotEnoughCards).map(_.foreach(deck.reinsertAtTheBottom))

    override def placeTrains(n: Int): Either[GameError, Unit] =
      require(n > 0, "n must be positive")
      Try(trainCars.placeTrainCars(n)).toEither.left.map(_ => NotEnoughTrains)

    override def score: Int = _score

    override def addPoints(points: Int): Unit =
      require(points > 0, "points must be positive")
      _score += points
