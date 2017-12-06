import java.io.File
import kotlin.test.assertEquals

fun numCycles(config: List<Int>): Int {
    val updated = config.toMutableList()
    val seen = hashSetOf(updated)

    while (true) {
        val max = updated.withIndex().maxBy { it.value }

        max ?: throw AssertionError("Could not find max in " + updated)

        var i = max.index
        var left = updated[i]
        updated[i] = 0

        while (left > 0) {
            i = (i + 1) % updated.size
            updated[i] += 1
            left -= 1
        }

        if (seen.contains(updated)) return seen.size

        seen.add(updated)
    }
}

fun main(args: Array<String>) {
    assertEquals(5, numCycles(listOf(0, 2, 7, 0)))

    val config = File("data/day6.txt").readLines().first().split("\t").map { it.toInt() }
    println(numCycles(config))
}