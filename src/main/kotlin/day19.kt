import java.io.File


fun getPath(map: List<String>, start: Pair<Int, Int>): List<Char> {
    var (x, y) = start
    var direction = Direction.SOUTH
    val path = mutableListOf(map[y][x])

    while (true) {
        // Perform a step
        when (direction) {
            Direction.NORTH -> y -= 1
            Direction.SOUTH -> y += 1
            Direction.WEST -> x -= 1
            Direction.EAST -> x += 1
        }

        val next = map[y][x]

        // If empty, means end of path
        if (next == ' ') break

        // Arrived at a corner
        if (next == '+') {
            // For all directions: if next step is not empty and this is not the way I came, choose it
            if (direction != Direction.NORTH && map[y+1][x] != ' ') direction = Direction.SOUTH
            else if (direction != Direction.SOUTH && map[y-1][x] != ' ') direction = Direction.NORTH
            else if (direction != Direction.WEST && map[y][x+1] != ' ') direction = Direction.EAST
            else if (direction != Direction.EAST && map[y][x-1] != ' ') direction = Direction.WEST
        }

        // Add visited node to path
        path.add(next)
    }

    return path
}


fun main(args: Array<String>) {
    val map = File("data/day19.txt").readLines()

    val startPos = Pair(map[0].indexOf('|'), 0)

    val path = getPath(map, startPos)

    println(path.filter { it in 'A'..'Z' }.joinToString(""))
    println(path.size)
}