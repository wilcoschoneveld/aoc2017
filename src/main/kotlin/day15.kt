import java.math.BigInteger
import kotlin.coroutines.experimental.buildSequence
import kotlin.test.assertEquals

fun buildGenerator(start: Long, factor: Long, criteria: Int = 1) = buildSequence {
    var next = start

    while (true) {
        next = (next * factor) % 2147483647

        if (next % criteria == 0L) yield(next)
    }
}

fun lastBits(n: Long) = BigInteger.valueOf(n).toString(2).padStart(16, '0').takeLast(16)

fun main(args: Array<String>) {
    val testA = buildGenerator(65, 16807)
    val testB = buildGenerator(8921, 48271)

    assertEquals(listOf(1092455L, 1181022009L, 245556042L, 1744312007L, 1352636452L), testA.take(5).toList())
    assertEquals(listOf(430625591L, 1233683848L, 1431495498L, 137874439L, 285222916L), testB.take(5).toList())

    assertEquals("1010101101100111", lastBits(1092455))
    assertEquals("0000000000000001", lastBits(1))

    val generatorA = buildGenerator(873, 16807, 4)
    val generatorB = buildGenerator(583, 48271, 8)

    val count = generatorA.zip(generatorB).take(5000000).count { (a, b) -> lastBits(a) == lastBits(b) }
    println(count)
}