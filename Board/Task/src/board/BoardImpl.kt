package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = GameBoardImpl<Nothing>(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)

