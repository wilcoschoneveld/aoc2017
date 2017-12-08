import java.io.File
import java.lang.Integer.max
import kotlin.test.assertEquals


val tests = mapOf<String, (Int, Int) -> Boolean>(
    ">=" to {a, b -> a >= b },
    "<=" to {a, b -> a <= b },
    ">" to {a, b -> a > b },
    "<" to {a, b -> a < b },
    "==" to {a, b -> a == b },
    "!=" to {a, b -> a != b }
)


fun largestValue(instructions: List<String>, endOnly: Boolean = true): Int {
    val pattern = Regex("([a-z]+) (dec|inc) (-?\\d+) if ([a-z]+) ([=><!]+) (-?\\d+)")
    val registry = mutableMapOf<String, Int>()
    var maxValue = 0

    instructions.forEach {
        val match = pattern.matchEntire(it)

        match ?: throw AssertionError("Could not match pattern with " + it)

        val target = match.groupValues[1]
        val operation = match.groupValues[2]
        val amount = match.groupValues[3].toInt()
        val reference = match.groupValues[4]
        val condition = match.groupValues[5]
        val value = match.groupValues[6].toInt()

        val targetValue = registry.getOrDefault(target, 0)
        val referenceValue = registry.getOrDefault(reference, 0)

        if (tests[condition]?.invoke(referenceValue, value) == true) {
            val newValue = targetValue + when (operation) {
                "inc" -> amount
                "dec" -> -amount
                else -> 0
            }

            registry[target] = newValue
            maxValue = max(newValue, maxValue)
        }
    }

    val max = registry.maxBy { it.value }

    max ?: throw AssertionError("Could not find largest value in register " + registry)

    return if (endOnly) max.value else maxValue
}


fun main(args: Array<String>) {
    val instructions = """
        |b inc 5 if a > 1
        |a inc 1 if b < 5
        |c dec -10 if a >= 1
        |c inc -20 if c == 10
        """.trimMargin().split('\n')

    assertEquals(1, largestValue(instructions))
    assertEquals(10, largestValue(instructions, false))

    val data = File("data/day8.txt").readLines()

    println(largestValue(data))
    println(largestValue(data, false))
}