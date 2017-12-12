import java.io.File
import kotlin.test.assertEquals

val pattern = Regex("(\\d+) <-> ([\\d, ]+)")

fun readPipe(line: String): Pair<Int, List<Int>> {
    val match = pattern.matchEntire(line)

    match ?: throw AssertionError("Could not match pattern with line " + line)

    val from = match.groupValues[1].toInt()
    val to = match.groupValues[2].split(", ").map { it.toInt() }

    return Pair(from, to)
}


fun countPrograms(input: List<String>): Int {
    val pipes = input.map { readPipe(it) }.toMap()
    val toCheck = hashSetOf(0)
    val checked = hashSetOf<Int>()

    while (toCheck.isNotEmpty()) {
        val program = toCheck.first()

        toCheck.remove(program)

        pipes.getValue(program).forEach {
            if (!checked.contains(it)) {
                toCheck.add(it)
            }
        }

        checked.add(program)
    }

    return checked.size
}


fun main(args: Array<String>) {
    val input = """
        |0 <-> 2
        |1 <-> 1
        |2 <-> 0, 3, 4
        |3 <-> 2, 4
        |4 <-> 2, 3, 6
        |5 <-> 6
        |6 <-> 4, 5
        """.trimMargin().split('\n')

    assertEquals(Pair(1, listOf(1)), readPipe("1 <-> 1"))
    assertEquals(Pair(2, listOf(0, 3, 4)), readPipe("2 <-> 0, 3, 4"))
    assertEquals(6, countPrograms(input))

    val data = File("data/day12.txt").readLines()

    println(countPrograms(data))
}