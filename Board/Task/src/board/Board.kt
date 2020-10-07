package board

import java.lang.IllegalArgumentException

data class Cell(val i: Int, val j: Int) {
    override fun toString()= "($i, $j)"
}


enum class Direction {
    UP, DOWN, RIGHT, LEFT;

    fun reversed() = when (this) {
        UP -> DOWN
        DOWN -> UP
        RIGHT -> LEFT
        LEFT -> RIGHT
    }
}

interface SquareBoard {
    val width: Int

    fun getCellOrNull(i: Int, j: Int): Cell?
    fun getCell(i: Int, j: Int): Cell

    fun getAllCells(): Collection<Cell>

    fun getRow(i: Int, jRange: IntProgression): List<Cell>
    fun getColumn(iRange: IntProgression, j: Int): List<Cell>

    fun Cell.getNeighbour(direction: Direction): Cell?
}

interface GameBoard<T> : SquareBoard {

    operator fun get(cell: Cell): T?
    operator fun set(cell: Cell, value: T?)

    fun filter(predicate: (T?) -> Boolean): Collection<Cell>
    fun find(predicate: (T?) -> Boolean): Cell?
    fun any(predicate: (T?) -> Boolean): Boolean
    fun all(predicate: (T?) -> Boolean): Boolean
}

class GameBoardImpl<T>(override val width:Int) : SquareBoard , GameBoard<T> {

    private val  coordinateMap:MutableMap<Cell, T?> = mutableMapOf()


    init {
        for (i in 1 .. width ) {
            for (j in 1 .. width) {

                val cell = Cell(i, j)
                coordinateMap[cell] = null
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {

        val cell = coordinateMap.filterKeys { it == Cell(i,j) }.takeUnless{ it.isEmpty() }?.keys?.first()

        return when (cell) {
            cell -> cell
            else -> null
        }
    }

    override fun getCell(i: Int, j: Int): Cell {

        val cell = coordinateMap.filterKeys { it == Cell(i,j) }.takeUnless{ it.isEmpty() }?.keys?.first()

        return when (cell){
            cell -> cell as Cell
            else -> throw IllegalArgumentException()
        }
    }

    override fun getAllCells(): Collection<Cell> = coordinateMap.keys

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {

        val rowList = mutableListOf<Cell>()

        for (j in jRange ) {
            val result:Cell? = getCellOrNull(i, j)
            result
                    .takeUnless { it == null }
                    ?.let { rowList.add(it as Cell) }
        }

        return rowList
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {

        val columnList = mutableListOf<Cell>()

        for (i in iRange ) {
            val result:Cell? = getCellOrNull(i, j)
            result
                    .takeUnless { it == null }
                    ?.let{ columnList.add(it as Cell) }
        }

        return columnList
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {

        return when(direction){

            Direction.UP ->  this@GameBoardImpl.getCellOrNull(this.i -1, this.j)
            Direction.DOWN ->  this@GameBoardImpl.getCellOrNull(this.i + 1, this.j)
            Direction.LEFT ->  this@GameBoardImpl.getCellOrNull(this.i , this.j - 1)
            Direction.RIGHT ->  this@GameBoardImpl.getCellOrNull(this.i, this.j + 1)
        }

    }


    override operator fun get(cell: Cell): T? {

        val result = this.getCellOrNull(cell.i, cell.j)

        return when(result) {
            result -> coordinateMap[cell]
            else -> null
        }

    }
    override operator fun set(cell: Cell, value: T?) {

        val result = this.getCellOrNull(cell.i, cell.j)

        when (value) {
            value -> {
                result.takeUnless { it == null }
                        ?.let { coordinateMap[cell] = value as T}
            }
        }
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = coordinateMap
                .filterValues(predicate)
                .keys


    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate)
                .first()


    override fun any(predicate: (T?) -> Boolean): Boolean = filter(predicate).count() > 0

    override fun all(predicate: (T?) -> Boolean): Boolean = filter(predicate).count() == coordinateMap.count()
}

