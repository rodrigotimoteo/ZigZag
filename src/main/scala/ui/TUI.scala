package pt.iscte
package ui

import game.GameUtilities.{Board, Coord2D}

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object TUI {

  /**
   * Represents a space character used for spacing in text user interface.
   */
  private val space = " "

  /**
   * Represents a horizontal slash character used for creating horizontal borders in text user interface.
   */
  private val horizontalSlash = "-"

  /**
   * Represents a vertical slash character used for creating vertical borders in text user interface.
   */
  private val verticalSlash = "|"

  /**
   * Reads user input from the standard input stream, trims leading and trailing whitespace,
   * and converts the input to uppercase.
   *
   * @return the user input as a trimmed and uppercase string.
   */
  def getUserInput: String = readLine.trim.toUpperCase


  /**
   * Displays the board in a text user interface format with horizontal and vertical borders,
   * and letters separated by spaces.
   *
   * @param board the game board to display
   */
//  @tailrec
//  def displayBoard(board: Board): Unit = {
//
//  }

  /**
   * Displays a welcome message for the ZigZag Scala game.
   */
  def displayWelcomeMessage(): Unit = {
    println("Welcome to ZigZag Scala!\n")
  }

  /**
   * Displays the main menu options for the ZigZag Scala game.
   * Asks the user to select an option and returns the user input.
   *
   * @return the user input as a string representing the selected option.
   */
  def displayMenu(): String = {
    println("1 - Start Game")
    println("2 - Settings\n")

    print("Select one option: ")
    getUserInput
  }

  /**
   * Asks the user to enter the desired board size for the ZigZag Scala game.
   *
   * @return the user input as a string representing the desired board size.
   */
  def askForBoardSize(): String = {
    print("Enter the board size: ")
    getUserInput
  }

  def showInstructions(): Unit = {
    println("To play enter the move in the format [Row] [Column], or Exit to close the game")
    println("Press [ENTER] to continue")
    getUserInput
  }

  def askForMove(): String = {
    print("Enter a new move")
    getUserInput
  }

  def decodeMove(move: String): Coord2D = {
    if (move == "EXIT") System.exit(0)
    move.split(" ").toList match {
      case List(xValue, yValue) =>
        if ((xValue forall Character.isDigit) && (yValue forall Character.isDigit)) (xValue.toInt, yValue.toInt)
        else (-1, -1)
      case _ => invalidMove
    }


  }


}
