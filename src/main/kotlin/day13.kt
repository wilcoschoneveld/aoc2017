import java.io.File
import kotlin.coroutines.experimental.buildSequence
import kotlin.test.assertEquals


typealias Scanner = Pair<Int, Int>


fun parse(input: List<String>): List<Scanner> = input
        .map { it.split(": ") }
        .map { (d, r) -> d.toInt() to r.toInt() }


fun caughtBy(scanners: List<Scanner>, delay: Int = 0) = buildSequence {
    for (scanner in scanners) {
        val (depth, range) = scanner
        val cycle = 2 * (range - 1)

        if ((depth + delay) % cycle == 0) {
            yield(scanner)
        }
    }
}


fun tripSeverity(scanners: List<Scanner>) = caughtBy(scanners)
        .sumBy { (depth, range) -> depth * range }


fun requiredDelay(scanners: List<Scanner>) = generateSequence(0) { it + 1 }
        .first { delay -> caughtBy(scanners, delay).none() }


fun main(args: Array<String>) {
    val input = """
        |0: 3
        |1: 2
        |4: 4
        |6: 4
        """.trimMargin().split("\n")

    assertEquals(24, tripSeverity(parse(input)))
    assertEquals(10, requiredDelay(parse(input)))

    val data = File("data/day13.txt").readLines()
    val scanners = parse(data)

    println(tripSeverity(scanners))
    println(requiredDelay(scanners))
}