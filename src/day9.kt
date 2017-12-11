import java.io.File
import kotlin.test.assertEquals

fun scoreGroups(input: String): Int {
    var depth = 0
    var score = 0
    var garbage = false
    var skip = false

    for (char in input) {
        if (skip) {
            skip = false
            continue
        }

        if (char == '!') {
            skip = true
            continue
        }

        if (char == '<') {
            garbage = true
            continue
        }

        if (char == '>') {
            garbage = false
            continue
        }

        if (garbage) {
            continue
        }

        if (char == '{') {
            depth += 1
            continue
        }

        if (char == '}') {
            score += depth
            depth -= 1
            continue
        }
    }

    return score
}


fun main(args: Array<String>) {
    assertEquals(1, scoreGroups("{}"))
    assertEquals(6, scoreGroups("{{{}}}"))
    assertEquals(5, scoreGroups("{{},{}}"))
    assertEquals(16, scoreGroups("{{{},{},{{}}}}"))
    assertEquals(1, scoreGroups("{<a>,<a>,<a>,<a>}"))
    assertEquals(9, scoreGroups("{{<ab>},{<ab>},{<ab>},{<ab>}}"))
    assertEquals(9, scoreGroups("{{<!!>},{<!!>},{<!!>},{<!!>}}"))
    assertEquals(3, scoreGroups("{{<a!>},{<a!>},{<a!>},{<ab>}}"))

    val input = File("data/day9.txt").readLines().first()

    println(scoreGroups(input))
}