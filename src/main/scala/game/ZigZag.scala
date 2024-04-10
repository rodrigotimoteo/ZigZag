package pt.iscte
package game

import game.GameUtilities.{generateNewBoard, initBoard}
import random.RandomImpl
import ui.TUI

import scala.annotation.tailrec

/**
 * Main entry point for the ZigZag word search game text user interface variant.
 * This object serves as the starting point for the application, initiating the text-based user interface.
 */
object ZigZag extends App {

  //Currently set to a defined seed
  val rand = RandomImpl(1)
  val fileName = "src/main/scala/words"

  TUI.displayWelcomeMessage()

  mainLoop()

  /**
   * Main loop method responsible for controlling the flow of the ZigZag word search game.
   * This method continuously displays the welcome message and menu options, allowing the player to interact
   * with the game through the text user interface.
   */
  @tailrec
  private def mainLoop(): Unit  = {
    TUI.displayWelcomeMessage()
    val option = TUI.displayMenu()

    option match {
      case "1" => {
        mainGameLoop()
      }
      case "2" => TUI.selectWord()
      case "3" => {
        println("Restarting game!")
        mainGameLoop()
      }
      case "4" => TUI.changeCharColor()
      case "5" => TUI.exitGame()
      case _   => println("Invalid option. Please select a valid one!")
    }
    mainLoop()
  }

  /**
   * Main loop method responsible for controlling the gameplay of the ZigZag word search game.
   * This method handles the logic for setting up the game board, displaying it, and processing player moves.
   */
  @tailrec
  private def mainGameLoop(): Unit = {
    val boardSize = TUI.askForBoardSize()
    val board = generateNewBoard(fileName, rand, initBoard(boardSize.toInt, boardSize.toInt))

    TUI.displayBoard(board._1)

    TUI.showInstructions()

    val (move, newRand) = TUI.decodeMove(TUI.askForMove())

    mainGameLoop()
  }

}
