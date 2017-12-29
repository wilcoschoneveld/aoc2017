import java.io.File

fun main(args: Array<String>) {
    val instructions = File("data/day18.txt").readLines()

    val registry = mutableMapOf<Char, Long>()
    var i = 0

    reader@ while (i in 0 until instructions.size) {
        val cmd = instructions[i].take(3)
        val target = instructions[i][4]
        val source = instructions[i].drop(6)

        val targetVal = registry.getOrDefault(target, 0L)
        val sourceVal =
                if (source.isEmpty()) 0L
                else try { source.toLong() }
                catch (e: NumberFormatException) { registry.getOrDefault(source[0], 0L) }

        if (cmd == "jgz" && targetVal > 0) {
            i += sourceVal.toInt()
            continue@reader
        }

        when (cmd) {
            "snd" -> println("play sound! $targetVal")
            "rcv" -> if (targetVal != 0L) {
                println("recover!")
                break@reader
            }
            "set" -> registry[target] = sourceVal
            "add" -> registry[target] = targetVal + sourceVal
            "mul" -> registry[target] = targetVal * sourceVal
            "mod" -> registry[target] = targetVal % sourceVal
        }

        i++
    }

    print(registry)
}