package pt.iscte
package game

import random.RandomImpl

import scala.annotation.tailrec
import scala.io.Source

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
   * Generates a random character from 'A' to 'Z' using the provided RandomImpl instance.
   *
   * @param rand the RandomImpl instance used to generate random numbers.
   * @return a tuple containing a random character from 'A' to 'Z' and the updated RandomImpl instance.
   */
  private def randomMove(rand: RandomImpl): (Char, RandomImpl) = {
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
   * @param coord  the coordinates of the cell to fill
   * @return a new board with the specified cell filled with the given letter
   */
  def fillOneCell(board: Board, char: Char, coord: Coord2D): Board = {
    val (xValue, yValue) = coord
    val updatedRow = board(yValue).updated(xValue, char)

    board.updated(yValue, updatedRow)
  } //T2 completed

  /**
   * Reads a file containing words and their corresponding positions and extracts the words and positions.
   *
   * @param filename the name of the file to read
   * @return a tuple containing a list of words and a list of their corresponding positions
   */
  private def readWordAndPositions(filename: String): (List[String], List[List[Coord2D]]) = {
    val source = Source.fromFile(filename)

    try {
      val lines = source.getLines().map { line =>
        val parts = line.split(" ")
        val word = parts.head
        val coordinates = parts.tail.grouped(2).toList.map { case Array(x, y) => (x.toInt, y.toInt) }

        (word, coordinates)
      }.toList

      val (words, positions) = lines.unzip
      (words, positions)
    } finally {
      source.close()
    }
  }

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

          val updatedBoardWithWord = charPositionPairs.foldLeft(updatedBoard) { case (accBoard, (char, (x, y))) =>
            accBoard.updated(x, accBoard(x).updated(y, char))
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
    val (updatedBoard, updatedRand) = emptyCells.foldLeft((board, rand)) { case ((accBoard, accRand), (x, y)) =>
      val (randomChar, newRand) = f(accRand)
      (accBoard.updated(x, accBoard(x).updated(y, randomChar)), newRand)
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
        case (cell, colIndex) if cell == '*' => (rowIndex, colIndex)
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
    val (words, positions) = readWordAndPositions(filename)
    val (updatedBoard, updatedRand) = completeBoardRandomly(
      setBoardWithWords(board, words, positions),
      rand,
      randomMove
    )

    (updatedBoard, updatedRand)
  }

}
