package model.player

/** The train cars, which are the number owned by the player with the possibility to place some of them. */
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

object TrainCars:
  def apply(numberTrainCars: Int): TrainCars = TrainCarsImpl(numberTrainCars)

  private case class TrainCarsImpl(private var _trainCars: Int) extends TrainCars:
    override def trainCars: Int = _trainCars

    override def placeTrainCars(n: Int): Unit =
      require(trainCars >= n, "Not enough train cars to place the amount given.")
      _trainCars -= n
