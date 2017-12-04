import java.io.File
import kotlin.test.assertEquals

fun valid(passphrase: String): Boolean {
    val words = passphrase.split(" ")

    return words.toSet().size == words.size
}


fun main(args: Array<String>) {
    assertEquals(true, valid("aa bb cc dd ee"))
    assertEquals(false, valid("aa bb cc dd aa"))
    assertEquals(true, valid("aa bb cc dd aaa"))

    println(File("data/day4.txt").readLines().filter { valid(it) }.size)
}