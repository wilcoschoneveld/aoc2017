import java.io.File
import kotlin.test.assertEquals


fun scoreGroups(input: String) = evalInput(input).first
fun countGarbage(input: String) = evalInput(input).second

fun evalInput(input: String): Pair<Int, Int> {
    var depth = 0
    var score = 0
    var trash = 0
    var garbage = false
    var skip = false

    for (char in input) {
        when {
            skip        -> skip = false
            char == '!' -> skip = true
            char == '>' -> garbage = false
            garbage     -> trash += 1
            char == '<' -> garbage = true
            char == '{' -> depth += 1
            char == '}' -> { score += depth; depth -= 1 }
        }
    }

    return Pair(score, trash)
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

    assertEquals(0, countGarbage("<>"))
    assertEquals(17, countGarbage("<random characters>"))
    assertEquals(3, countGarbage("<<<<>"))
    assertEquals(2, countGarbage("<{!>}>"))
    assertEquals(0, countGarbage("<!!>"))
    assertEquals(0, countGarbage("<!!!>>"))
    assertEquals(10, countGarbage("<{o\"i!a,<{i<a>"))

    val input = File("data/day09.txt").readLines().first()

    println(scoreGroups(input))
    println(countGarbage(input))
}