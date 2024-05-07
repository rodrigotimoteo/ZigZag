package pt.iscte
package game

import pt.iscte.game.GameUtilities.Coord2D

import java.io.{FileInputStream, FileOutputStream, IOException, ObjectInputStream, ObjectOutputStream}

/**
 * A class representing the game state.
 *
 * @param gameState The current state of the game.
 */
case class Game(gameState: GameState, wordCoords: Set[Coord2D]) extends Serializable {

  /**
   * Writes the `Game` object to a file.
   *
   * @param filename The name of the file to write to.
   */
  def writeToFile(filename: String): Unit = Game.saveGame(filename, this)

  /**
   * Reads a `Game` object from a file.
   *
   * @param filename The name of the file to read from.
   * @return         An instance of `Game` representing the loaded game state.
   */
  def readFromFile(filename: String): Option[Game] = Game.readSaveGame(filename)

  /**
   *
   *
   * @param coords
   * @return
   */
  def updateWordCoords(coords: Set[Coord2D]) : Game = {
    val updatedWordCoords = wordCoords ++ coords
    this.copy(wordCoords = updatedWordCoords)
  }
}

/**
 * A Scala module for managing the serialization and deserialization of the `Game` class.
 */
private object Game {

  /** The directory where game files are saved. */
  private val savesDirectory = "./"

  /**
   * Writes a serialized `Game` object to a file.
   *
   * @param name The name of the file to write the `Game` object to.
   * @param game The `Game` object to be serialized and written to the file.
   */
  private def saveGame(name: String, game : Game): Unit = {
    val filePath = savesDirectory + name

    try {
      val outputStream = new ObjectOutputStream(new FileOutputStream(filePath, false))

      try {
        outputStream.writeObject(game)
        println("Game saved to file: " + filePath)
      } finally {
        outputStream.close()
      }
    } catch {
      case e: IOException =>
        e.printStackTrace()
        println("Failed to save game to file: " + filePath)
    }
  }

  /**
   * Reads a serialized `Game` object from a file.
   *
   * @param name The name of the file to read the `Game` object from.
   * @return An `Option` containing the read `Game` object if successful, or `None` if an error occurs.
   */
  private def readSaveGame(name: String) : Option[Game] = {
    val filePath = savesDirectory + name

    try {
      val inputStream = new ObjectInputStream(new FileInputStream(filePath))

      try {
        val game = inputStream.readObject().asInstanceOf[Game]
        println("Game saved to file: " + filePath)
        Some(game)
      } finally {
        inputStream.close()
      }
    } catch {
      case _: IOException =>
        println("Failed to save game to file: " + filePath)
        None
    }
  }

  def generateGameFromSave(filename: String): Option[Game] = {
    readSaveGame(filename)
  }

}