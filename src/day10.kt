import java.io.File
import kotlin.test.assertEquals

fun reverseElements(input: List<Int>, pos: Int, length: Int): List<Int> {
    val output = input.toMutableList()

    for (i in 0 until length) {
        val from = (pos + i) % input.size
        val to = (length - i - 1 + pos) % input.size

        output[to] = input[from]
    }

    return output
}

fun hashInput(input: List<Int>, range: Int = 256): Int {
    var circle = (0 until range).toList()
    var pos = 0
    var skip = 0

    for (length in input) {
        circle = reverseElements(circle, pos, length)
        pos += length + skip
        skip += 1
    }

    return circle[0] * circle[1]
}

fun main(args: Array<String>) {
    assertEquals(listOf(2, 1, 0, 3, 4), reverseElements(listOf(0, 1, 2, 3, 4), 0, 3))
    assertEquals(listOf(4, 3, 0, 1, 2), reverseElements(listOf(2, 1, 0, 3, 4), 3, 4))
    assertEquals(listOf(4, 3, 0, 1, 2), reverseElements(listOf(4, 3, 0, 1, 2), 3, 1))
    assertEquals(listOf(3, 4, 2, 1, 0), reverseElements(listOf(4, 3, 0, 1, 2), 1, 5))

    assertEquals(12, hashInput(listOf(3, 4, 1, 5), 5))

    val input = File("data/day10.txt").readLines().first().split(",").map { it.toInt() }

    println(hashInput(input))
}