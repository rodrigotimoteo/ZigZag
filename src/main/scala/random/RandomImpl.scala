package pt.iscte
package random

case class RandomImpl(seed: Long) extends Random  {
  def nextInt(n: Int): (Int, RandomImpl) = {
    val nextSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    val nextRandom = RandomImpl(nextSeed)
    val value = ((nextSeed >>> 16).toInt) % n
    (if (value < 0)
      -value
    else value, nextRandom)
  }
}
