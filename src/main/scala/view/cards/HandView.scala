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
    override def handComponent: Component =
      val panel = FlowPanel(FlowPanel.Alignment.Center)()
      panel.contents.++=:(_cardComponents)
      panel

    override def updateHandComponent(hand: Hand): Unit =
      _cardComponents = hand.cards.map(CardView().cardComponent(_))

    override def removeCardComponents(cards: List[Component]): Unit = ???

    override def addCardComponents(cards: List[Card]): Unit = ???
