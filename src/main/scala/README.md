# ZigZag-Scala

## Goal
This project was designed to introduce us to the functional paradigm with a more hands-on approach with the objective of developing a ZigZag Game clone

## Main Tasks

1. **Pure Random Move:** the functions *playCPU* and *GetRandomInput* return a valid random coordinate and a new RandomState for a possible next use
2. **Play:** Check documentation below on BoardState's function *play*.
3. **Visual Representation:** Check documentation below on BoardState's function *draw*.
4. **Check for a Continuous Line:** The game ends when there is a continuous vertical or horizontal line across the board, check the Documentation below for more information about our solution to this problem.
5. **Undo'ing a move:** In the Main object, there are some recursive functions (updatePvP, etc...), that are used to make the game flow. Each one of those has an argument *history*, that represents all the BoardStates until then. This makes for an easy implementation of an undo option.
6. **Developing a TUI:** Once we run our code, a simple TUI menu will be displayed in the terminal with some game modes and other options, for each game mode the user will be capable of defining the board size.
7. **Developing a GUI:** TODO
## Documentation

### BoardState
This case class / companion object is used to represent a GameState. <br>
Each GameState is composed by a **Board** and a **UnionFind**.
<br><br>
* ##### draw and drawFold
Unit/String functions that allow for a visual representation of the **Board** via Text User Interface.<br>draw uses 2 tail recursive nested functions while drawFold uses a really extended foldRight. Although the latter one occupies fewer lines of code, it is a lot slower.
<br><br>
* ##### getInput
This function returns a valid coordinate chosen by the user
<br><br>
* ##### play (coordinates, piece) and playRecursive
This function returns a new **BoardState** with *piece* played at *coordinates*, playRecursive does the same but using recursion
<br><br>
* ##### playCPU(randomState)
This function returns a valid random coordinate
<br><br>
* ##### playCPUAdj(randomState, oldCoord)
This function returns a valid coordinate adjacent to *oldCoord*, if no valid coordinate is found then it returns a random valid coordinate
<br><br>
* ##### valid(coordinates)
Used to check if *coordinates* corresponds to an empty slot on the **Board** and is within the boundaries
<br><br>
* ##### hasContinuousLine
Uses a **UnionFind** data structure to check for a possible winner
<br><br>
* ##### defineBoard(size)
A companion object function to initialize the BoardState of length *size*
<br><br>

### UnionFind
This case class / companion object represents a simple Union Find data structure, it is useful to check for a possible winner while having a low time complexity
<br><br>
* ##### createUnions(coordinates, board, piece)
This function returns a new UnionFind where the tile *coordinates* gets connected to the adjacent tiles that have matching pieces
<br><br>
* ##### percolates(board)
This function uses foldRights to check for a possible winner in *board* with a quadratic time complexity.
<br>Note that it would be possible for a constant time complexity if we had implemented the Quick-Find algorithm, however we agreed that a quadratic time complexity would already be a good enough optimization since the brute-force/naive approach would be an estimate of O(6^n)
* ##### init(size)
This companion object function initializes the base data for the Union Find structure

