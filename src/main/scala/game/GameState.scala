package pt.iscte
package game

import game.GameUtilities.{Board, Coord2D}

import random.RandomImpl

/**
 * A class representing the state of the game.
 *
 * @param board       The game board.
 * @param random      The random number generator used in the game.
 */
case class GameState(board: Board, random: RandomImpl) extends Serializable {

  /**
   * Plays a move in the game.
   *
   * @param start       The starting position for the move.
   * @param dir         The direction of the move.
   * @param currentWord The word to search for.
   * @return A tuple containing a boolean indicating whether the move was successful, and a set of positions affected by the move.
   */
  def play(start: Coord2D, dir: Direction.Direction, currentWord: String): (Boolean, Set[(Int, Int)]) = GameUtilities.play(board, start, dir, currentWord)

}