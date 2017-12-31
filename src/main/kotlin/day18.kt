import kotlinx.coroutines.experimental.TimeoutCancellationException
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import java.io.File

class Program(
        val id: Long,
        val instructions: List<String>,
        val sender: Channel<Long>,
        val receiver: Channel<Long>,
        val receiveWhenZero: Boolean = true) {

    val registry = mutableMapOf("p" to id)
    var sendCount = 0

    private fun extractValue(value: String): Long =
            try { value.toLong() }
            catch (e: NumberFormatException) { registry.getOrDefault(value, 0L) }

    suspend fun execute() {
        var ptr = 0

        while (ptr in 0 until instructions.size) {
            val parts = instructions[ptr].split(" ")
            val cmd = parts[0]
            val target = parts[1]
            val source = parts.last()  // if source not provided, this will be target

            val targetVal = extractValue(target)
            val sourceVal = extractValue(source)

            if (cmd == "jgz" && targetVal > 0) {
                ptr += sourceVal.toInt()
                continue
            }

            when (cmd) {
                "snd" -> sender.send(targetVal).also { sendCount++ }
                "rcv" -> if (targetVal != 0L || receiveWhenZero) {
                    registry[target] = withTimeout(1000) { receiver.receive() }
                }
                "set" -> registry[target] = sourceVal
                "add" -> registry[target] = targetVal + sourceVal
                "mul" -> registry[target] = targetVal * sourceVal
                "mod" -> registry[target] = targetVal % sourceVal
            }

            ptr++
        }
    }
}

suspend fun part1() {
    val instructions = File("data/day18.txt").readLines()
    val instructions2 = listOf("rcv a", "jgz p -1")

    val channel0 = Channel<Long>(Channel.UNLIMITED)
    val channel1 = Channel<Long>(Channel.UNLIMITED)

    val p0 = Program(0, instructions, channel0, channel1, false)
    val p1 = Program(1, instructions2, channel1, channel0)

    val run0 = async { p0.execute() }
    val run1 = async { p1.execute() }

    try {
        run0.await()
        run1.await()
    } catch (e: TimeoutCancellationException) {
        println("Possible deadlock detected!")
    }

    val lastValue = p1.registry["a"]

    println("Last frequency played is $lastValue")
}

suspend fun part2() {
    val instructions = File("data/day18.txt").readLines()

    val channel0 = Channel<Long>(Channel.UNLIMITED)
    val channel1 = Channel<Long>(Channel.UNLIMITED)

    val p0 = Program(0, instructions, channel0, channel1)
    val p1 = Program(1, instructions, channel1, channel0)

    val run0 = async { p0.execute() }
    val run1 = async { p1.execute() }

    try {
        run0.await()
        run1.await()
    } catch (e: TimeoutCancellationException) {
        println("Possible deadlock detected!")
    }

    println("Program 1 sent a value ${p1.sendCount} times")
}


fun main(args: Array<String>) = runBlocking {
    part1()
    part2()
}