import java.io.File
import kotlin.math.absoluteValue
import kotlin.test.assertEquals


fun distance(path: List<String>): Int {
    var x = 0
    var y = 0
    var z = 0

    // https://www.redblobgames.com/grids/hexagons/#coordinates-cube
    for (step in path) {
        when (step) {
            "n"  -> { y += 1; z -= 1 }
            "ne" -> { x += 1; z -= 1 }
            "se" -> { x += 1; y -= 1 }
            "s"  -> { z += 1; y -= 1 }
            "sw" -> { z += 1; x -= 1 }
            "nw" -> { y += 1; x -= 1 }
        }
    }

    // https://www.redblobgames.com/grids/hexagons/#distances-cube
    return maxOf(x.absoluteValue, y.absoluteValue, z.absoluteValue)
}


fun main(args: Array<String>) {
    assertEquals(3, distance(listOf("ne", "ne", "ne")))
    assertEquals(0, distance(listOf("ne", "ne", "sw", "sw")))
    assertEquals(2, distance(listOf("ne", "ne", "s", "s")))
    assertEquals(3, distance(listOf("se", "sw", "se", "sw", "sw")))

    println(distance(File("data/day11.txt").readLines().first().split(",")))
}