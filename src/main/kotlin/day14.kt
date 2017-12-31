import kotlin.test.assertEquals

val hexToBits = mapOf(
        '0' to listOf(0, 0, 0, 0),
        '1' to listOf(0, 0, 0, 1),
        '2' to listOf(0, 0, 1, 0),
        '3' to listOf(0, 0, 1, 1),
        '4' to listOf(0, 1, 0, 0),
        '5' to listOf(0, 1, 0, 1),
        '6' to listOf(0, 1, 1, 0),
        '7' to listOf(0, 1, 1, 1),
        '8' to listOf(1, 0, 0, 0),
        '9' to listOf(1, 0, 0, 1),
        'a' to listOf(1, 0, 1, 0),
        'b' to listOf(1, 0, 1, 1),
        'c' to listOf(1, 1, 0, 0),
        'd' to listOf(1, 1, 0, 1),
        'e' to listOf(1, 1, 1, 0),
        'f' to listOf(1, 1, 1, 1)
)

val nextPos = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))

fun floodFill(input: List<List<Int>>, location: Pair<Int, Int>, target: Int): List<List<Int>> {
    val old = input[location.first][location.second]

    if (old == target) return input

    val output = input.map { it.toMutableList() }.toMutableList()

    val toReplace = mutableSetOf(location)

    while (toReplace.isNotEmpty()) {
        val currentPos = toReplace.first()

        toReplace -= currentPos

        output[currentPos.first][currentPos.second] = target

        val next = nextPos
                .map { Pair(it.first + currentPos.first, it.second + currentPos.second )}
                .filter { it.first in 0 until input.size }
                .filter { it.second in 0 until input[0].size }
                .filter { output[it.first][it.second] != target }

        toReplace += next
    }

    return output
}


fun main(args: Array<String>) {

    val mapA = listOf(listOf(0))

    assertEquals(mapA, floodFill(mapA, Pair(0,0), 0))
    assertEquals(listOf(listOf(1)), floodFill(mapA, Pair(0, 0), 1))

    val mapB = listOf(listOf(0, 1), listOf(0, 0))
    val mapC = listOf(listOf(1, 1), listOf(1, 1))

    assertEquals(mapB, floodFill(mapB, Pair(0, 0), 0))
    assertEquals(mapB, floodFill(mapB, Pair(1, 0), 0))
    assertEquals(mapC, floodFill(mapB, Pair(0, 0), 1))


    val key = "wenycdww"

    val map = (0..127)
            .map { knotHash("$key-$it") }
            .map { it.map { hexToBits.getValue(it) }.flatten() }

    println(map.sumBy { it.sum() })

    var currentMap = map
    var regions = 0

    for (i in 0..127) {
        for (j in 0..127) {
            val filledMap = floodFill(currentMap, Pair(i, j), 0)

            if (currentMap == filledMap) continue

            regions += 1
            currentMap = filledMap
        }
    }

    println(regions)
}