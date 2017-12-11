import java.io.File
import kotlin.test.assertEquals


fun checksum(sheet: List<List<Int>>): Int {
    return sheet.map {
        val max = it.max() ?: 0
        val min = it.min() ?: 0
        max - min
    }.sum()
}


fun combinations(row: List<Int>): List<Pair<Int, Int>> {
    val all = row.mapIndexed { i, x -> row.mapIndexed { j, y -> listOf(i, j, x, y)}}.flatten()

    return all.filter { (i, j, _, _) -> i != j }.map { (_, _, x, y) -> Pair(x, y) }
}


fun divisor(row: List<Int>): Int {
    val (x, y) = combinations(row).first { it.first % it.second == 0 }

    return x / y
}


fun checksum2(sheet: List<List<Int>>): Int {
    return sheet.map { divisor(it) }.sum()
}


fun main(args: Array<String>) {

    val sheet = listOf(listOf(5, 1, 9, 5), listOf(7, 5, 3), listOf(2, 4, 6, 8))
    assertEquals(18, checksum(sheet))

    val sheet2 = listOf(listOf(5, 9, 2, 8), listOf(9, 4, 7, 3), listOf(3, 8, 6, 5))
    assertEquals(9, checksum2(sheet2))

    val lines = File("data/day2.txt").readLines()

    val rows = lines.map { it.split("\t").map { it.toInt() } }

    println(checksum(rows))
    println(checksum2(rows))
}