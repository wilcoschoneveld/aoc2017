import kotlin.math.*
import kotlin.test.assertEquals

class Square(val pos: Pair<Int, Int>, val value: Int, val dir: String) {
    companion object {
        // Delta positions for all neighbors
        val NEIGHBORS = listOf(
                Pair( 1, 0), Pair( 1,  1), Pair(0,  1), Pair(-1,  1),
                Pair(-1, 0), Pair(-1, -1), Pair(0, -1), Pair( 1, -1))
    }
}


fun nextPos(x: Int, y: Int, dir: String) = when (dir) {
    // Determine next position from given position in spiral pattern
    "right" -> Triple((x + 1), y, if ((x + 1) > -y) "up" else dir)
    "up" -> Triple(x, (y + 1), if ((y + 1) >= x) "left" else dir)
    "left" -> Triple((x - 1), y, if ((x - 1) <= -y) "down" else dir)
    else -> Triple(x, (y - 1), if ((y - 1) <= x) "right" else dir)
}


fun grid(): Sequence<Square> {
    // Start in center (x: 0, y: 0) of grid with value 1
    val start = Square(Pair(0, 0), 1, "right")

    // Create a map to store squares based on position
    val map = hashMapOf(start.pos to start)

    // Return the spiral pattern as a sequence starting from the center square
    return generateSequence(start) {
        // Determine next position from previous square
        val (x, y, dir) = nextPos(it.pos.first, it.pos.second, it.dir)

        // Calculate value by summing values of all existing neighbors
        val value = Square.NEIGHBORS.sumBy { (dx, dy) -> map[Pair(x + dx, y + dy)]?.value ?: 0 }

        // Create square and add to map
        val square = Square(Pair(x, y), value, dir)
        map[square.pos] = square

        // Return new square
        square
    }
}


fun requiredSteps(square: Int): Int {
    var i = 1
    var x = 0
    var y = 0
    var dir = "right"

    while (i < square) {
        when (dir) {
            "right" -> {
                x++
                if (x > -y) dir = "up"
            }
            "up" -> {
                y++
                if (y >= x) dir = "left"
            }
            "left" -> {
                x--
                if (x <= -y) dir = "down"
            }
            "down" -> {
                y--
                if (y <= x) dir = "right"
            }
        }

        i++
    }

    return abs(x) + abs(y)
}


fun main(args: Array<String>) {
    assertEquals(0, requiredSteps(1))
    assertEquals(3, requiredSteps(12))
    assertEquals(2, requiredSteps(23))
    assertEquals(31, requiredSteps(1024))

    println(requiredSteps(361527))

    assertEquals(1, grid().elementAt(0).value)
    assertEquals(1, grid().elementAt(1).value)
    assertEquals(2, grid().elementAt(2).value)
    assertEquals(4, grid().elementAt(3).value)
    assertEquals(5, grid().elementAt(4).value)

    println(grid().first { it.value > 361527 }.value)
}