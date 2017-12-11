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

fun simpleHash(input: List<Int>, range: Int = 256): Int {
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


fun toAscii(input: String) = input.map { it.toInt() }
fun toSequence(input: String) = toAscii(input) + listOf(17, 31, 73, 47, 23)
fun xor(input: List<Int>) = input.reduce { acc, i -> acc.xor(i) }
fun Int.toHex() = this.toString(16).padStart(2, '0')

fun knotHash(input: String): String {
    val lengths = toSequence(input)
    var circle = (0 until 256).toList()
    var pos = 0
    var skip = 0

    repeat(64) {
        for (length in lengths) {
            circle = reverseElements(circle, pos, length)
            pos += length + skip
            skip += 1
        }
    }

    return circle.chunked(16).map { xor(it).toHex() }.joinToString("")
}


fun main(args: Array<String>) {
    assertEquals(listOf(2, 1, 0, 3, 4), reverseElements(listOf(0, 1, 2, 3, 4), 0, 3))
    assertEquals(listOf(4, 3, 0, 1, 2), reverseElements(listOf(2, 1, 0, 3, 4), 3, 4))
    assertEquals(listOf(4, 3, 0, 1, 2), reverseElements(listOf(4, 3, 0, 1, 2), 3, 1))
    assertEquals(listOf(3, 4, 2, 1, 0), reverseElements(listOf(4, 3, 0, 1, 2), 1, 5))

    assertEquals(12, simpleHash(listOf(3, 4, 1, 5), 5))

    val input = File("data/day10.txt").readLines().first().split(",").map { it.toInt() }

    println(simpleHash(input))

    assertEquals(listOf(49, 44, 50, 44, 51), toAscii("1,2,3"))
    assertEquals(listOf(49, 44, 50, 44, 51, 17, 31, 73, 47, 23), toSequence("1,2,3"))
    assertEquals(64, xor(listOf(65, 27, 9, 1, 4, 3, 40, 50, 91, 7, 6, 0, 2, 5, 68, 22)))
    assertEquals(listOf("40", "07", "ff"), listOf(64, 7, 255).map { it.toHex() })

    assertEquals("a2582a3a0e66e6e86e3812dcb672a272", knotHash(""))
    assertEquals("33efeb34ea91902bb2f59c9920caa6cd", knotHash("AoC 2017"))
    assertEquals("3efbe78a8d82f29979031a4aa0b16a9d", knotHash("1,2,3"))
    assertEquals("63960835bcdc130f0b66d7ff4f6a5a8e", knotHash("1,2,4"))

    val input2 = File("data/day10.txt").readLines().first().trimMargin()

    println(knotHash(input2))
}