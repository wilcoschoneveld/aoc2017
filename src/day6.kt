import java.io.File
import kotlin.test.assertEquals

fun redistribute(config: List<Int>): List<Int> {
    // Create a mutable list which can be modified in place
    val updated = config.toMutableList()

    // Find the maximum value in the list
    val max = updated.withIndex().maxBy { it.value }

    // Throw an assertion error if the maximum could not be found (list should never be empty)
    max ?: throw AssertionError("Could not find max in " + updated)

    // Variables to keep track of redistribution
    var i = max.index
    var left = updated[i]
    updated[i] = 0

    // While there is 'memory' left to redistribute
    while (left > 0) {
        // Move to the next block and wrap around if needed
        i = (i + 1) % updated.size

        // Increment the block and remove one from the remaining stack
        updated[i] += 1
        left -= 1
    }

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
        val before = seen[updated]

        // If it was seen before
        if (before != null) {
            // Return total number of cycles or only the loop size
            return when(loopOnly) {
                true -> seen.size - before
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