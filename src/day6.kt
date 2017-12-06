import java.io.File
import kotlin.test.assertEquals

fun redistribute(config: List<Int>): List<Int> {
    // Create a mutable list which can be modified in place
    val updated = config.toMutableList()

    // Find the maximum value in the list
    val max = updated.withIndex().maxBy { it.value }

    // Throw an assertion error if the maximum could not be found (list should never be empty)
    max ?: throw AssertionError("Could not find max in " + updated)

    // Remove all memory from the maximum block
    updated[max.index] = 0

    // Redistribute all memory, starting from the next block and continuing on
    (1..max.value).map { (max.index + it) % updated.size } .forEach { updated[it] += 1 }

    // Return the redistributed memory, will be cast to immutable list
    return updated
}

fun numCycles(config: List<Int>, loopOnly: Boolean = false): Int {
    // Create a hashMap to efficiently check for uniqueness and to keep track of cycles
    val seen = hashMapOf(config to 0)
    var updated = config

    while (true) {
        // Redistribute current memory
        updated = redistribute(updated)

        // Check if current distribution has already been seen before
        seen[updated]?.let {
            // Return total number of cycles or only the loop size
            return when(loopOnly) {
                true -> seen.size - it
                else -> seen.size
            }
        }

        // Add current distribution with the number of cycles
        seen[updated] = seen.size
    }
}

fun main(args: Array<String>) {
    assertEquals(5, numCycles(listOf(0, 2, 7, 0)))
    assertEquals(4, numCycles(listOf(0, 2, 7, 0), true))

    val config = File("data/day6.txt").readLines().first().split("\t").map { it.toInt() }

    println(numCycles(config))
    println(numCycles(config, true))
}