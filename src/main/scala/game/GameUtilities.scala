package pt.iscte
package game

import random.RandomImpl

import scala.annotation.tailrec
import scala.io.Source

/**
 * Helper object containing utility methods for the ZigZag word search game.
 * This object provides various functions for game logic and board manipulation.
 */
object GameUtilities {

  /**
   * Type used to represent a game board
   */
  type Board = List[List[Char]]

  /**
   * Type used to represent a 2 dimension location (coordinates)
   */
  type Coord2D = (Int, Int)

  /**
   * Private type alias representing a mapping of words to a list of 2D coordinates.
   */
  private type WordKeeper = Map[String, List[Coord2D]]

  /**
   * Represents the maximum expected time that a game should take (to compute score)
   */
  private val maxTimeScore = 6000

  /**
   * Initializes the WordKeeper map by reading data from a file.
   * The method reads the specified file and constructs a WordKeeper map, where
   * each line in the file represents a word followed by its associated 2D coordinates.
   *
   * @param filename The name or path of the file to read the word-coordinate data from.
   * @return The WordKeeper map containing the words and their associated coordinates.
   * @throws java.io.IOException if there is an error reading the file.
   */
  private def initializeWordKeeper(filename: String): WordKeeper = {
    val source = Source.fromFile(filename)

    try {
      val lines = source.getLines().map { line =>
        val parts = line.split(" ")
        val word = parts.head
        val coordinates = parts.tail.grouped(2).toList.map { case Array(x, y) => (x.toInt, y.toInt) }

        (word, coordinates)
      }.toMap

      lines
    } finally {
      source.close()
    }
  }

  /**
   * Generates a random character from 'A' to 'Z' using the provided RandomImpl instance.
   *
   * @param rand the RandomImpl instance used to generate random numbers.
   * @return a tuple containing a random character from 'A' to 'Z' and the updated RandomImpl instance.
   */
  private def randomChar(rand: RandomImpl): (Char, RandomImpl) = {
    val (randInt, newRand) = rand.nextInt(26)
    val randChar = (randInt + 'A'.toInt).toChar

    (randChar, newRand)
  } //T1 completed

  /**
   * Initializes a new board with the specified number of rows and columns, filled with a default character.
   *
   * @param rows    the number of rows for the board
   * @param cols    the number of columns for the board
   * @return a new board initialized with the specified size and filled with the default character a start (*)
   */
  def initBoard(rows: Int, cols: Int): Board = {
    List.fill(rows)(List.fill(cols)('*'))
  }

  /**
   * Fills one cell of the board with the given letter at the specified coordinates.
   *
   * @param board  the game board
   * @param char   the letter to fill the cell with
   * @param coordinate  the coordinates of the cell to fill
   * @return a new board with the specified cell filled with the given letter
   */
  private def fillOneCell(board: Board, char: Char, coordinate: Coord2D): Board = {
    val (xValue, yValue) = coordinate
    val updatedRow = board(yValue).updated(xValue, char)

    board.updated(yValue, updatedRow)
  } //T2 completed

  /**
   * Sets the given words on the specified positions on the game board.
   *
   * @param board      the game board on which to set the words
   * @param words      the list of words to set on the board
   * @param positions  the list of positions where each word should be placed
   * @return the updated game board with the words set at the specified positions
   */
  private def setBoardWithWords(board: Board, words: List[String], positions: List[List[Coord2D]]): Board = {

    @tailrec
    def updateBoard(board: Board, remainingWords: List[String], remainingPositions: List[List[Coord2D]], updatedBoard: Board): Board = {
      (remainingWords, remainingPositions) match {
        case (Nil, _) | (_, Nil) => updatedBoard
        case (word :: tailWords, positions :: tailPositions) =>
          val charPositionPairs = word.zip(positions)

          val updatedBoardWithWord = charPositionPairs.foldLeft(updatedBoard) { case (accBoard, (char, coordinate)) =>
            fillOneCell(accBoard, char, coordinate)
          }

          updateBoard(board, tailWords, tailPositions, updatedBoardWithWord)
      }
    }

    updateBoard(board, words, positions, board)
  } //T3 completed

  /**
   * Completes the game board randomly using a provided function.
   *
   * @param board the initial game board
   * @param rand  the RandomImpl instance to generate random numbers
   * @param f     the function used to generate random characters and update the random number generator
   * @return a tuple containing the updated game board and the updated RandomImpl instance
   */
  private def completeBoardRandomly(board: Board, rand: RandomImpl, f: RandomImpl => (Char, RandomImpl)): (Board, RandomImpl) = {
    val emptyCells = findEmptyCells(board)
    val (updatedBoard, updatedRand) = emptyCells.foldLeft((board, rand)) { case ((accBoard, accRand), coordinate) =>
      val (randomChar, newRand) = f(accRand)
      (fillOneCell(accBoard, randomChar, coordinate), newRand)
    }
    (updatedBoard, updatedRand)
  } //T4 completed

  /**
   * Finds the coordinates of empty cells on the given game board.
   *
   * @param board the game board to search for empty cells
   * @return a list of coordinates (Coord2D) representing the empty cells on the board
   */
  private def findEmptyCells(board: Board): List[Coord2D] = {
    board.zipWithIndex.flatMap { case (row, rowIndex) =>
      row.zipWithIndex.collect {
        case (cell, colIndex) if cell == '*' => (colIndex, rowIndex)
      }
    }
  }

  /**
   * Generates a new game board based on a file containing words and their positions, completing it randomly.
   *
   * @param filename the name of the file containing words and their positions
   * @param rand     the RandomImpl instance to generate random numbers
   * @param board    the initial game board
   * @return the updated game board with words set at specified positions and completed randomly
   */
  def generateNewBoard(filename: String, rand: RandomImpl, board: Board): (Board, RandomImpl) = {
   val wordKeeper = initializeWordKeeper(filename)

    val (updatedBoard, updatedRand) = completeBoardRandomly(
      setBoardWithWords(board, wordKeeper.keys.toList, wordKeeper.values.toList), rand, randomChar)

    (updatedBoard, updatedRand)
  }

  /**
   * Plays the word search game starting from a given position and direction.
   *
   * This method checks if the provided start position and direction are valid for the game board
   * and if the first character of the current word matches the character at the start position.
   * If any of these conditions fail, the method returns (false, Set.empty).
   *
   * If the conditions are met, the method recursively searches for the complete word on the board,
   * moving in the specified direction. It returns `(true, visited)` if the word is found, where `visited`
   * is a set of coordinates that were visited during the search.
   *
   * @param board The game board represented as a 2D list of characters.
   * @param start The starting position (row, column) for the word search.
   * @param dir The direction of the word search (e.g., North, South, East, West, etc.).
   * @return A tuple containing a boolean indicating whether the word was found (true for found, false for not found)
   *         and a set of coordinates that were visited during the search.
   */
  def play(board: Board, start: Coord2D, dir: Direction.Direction, currentWord: String): (Boolean, Set[Coord2D]) = {
    if(!isWithinBounds(start, board) || board(start._1)(start._2) != currentWord(0))
      return (false, Set.empty)

    val direction = (Direction.horizontalComponent(dir), Direction.verticalComponent(dir))
    val coordinate  = sumCoordinate(start, direction)

    if(!isWithinBounds(coordinate, board)) return (false, Set.empty)

    @tailrec
    def searchWord(board: Board, coordinates: List[Coord2D], index: Int, visited: Set[Coord2D]): (Boolean, Set[Coord2D])  = {
      if (index == currentWord.length) return (true, visited)
      if (coordinates.isEmpty)  return (false, Set.empty)

      val matchingCharCoordinates = coordinates.filter { newCoordinates: Coord2D =>
        val newCoordinate = newCoordinates
        board(newCoordinate._2)(newCoordinate._1) == currentWord(index)
      }

      val adjacentCoordinates = matchingCharCoordinates.flatMap { matchingCoordinate =>
        getAdjacentValidPositions(matchingCoordinate, board).filter(!visited.contains(_))
      }

      searchWord(board, adjacentCoordinates, index + 1, visited ++ matchingCharCoordinates.toSet)
    }

    val (foundWord, visitedCoords) = searchWord(board, List(coordinate), 1, Set(start))

    if(foundWord) {

    }

    (foundWord, visitedCoords)
  } //T5 Completed

  /**
   * Retrieves a list of adjacent valid positions (up, down, left, right, and diagonals) for a given coordinate on the board.
   *
   * @param coordinate The coordinate (x, y) for which adjacent positions are to be found.
   * @param board The game board represented as a two-dimensional list of characters.
   * @return A list of adjacent valid coordinates within the bounds of the board.
   */
  private def getAdjacentValidPositions(coordinate: Coord2D, board: Board): List[Coord2D] = {
    List(
      //NorthWest Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.NorthWest),
        coordinate._2 + Direction.verticalComponent(Direction.NorthWest)),

      //North Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.North),
        coordinate._2 + Direction.verticalComponent(Direction.North)),

      //NorthEast Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.NorthEast),
        coordinate._2 + Direction.verticalComponent(Direction.NorthEast)),

      //East Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.East),
        coordinate._2 + Direction.verticalComponent(Direction.East)),

      //SouthEast Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.SouthEast),
        coordinate._2 + Direction.verticalComponent(Direction.SouthEast)),

      //South Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.South),
        coordinate._2 + Direction.verticalComponent(Direction.South)),

      //SouthWest Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.SouthWest),
        coordinate._2 + Direction.verticalComponent(Direction.SouthWest)),

      //West Coordinate
      (coordinate._1 + Direction.horizontalComponent(Direction.West),
        coordinate._2 + Direction.verticalComponent(Direction.West)),
    ).filter { newCoordinate: Coord2D =>
      isWithinBounds(newCoordinate, board)
    }
  }

  /**
   * Checks if a given coordinate is within the bounds of the game board.
   *
   * @param coordinate The coordinate (x, y) to be checked.
   * @param board The game board represented as a two-dimensional list of characters.
   * @return True if the coordinate is within the bounds of the board, false otherwise.
   */
  private def isWithinBounds(coordinate: Coord2D, board: Board): Boolean = {
    coordinate._1 >= 0 && coordinate._1 < board.head.length && coordinate._2 >= 0 && coordinate._2 < board.length
  }

  /**
   * Sums two coordinates to calculate a new coordinate.
   *
   * This method takes two coordinates and calculates their sum by adding their respective components.
   * The result is a new coordinate representing the sum of the two input coordinates.
   *
   * @param coordinate1 The first coordinate represented as a tuple (x1, y1).
   * @param coordinate2 The second coordinate represented as a tuple (x2, y2).
   * @return The sum of the two input coordinates as a new coordinate represented as a tuple (x1 + x2, y1 + y2).
   */
  private def sumCoordinate(coordinate1: Coord2D, coordinate2: Coord2D): Coord2D = {
    (coordinate1._1 + coordinate2._1, coordinate1._2 + coordinate2._2)
  }

  /**
   * Validates the game board by checking that it contains all the specified words, ensuring they are valid and
   * non-duplicated.
   *
   * @param board The game board to be validated.
   * @return True if the board is valid, false otherwise.
   */
  def checkBoard(board: Board): Boolean = {
    true
  } //T6 TODO

  /**
   * Determines whether the game is finished or not.
   *
   * @return true if the game is finished, false otherwise.
   */
  def isGameFinished: Boolean = {
    false
  }

  /**
   * Computes the score based on the elapsed time.
   *
   * @param time The elapsed time in milliseconds.
   * @return The computed score.
   */
  def computeScore(time: Long): Int = {
    val score = (maxTimeScore - (time / 1000)).toInt

    score
  } //T7

}
