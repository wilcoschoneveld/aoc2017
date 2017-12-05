import java.io.File
import kotlin.test.assertEquals


fun steps(offsets: List<Int>): Int {
    val instructions = offsets.toMutableList()

    var cur = 0
    var steps = 0

    while (cur >= 0 && cur < instructions.size) {
        val jump = instructions[cur]

        instructions[cur] += 1

        cur += jump
        steps += 1
    }

    return steps
}


fun main(args: Array<String>) {
    assertEquals(5, steps(listOf(0, 3, 0, 1, -3)))

    println(steps(File("data/day5.txt").readLines().map { it.toInt() }))
}