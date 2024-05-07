package pt.iscte
package game

import game.GameUtilities.{Board, generateNewBoard, initBoard}
import random.RandomImpl
import ui.TUI

import scala.annotation.tailrec

/**
 * Main entry point for the ZigZag word search game text user interface variant.
 * This object serves as the starting point for the application, initiating the text-based user interface.
 */
object ZigZag extends App {

  //Currently set to a defined seed
  val rand = RandomImpl(2)
  val fileName = "src/main/scala/words"

  private var startTime: Long = 0

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
      case "1" =>
        val boardSize = TUI.askForBoardSize()
        val board = generateNewBoard(fileName, rand, initBoard(boardSize.toInt, boardSize.toInt))

        val newGameState = GameState(board._1, board._2)
        val newGame = Game(newGameState, Set.empty)

        TUI.displayBoard(newGame)
        TUI.showInstructions()

        mainGameLoop(newGame)
        startTime = System.currentTimeMillis
      case "2" => TUI.changeCharColor()
      case "3" =>
        val fileName = TUI.showLoadInstructions()
        Game.generateGameFromSave(fileName) match {
          case Some(savedGame) =>
            TUI.displayBoard(savedGame)
            TUI.showInstructions()

            mainGameLoop(savedGame)
            startTime = System.currentTimeMillis

          case None =>
            println("Invalid file try again!")
        }
      case "4" => TUI.exitGame()
      case _   => println("Invalid option. Please select a valid one!")
    }
    mainLoop()
  }

  /**
   * Main loop method responsible for controlling the gameplay of the ZigZag word search game.
   * This method handles the logic for setting up the game board, displaying it, and processing player moves.
   */
  private def mainGameLoop(game: Game): Unit = {
    val option = TUI.askPlayMenu()

    option match {
      case "1" =>
        val searchWord = TUI.askWordPrompt()

        if(searchWord.nonEmpty) {
          val startPosition = TUI.decodeMove(TUI.askForMove())
          val direction = TUI.askDirectionPrompt()

          val result = game.gameState.play(startPosition, direction, searchWord)

          if(result._1) {
            println("Word found!")

            val newGame = game.updateWordCoords(result._2)
            TUI.displayBoard(newGame)

            if(GameUtilities.isGameFinished) {
              println("\nGame Finished!")

              val score = GameUtilities.computeScore(System.currentTimeMillis() - startTime)
              println("Your score was " + score + "!\n")

              mainLoop()
            }

            mainGameLoop(newGame)
          } else {
            TUI.displayBoard(game)
          }
        } else
          println("Please provide a valid string!")
      case "2" => mainLoop()
      case "3" =>
        val filename = TUI.askForFileName()
        game.writeToFile(filename)
      case "4" => TUI.exitGame()
    }

    mainGameLoop(game)
  }

}
