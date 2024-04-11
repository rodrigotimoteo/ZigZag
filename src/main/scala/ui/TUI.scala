package pt.iscte
package ui

import game.GameUtilities.{Board, Coord2D}

import game.Direction
import game.Direction.Direction

import scala.annotation.tailrec
import scala.io.StdIn.readLine

/**
 * An object containing ANSI escape sequences for various colors.
 * These escape sequences can be used to format text printed to the console
 * in different colors.
 */
object Colors {
  /**
   * ANSI escape sequence for the color red.
   */
  val RED = "\u001b[31m"

  /**
   * ANSI escape sequence for the color green.
   */
  val GREEN = "\u001b[32m"

  /**
   * ANSI escape sequence for the color yellow.
   */
  val YELLOW = "\u001b[33m"

  /**
   * ANSI escape sequence for the color blue.
   */
  val BLUE = "\u001b[34m"

  /**
   * ANSI escape sequence for the color magenta.
   */
  val MAGENTA = "\u001b[35m"

  /**
   * ANSI escape sequence for the color cyan.
   */
  val CYAN = "\u001b[36m"

  /**
   * ANSI escape sequence for the color white.
   */
  val WHITE = "\u001b[37m"
}

/**
 * An object containing text user interface utilities for the word search game.
 * These utilities include methods for displaying messages, getting user input,
 * and formatting the game interface.
 */
object TUI {

  private var selectedColor = Colors.WHITE

  /**
   * Represents a space character used for spacing in text user interface.
   */
  private val space = " "

  /**
   * Represents a invalid position on the game logic
   */
  private val invalidPosition = (-1, -1)

  /**
   * Reads user input from the standard input stream, trims leading and trailing whitespace,
   * and converts the input to uppercase.
   *
   * @return the user input as a trimmed and uppercase string.
   */
  private def getUserInput: String = readLine.trim.toUpperCase


  /**
   * Displays the board in a text user interface format with horizontal and vertical borders,
   * and letters separated by spaces.
   *
   * @param board the game board to display
   */
  def displayBoard(board: Board): Unit = {
    board.foreach(row => println(row.mkString(space + space)))
  }

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
    println("2 - Change Color")
    println("3 - Exit\n")

    print("Select one option: ")
    getUserInput
  }

  /**
   * Changes the color of the select characters I guess?
   */
  def changeCharColor(): Unit = {
    println("1 - Red")
    println("2 - Green")
    println("3 - Yellow")
    println("4 - Blue")
    println("5 - Magenta")
    println("6 - Cyan")
    println("7 - White\n")

    print("Select a new Color: ")
    val color = getUserInput

    color match {
      case "1" => selectedColor = Colors.RED
      case "2" => selectedColor = Colors.GREEN
      case "3" => selectedColor = Colors.YELLOW
      case "4" => selectedColor = Colors.BLUE
      case "5" => selectedColor = Colors.MAGENTA
      case "6" => selectedColor = Colors.CYAN
      case "7" => selectedColor = Colors.WHITE
    }
  }

  /**
   * Exits the game.
   */
  def exitGame(): Unit = {
    println("Exiting Game! See you next time!")
    System.exit(0)
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

  /**
   * Displays the Play Menu and prompts the user to select an option.
   *
   * @return The user's selected option as a string.
   */
  def askPlayMenu(): String = {
    println("Welcome to the Play Menu")
    println("1 - Make a move")
    println("2 - Select a word")
    println("3 - Restart game")
    println("4 - Exit the game")
    print("Please, select an option: ")

    getUserInput
  }

  /**
   * Displays the game instructions to the user.
   */
  def showInstructions(): Unit = {
    println("To play you need to type a selected word, a starting position to search and a direction for the search")
  }

  /**
   * Displays the word selection prompt and get a word from the user
   *
   * @return word given by the user
   */
  def askWordPrompt(): String = {
    print("Please selected a word to search: ")
    getUserInput
  }

  /**
   * Displays the direction selection prompt and gets a direction from the user
   *
   * @return direction given by the user
   */
  @tailrec
  def askDirectionPrompt(): Direction = {
    println("Please select a direction from the following")
    println("1 - NorthWest")
    println("2 - North")
    println("3 - NorthEast")
    println("4 - East")
    println("5 - SouthEast")
    println("6 - South")
    println("7 - SouthWest")
    println("8 - West")
    print("Your option: ")

    val direction = getUserInput

    direction match {
      case "1" => Direction.NorthWest
      case "2" => Direction.North
      case "3" => Direction.NorthEast
      case "4" => Direction.East
      case "5" => Direction.SouthEast
      case "6" => Direction.South
      case "7" => Direction.SouthWest
      case "8" => Direction.West
      case _ =>
        println("Invalid option! Asking again!")
        askDirectionPrompt()

    }
  }

  /**
   * Prompts the user to enter a new position to begin the search.
   *
   * @return The user's input as a string representing the move.
   */
  def askForMove(): String = {
    print("Enter a position: ")
    getUserInput
  }

  /**
   * Decodes a user move string into a 2D coordinate.
   *
   * @param move The user move string in the format "x y" or "EXIT" to exit the program.
   * @return The decoded 2D coordinate.
   */
  def decodeMove(move: String): Coord2D = {
    if (move == "EXIT") System.exit(0)
    move.split(" ").toList match {
      case List(xValue, yValue) =>
        if ((xValue forall Character.isDigit) && (yValue forall Character.isDigit)) (xValue.toInt, yValue.toInt)
        else invalidPosition
      case _ => invalidPosition
    }
  }

}
