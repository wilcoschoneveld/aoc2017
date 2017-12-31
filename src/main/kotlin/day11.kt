import java.io.File
import kotlin.math.absoluteValue
import kotlin.test.assertEquals


fun distance(path: List<String>, part2: Boolean = false): Int {
    var (x, y, z) = listOf(0, 0, 0)
    var max = 0

    // From: https://www.redblobgames.com/grids/hexagons/#coordinates-cube
    for (step in path) {
        when (step) {
            "n"  -> { y += 1; z -= 1 }
            "ne" -> { x += 1; z -= 1 }
            "se" -> { x += 1; y -= 1 }
            "s"  -> { z += 1; y -= 1 }
            "sw" -> { z += 1; x -= 1 }
            "nw" -> { y += 1; x -= 1 }
        }

        // From: https://www.redblobgames.com/grids/hexagons/#distances-cube
        val distance = maxOf(x.absoluteValue, y.absoluteValue, z.absoluteValue)

        // For part 2 keep track of maximum distance ever reached, otherwise current dist
        max = if (part2 && max > distance) max else distance
    }

    return max
}


fun main(args: Array<String>) {
    assertEquals(3, distance(listOf("ne", "ne", "ne")))
    assertEquals(0, distance(listOf("ne", "ne", "sw", "sw")))
    assertEquals(2, distance(listOf("ne", "ne", "s", "s")))
    assertEquals(3, distance(listOf("se", "sw", "se", "sw", "sw")))

    val data = File("data/day11.txt").readLines().first().split(",")

    println(distance(data))
    println(distance(data, true))
}