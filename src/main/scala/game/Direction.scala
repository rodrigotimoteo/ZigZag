package pt.iscte
package game

/**
 * Enumeration representing cardinal and ordinal directions.
 */
object Direction extends Enumeration {
  type Direction = Value

  /**
   * Represents all the possible directions
   */
  val North, South, East, West,
  NorthEast, NorthWest, SouthEast, SouthWest = Value

  /**
   * Retrieves the horizontal component of a given direction.
   *
   * @param dir The direction for which to retrieve the horizontal component.
   * @return The horizontal component of the given direction.
   */
  def horizontalComponent(dir: Direction): Int = dir match {
    case North | South | East | West => 0
    case NorthEast | NorthWest => -1
    case SouthEast | SouthWest => 1
  }

  /**
   * Retrieves the vertical component of a given direction.
   *
   * @param dir The direction for which to retrieve the vertical component.
   * @return The vertical component of the given direction.
   */
  def verticalComponent(dir: Direction): Int = dir match {
    case North | South | East | West => 0
    case NorthEast | SouthEast => 1
    case NorthWest | SouthWest => -1
  }
}