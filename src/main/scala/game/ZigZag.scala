package pt.iscte
package game

import game.GameUtilities.{generateNewBoard, initBoard}
import random.RandomImpl
import ui.TUI

import scala.annotation.tailrec

object ZigZag extends App {
//  val startBoard = createBoardCompact(asksBoardSize().toInt)
//  val rand = MyRandom(11)
//  val cells = asksPlayerColor()
//
//  val startGameState = GameState(startBoard,cells,rand)
//  val game = Game(List(startGameState),cells, false, false)
//
//  showExampleDecision()
//
//  mainLoop(game)
//
//  @tailrec
//  def mainLoop(game: Game): Unit = {
//
//    showPrompt()
//
//    val decision = playerDecision(getUserInput())
//
//    if (decision != undo) {
//      // Player's Turn
//      val playerBoard = askAndPlay(game.gs.head, decision._1, decision._2)
//      val playerState = GameState(playerBoard,game.cells,game.gs.head.random)
//      // Computer's Turn
//      val (newBoard, newRandom, _) = playerState.playIA()
//
//      val newGame = game.addBoard(newBoard, newRandom)
//      printBoard(newBoard)
//
//      // Next Turn or Game Over
//      val (gameWon) = printGameWon(newBoard)
//      if (!gameWon) mainLoop(newGame)
//    }
//    // Return to previous Board
//    else {
//      mainLoop(game.undo())
//    }
//  }
  //Currently set to a defined seed
  val rand = RandomImpl(1)
  val fileName = "src/main/scala/words"

  TUI.displayWelcomeMessage()

  private val boardSize = TUI.askForBoardSize()
  var board = generateNewBoard(fileName, rand, initBoard(boardSize.toInt, boardSize.toInt))

  TUI.showInstructions()

  mainLoop()

  @tailrec
  private def mainLoop(): Unit = {
    val (move, newRand) = TUI.decodeMove(TUI.askForMove())

    mainLoop()
  }

}
