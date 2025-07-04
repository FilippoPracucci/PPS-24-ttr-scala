package view.cards

import model.cards.{Card, Hand}
import scala.swing.*

/** The representation of a player hand of train cards. It's possible to update it, remove or add some train cards. */
trait HandView:
  /** The hand view component, that consists in a list of components.
    *
    * @return
    *   the list of components composing the hand representation.
    */
  def handComponent: List[Component]

  /** Set the hand component with the list of components given.
    *
    * @param cards
    *   the list of components representing the cards of the player hand.
    */
  def handComponent_=(cards: List[Component]): Unit

  /** Update the hand component with the player hand given.
    *
    * @param hand
    *   the hand to update the component.
    */
  def updateHandComponent(hand: Hand): Unit

  /** Remove the train cards given from the player hand representation.
    *
    * @param cards
    *   the list of components consisting in the train cards to remove from the player hand representation.
    */
  def removeCardComponents(cards: List[Component]): Unit

  /** Add the train cards given to the player hand representation.
    *
    * @param cards
    *   the list of train cards to add to the player hand representation.
    */
  def addCardComponents(cards: List[Card]): Unit

/** The factory for [[HandView]] instances. */
object HandView:
  /** Create a hand representation from the player hand given.
    *
    * @param hand
    *   the player hand of train cards.
    * @return
    *   the hand representation.
    */
  def apply(hand: Hand): HandView = HandViewImpl(hand.cards.map(CardView().cardComponent(_)))

  private case class HandViewImpl(private var _cardComponents: List[Component]) extends HandView:
    override def handComponent: List[Component] = _cardComponents

    override def handComponent_=(cards: List[Component]): Unit = _cardComponents = cards

    override def updateHandComponent(hand: Hand): Unit =
      handComponent = hand.cards.map(CardView().cardComponent(_))

    override def removeCardComponents(cards: List[Component]): Unit = ???

    override def addCardComponents(cards: List[Card]): Unit = ???
