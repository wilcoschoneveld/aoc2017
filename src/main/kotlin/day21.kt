import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Ok so I've thought a lot about how to approach this problem but the best way to create 2d arrays in kotlin seems to
// be to use lists of lists of elements.

typealias Columns<T> = List<T>
typealias Rows<T> = List<Columns<T>>

// I've defined columns to be a list of elements. Another choice would be to define a row as a list of elements but
// I thought it would be better to use the higher order 'Grid' as a class, instead of a list of rows.

class Grid<T>(val rows: Rows<T>) {

    // The rows contain the columns

    init {
        if (rows.any { it.size != rows.size }) throw NotImplementedError("Only square grids")
    }

    val size = rows.size

    fun elementAt(i: Int): T {
        val x = i / size
        val y = i % size

        return rows[x][y]
    }

    fun <R> map(transform: (T) -> R): Grid<R> {
        return Grid(rows.map { it.map { transform(it) }})
    }

    fun flatten(): Columns<T> {
        return (0 until size*size).map { elementAt(it) }
    }

    fun split(size: Int): Grid<Grid<T>> {
        throw NotImplementedError()
    }
}

fun <T> Columns<T>.inflate(): Grid<T> {
    val gridSize = sqrt((size * size).toDouble()).toInt()
    val rows =  (0 until gridSize).map { subList(it, it + gridSize) }

    return Grid(rows)
}

fun <T> Grid<Grid<T>>.join(): Grid<T> {
    throw NotImplementedError()
}

fun testElementAt() {
    val grid1 = Grid(listOf(listOf('a', 'b', 'c'), listOf('u', 'v', 'w'), listOf('x', 'y', 'z')))

    assertEquals('a', grid1.elementAt(0))
    assertEquals('c', grid1.elementAt(2))
    assertEquals('v', grid1.elementAt(4))
    assertEquals('z', grid1.elementAt(8))

    val grid2 = Grid(listOf(listOf('a', 'b'), listOf('e', 'f')))

    assertEquals('e', grid2.elementAt(2))

    assertFailsWith(NotImplementedError::class) {
        val grid3 = Grid(listOf(listOf('a'), listOf('b')))
    }
}


fun iterate(rulebook: Rulebook<Char>, grid: Grid<Char>): Grid<Char> {
    // TODO: can invert? first get value followed by when

    if (grid.size > 2 && grid.size % 2 == 0)
        return grid.split(2).map { iterate(rulebook, it) }.join()

    if (grid.size > 3 && grid.size % 3 == 0)
        return grid.split(3).map { iterate(rulebook, it) }.join()

    return rulebook.rules.getValue(grid)
}

class Rulebook<T>(val rules: Map<Grid<T>, Grid<T>>) {
    // Rulebook contains rules to convert a grid of type T to a new grid

    val invariants2 = listOf(
            listOf(1, 2, 3, 4),
            listOf(2, 1, 4, 3),
            listOf(3, 1, 4, 2),
            listOf(1, 3, 2, 4),
            listOf(4, 3, 2, 1),
            listOf(3, 4, 1, 2),
            listOf(2, 4, 1, 3),
            listOf(4, 2, 3, 1)
    )

    val invariants3 = listOf(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
            listOf(7, 4, 1, 8, 5, 2, 9, 6, 3),
            listOf(9, 8, 7, 6, 5, 4, 3, 2, 1),
            listOf(3, 6, 9, 2, 5, 8, 1, 4, 7),
            listOf(7, 8, 9, 4, 5, 6, 1, 2, 3),
            listOf(9, 6, 3, 8, 5, 2, 7, 4, 1),
            listOf(3, 2, 1, 6, 5, 4, 9, 8, 7),
            listOf(1, 4, 7, 2, 5, 8, 3, 6, 9)
    )

    fun fullyInvariant(): Rulebook<T> {
        // TODO:
        // convert flattened invariants to grid invariants by using a rotate and flip method,
        // this should eliminate the need for the Grid.flatten and Columns.inflate methods
        // and also remove restrictions to grids of size 2 and 3

        val newRules = rules.toList().map { (gridIn, gridOut) ->
            val invariants = when (gridIn.size) {
                2 -> invariants2
                3 -> invariants3
                else -> throw NotImplementedError()
            }

            invariants.map {
                val flattened = gridIn.flatten()
                val reshaped = it.map { flattened[it] }.inflate()

                Pair(reshaped, gridOut)
            }
        }.flatten().toMap()

        return Rulebook(newRules)
    }
}


fun main(args: Array<String>) {
    testElementAt()

    // TODO:
    // load rulebook based on example
    // iterate starting grid once and assert example
}