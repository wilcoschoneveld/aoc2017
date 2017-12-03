import kotlin.math.*
import kotlin.test.assertEquals

class Square(val pos: Pair<Int, Int>, val value: Int, val dir: String)


fun grid(): Sequence<Square> {
    val start = Square(Pair(0, 0), 1, "right")
    val map = hashMapOf(start.pos to start)

    return generateSequence(start) { current ->
        val x: Int
        val y: Int
        val dir: String

        when (current.dir) {
            "right" -> {
                x = current.pos.first + 1
                y = current.pos.second
                dir = if (x > -y) "up" else "right"
            }
            "up" -> {
                x = current.pos.first
                y = current.pos.second + 1
                dir = if (y >= x) "left" else "up"
            }
            "left" -> {
                x = current.pos.first - 1
                y = current.pos.second
                dir = if (x <= -y) "down" else "left"
            }
            else -> {
                x = current.pos.first
                y = current.pos.second - 1
                dir = if (y <= x) "right" else "down"
            }
        }

        val value = (map[Pair(x + 1, y)]?.value ?: 0) +
                (map[Pair(x + 1, y + 1)]?.value ?: 0) +
                (map[Pair(x, y + 1)]?.value ?: 0) +
                (map[Pair(x - 1, y + 1)]?.value ?: 0) +
                (map[Pair(x - 1, y)]?.value ?: 0) +
                (map[Pair(x - 1, y - 1)]?.value ?: 0) +
                (map[Pair(x, y - 1)]?.value ?: 0) +
                (map[Pair(x + 1, y - 1)]?.value ?: 0)

        val square = Square(Pair(x, y), value, dir)
        map[square.pos] = square

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