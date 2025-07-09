package view.cards

import scala.swing.*

/** The representation of a player hand of train cards. It's possible to update it, remove or add some train cards. */
trait HandView:
  /** The hand view component.
    *
    * @return
    *   the hand view component.
    */
  def handComponent: Component

  /** Remove the train cards view given from the player hand representation.
    *
    * @param cards
    *   the list of components consisting in the train cards to remove from the player hand representation.
    */
  def removeCardsComponent(cards: List[CardView]): Unit

  /** Add the train cards view given to the player hand representation.
    *
    * @param cards
    *   the list of cards component to add to the player hand representation.
    */
  def addCardsComponent(cards: List[CardView]): Unit

  /** Reorder the train cards component of the player's hand, grouping them by color. */
  def groupCardsComponentByColor(): Unit

/** The factory for [[HandView]] instances. */
object HandView:
  /** Create a hand representation from the player hand given.
    *
    * @param views
    *   the player hand of train cards.
    * @return
    *   the hand representation.
    */
  def apply(views: List[CardView]): HandView = HandViewImpl(views)

  private case class HandViewImpl(private var _cardsComponent: List[CardView]) extends HandView:
    override def handComponent: Component =
      val panel = FlowPanel(FlowPanel.Alignment.Center)()
      panel.contents.++=:(_cardsComponent.map(_.cardComponent))
      panel

    override def removeCardsComponent(cards: List[CardView]): Unit = ???

    override def addCardsComponent(cards: List[CardView]): Unit =
      _cardsComponent = _cardsComponent :++ cards

    override def groupCardsComponentByColor(): Unit =
      _cardsComponent = _cardsComponent.groupBy(_.color).flatMap(_._2).toList
