package pt.iscte
package random

/**
 * A trait representing a stateful random number generator.
 */
trait Random {

  /**
   * Generates the next pseudo-random integer between 0 (inclusive) and `n` (exclusive),
   * along with the updated state of the random number generator.
   *
   * @param n The exclusive upper bound of the generated random integer.
   * @return A tuple containing the generated random integer and the updated state
   *         of the random number generator.
   */
  def nextInt(n: Int): (Int, Random)

  //def nextCoords(n:Int):((Int,Int), randState)
}