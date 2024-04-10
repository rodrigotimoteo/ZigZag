package pt.iscte
package random

/**
 * Represents a pseudo-random number generator implementation based on a given seed.
 * This case class implements the Random trait, providing functionality to generate
 * pseudo-random integers and update the internal state based on a linear congruential formula.
 *
 * @param seed the initial seed value used for generating pseudo-random numbers
 */
case class RandomImpl(seed: Long) extends Random  {

  /**
   * Generates the next pseudo-random integer value.
   * This method calculates the next pseudo-random integer using the current seed value.
   * It updates the seed and returns the generated integer along with the updated RandomImpl instance.
   *
   * @param n the bound for generating the random integer (exclusive)
   * @return a tuple containing the next pseudo-random integer value and the updated RandomImpl instance
   */
  def nextInt(n: Int): (Int, RandomImpl) = {
    val nextSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRandom = RandomImpl(nextSeed)
    val value = ((nextSeed >>> 16).toInt) % n
    (if (value < 0)
      -value
    else value, nextRandom)
  }
}
