package model.player

import model.cards.{Deck, Hand}
import model.map.Route

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

  import model.map.Route

  /** Claim the given route of the map.
    *
    * @param route
    *   the route that the player wants to claim.
    */
  def claimRoute(route: Route): Unit

/** The factory for [[Player]] instances. */
object Player:
  /** The train cars, which are the number owned by the player with the possibility to set it or place some of them. */
  trait TrainCars:
    /** Number of player train cars left.
      *
      * @return
      *   the number of train cars left.
      */
    def trainCars: Int

    /** Set the number of player train cars to the amount given.
      *
      * @param n
      *   the amount to train cars to set.
      */
    def trainCars_=(n: Int): Unit

    /** Place the given amount of player train cars, if they are sufficient.
      *
      * @param n
      *   the number of train cars to place.
      */
    def placeTrainCars(n: Int): Unit

  private object TrainCars:
    def apply(numberTrainCars: Int): TrainCars = TrainCarsImpl(numberTrainCars)

    private case class TrainCarsImpl(private var _trainCars: Int) extends TrainCars:
      def trainCars: Int = _trainCars

      def trainCars_=(n: Int): Unit = _trainCars = n

      def placeTrainCars(n: Int): Unit =
        require(trainCars >= n, "Not enough train cars to place the amount given.")
        trainCars -= n

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
    override val objective: Objective = "" // TODO
    override val hand: Hand = Hand(deck)

    override def drawCards(n: Int): Unit = hand.addCards(deck.draw(n))

    override def claimRoute(route: Route): Unit = ???
