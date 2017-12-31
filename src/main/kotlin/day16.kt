import java.io.File
import kotlin.coroutines.experimental.buildSequence
import kotlin.test.assertEquals

val spinRegex = Regex("s(\\d+)")
val exchangeRegex = Regex("x(\\d+)/(\\d+)")
val partnerRegex = Regex("p(\\w)/(\\w)")

fun dance(start: String, moves: List<String>) = buildSequence {
    var next = start

    moves.forEach {
        next = perform(next, it)
        yield(next)
    }
}

fun perform(input: String, move: String): String {
    spinRegex.matchEntire(move)?.let {
        val (_, size) = it.groupValues
        return spin(input, size.toInt())
    }

    exchangeRegex.matchEntire(move)?.let {
        val (_, a, b) = it.groupValues
        return exchange(input, a.toInt(), b.toInt())
    }

    partnerRegex.matchEntire(move)?.let {
        val (_, a, b) = it.groupValues
        return partner(input, a.first(), b.first())
    }

    return input
}

fun spin(input: String, size: Int): String = input.takeLast(size) + input.dropLast(size)

fun exchange(input: String, posA: Int, posB: Int): String {
    val output = input.toMutableList()

    output[posA] = input[posB]
    output[posB] = input[posA]

    return output.joinToString("")
}

fun partner(input: String, itemA: Char, itemB: Char) = exchange(input, input.indexOf(itemA), input.indexOf(itemB))

fun main(args: Array<String>) {
    assertEquals("eabcd", spin("abcde", 1))
    assertEquals("eabdc", exchange("eabcd", 3, 4))
    assertEquals("baedc", partner("eabdc", 'e', 'b'))

    assertEquals("deabc", perform("abcde", "s2"))

    val testMoves = listOf("s1", "x3/4", "pe/b")
    assertEquals("baedc", dance("abcde", testMoves).last())

    val start = "abcdefghijklmnop"
    val moves = File("data/day16.txt").readLines().first().split(",")
    val end = dance(start, moves).last()
    println(end)

    // It would take a long time to perform 1 billion dances, so lets try to find a cycle
    val dances = mutableListOf(start)
    var stage = start

    while (true) {
        // Perform the full dance again
        stage = dance(stage, moves).last()

        // If current arrangement was already seen before, cycle is found
        if (dances.contains(stage)) break

        // Add to possible dances
        dances.add(stage)
    }

    // Calculate the remainder of 1 billion dances with the cycle, and report the arrangement
    println(dances[1000000000 % dances.size])
}