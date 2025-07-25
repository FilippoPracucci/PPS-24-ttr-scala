package view

import scala.swing.*
import map.{CitiesLoader, MapView}
import player.{PlayerView, PlayerScoresView}

private case class InitViewHelper(frame: MainFrame, handPanel: BoxPanel, mapView: MapView, objectiveView: PlayerView,
    playerInfoView: PlayerView, playerScoresView: PlayerScoresView):

  import java.awt.Toolkit
  import ScrollPane.BarPolicy.*
  import controller.GameController
  import config.GameViewConfig.*

  private val screenSize: Dimension = Toolkit.getDefaultToolkit.getScreenSize
  private val insets = Toolkit.getDefaultToolkit.getScreenInsets(frame.peer.getGraphicsConfiguration)
  private val panel = new BorderPanel()
  private val southPanel = new BoxPanel(Orientation.Horizontal)
  private val scrollPane = new ScrollPane(handPanel)
  private val eastPanel = new BoxPanel(Orientation.Vertical)
  private val drawButton = new Button(DrawButtonText)
  private val rulesButton = new Button(RulesButtonText)

  private val gameController: GameController = GameController()

  def setFrame(): Unit =
    frame.title = FrameTitle
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
    import config.GameViewConfig.BorderConfig.*
    val BorderWeight = 5
    val BorderThickness = 1
    scrollPane.horizontalScrollBarPolicy = AsNeeded
    scrollPane.verticalScrollBarPolicy = Never
    scrollPane.border = CompoundBorder(EmptyBorder(BorderWeight), LineBorder(BorderThickness))
    southPanel.contents ++= List(rulesButton, scrollPane, drawButton)
    southPanel.border = Swing.EmptyBorder(BorderWeight)

  private def configEastPanel(): Unit =
    val EastPanelWidthRatio = 0.15
    val ObjectiveViewHeightRatio = 0.25
    val PlayerScoresViewHeightRatio = 0.4
    val BorderWeight = 1
    eastPanel.preferredSize = new Dimension((frame.size.width * EastPanelWidthRatio).toInt, frame.size.height)
    eastPanel.contents ++= List(playerInfoView.component, objectiveView.component, playerScoresView.component)
    eastPanel.border = Swing.EmptyBorder(BorderWeight)
    objectiveView.component.preferredSize = new Dimension(eastPanel.preferredSize.width,
      (eastPanel.preferredSize.height * ObjectiveViewHeightRatio).toInt)
    playerScoresView.component.preferredSize = new Dimension(eastPanel.preferredSize.width,
      (eastPanel.preferredSize.height * PlayerScoresViewHeightRatio).toInt)

  import event.ButtonClicked
  private def configDrawButton(): Unit =
    drawButton.listenTo(drawButton.mouse.clicks)
    drawButton.reactions += {
      case _: ButtonClicked => gameController.drawCards()
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
    )()(using mapView).load()
