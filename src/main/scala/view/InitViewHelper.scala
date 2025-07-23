package view

import scala.swing.*
import map.{CitiesLoader, MapView}
import player.{PlayerView, PlayerScoresView}

private case class InitViewHelper(frame: MainFrame, handPanel: BoxPanel, mapView: MapView, objectiveView: PlayerView,
    playerInfoView: PlayerView, playerScoresView: PlayerScoresView):

  import java.awt.Toolkit
  import ScrollPane.BarPolicy.*
  import controller.GameController

  private val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
  private val insets = Toolkit.getDefaultToolkit.getScreenInsets(frame.peer.getGraphicsConfiguration)
  private val panel = new BorderPanel()
  private val southPanel = new BoxPanel(Orientation.Horizontal)
  private val scrollPane = new ScrollPane(handPanel)
  private val eastPanel = new BoxPanel(Orientation.Vertical)
  private val drawButton = new Button("Draw")
  private val rulesButton = new Button("Show rules")

  private val gameController: GameController = GameController()

  def setFrame(): Unit =
    frame.title = "Ticket to ride"
    frame.contents = panel
    frame.size = new Dimension(screenSize.width - insets.left - insets.right,
      screenSize.height - insets.bottom - insets.top)
    frame.resizable = false

  def initPanels(): Unit =
    configDrawButton()
    configRulesButton()
    configSouthPanel()
    configEastPanel()
    panel.layout ++= List((mapView.component, BorderPanel.Position.Center),
      (southPanel, BorderPanel.Position.South), (eastPanel, BorderPanel.Position.East))
    initMap()
    frame.repaint()

  private def configSouthPanel(): Unit =
    import java.awt.Color.*
    import javax.swing.BorderFactory
    val borderWeight = 5
    val borderThickness = 1
    val borderColor = LIGHT_GRAY
    scrollPane.horizontalScrollBarPolicy = AsNeeded
    scrollPane.verticalScrollBarPolicy = Never
    scrollPane.border = Swing.CompoundBorder(
      Swing.EmptyBorder(borderWeight),
      BorderFactory.createLineBorder(borderColor, borderThickness, true)
    )
    southPanel.contents ++= List(rulesButton, scrollPane, drawButton)
    southPanel.border = Swing.EmptyBorder(borderWeight)

  private def configEastPanel(): Unit =
    val eastPanelWidthRatio = 0.15
    val objectiveViewHeightRatio = 0.25
    val playerScoresViewHeightRatio = 0.4
    val borderWeight = 1
    eastPanel.preferredSize = new Dimension((frame.size.width * eastPanelWidthRatio).toInt, frame.size.height)
    eastPanel.contents ++= List(playerInfoView.component, objectiveView.component, playerScoresView.component)
    eastPanel.border = Swing.EmptyBorder(borderWeight)
    objectiveView.component.preferredSize = new Dimension(eastPanel.preferredSize.width,
      (eastPanel.preferredSize.height * objectiveViewHeightRatio).toInt)
    playerScoresView.component.preferredSize = new Dimension(eastPanel.preferredSize.width,
      (eastPanel.preferredSize.height * playerScoresViewHeightRatio).toInt)

  import event.ButtonClicked
  private def configDrawButton(): Unit =
    drawButton.listenTo(drawButton.mouse.clicks)
    drawButton.reactions += {
      case _: ButtonClicked => gameController.drawCards(2)
      case _ => ()
    }

  private def configRulesButton(): Unit =
    rulesButton.listenTo(rulesButton.mouse.clicks)
    rulesButton.reactions += {
      case _: ButtonClicked => gameController.showRules()
      case _ => ()
    }

  private def initMap(): Unit =
    CitiesLoader(
      frame.size.width - eastPanel.peer.getPreferredSize.getWidth.toInt,
      frame.size.height - frame.peer.getInsets.top - southPanel.peer.getPreferredSize.getHeight.toInt
    )().load()
