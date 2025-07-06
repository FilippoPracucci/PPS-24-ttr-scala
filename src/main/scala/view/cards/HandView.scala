package view.cards

import model.cards.{Card, Hand}
import scala.swing.*

/** The representation of a player hand of train cards. It's possible to update it, remove or add some train cards. */
trait HandView:
  /** The hand view component.
    *
    * @return
    *   the hand view component.
    */
  def handComponent: Component

  /** Remove the train cards given from the player hand representation.
    *
    * @param cards
    *   the list of components consisting in the train cards to remove from the player hand representation.
    */
  def removeCardsComponent(cards: List[Component]): Unit

  /** Add the train cards given to the player hand representation.
    *
    * @param cards
    *   the list of train cards to add to the player hand representation.
    */
  def addCardsComponent(cards: List[Card]): Unit

/** The factory for [[HandView]] instances. */
object HandView:
  /** Create a hand representation from the player hand given.
    *
    * @param hand
    *   the player hand of train cards.
    * @return
    *   the hand representation.
    */
  def apply(hand: Hand): HandView = HandViewImpl(hand.cards.map(CardView().cardComponent))

  private case class HandViewImpl(private var _cardComponents: List[Component]) extends HandView:
    override def handComponent: Component =
      val panel = FlowPanel(FlowPanel.Alignment.Center)()
      panel.contents.++=:(_cardComponents)
      panel

    override def removeCardsComponent(cards: List[Component]): Unit = ???

    override def addCardsComponent(cards: List[Card]): Unit =
      _cardComponents = _cardComponents :++ cards.map(CardView().cardComponent)
