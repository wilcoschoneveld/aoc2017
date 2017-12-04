import java.io.File
import kotlin.test.assertEquals

fun valid(passphrase: String): Boolean {
    val words = passphrase.split(" ")

    // Is the set of words equal in length to the number of words?
    return words.toSet().size == words.size
}

fun valid2(passphrase: String): Boolean {
    val words = passphrase.split(" ")

    // Is the set of sorted words equal in length to the number of words?
    return words.map { it.toList().sorted() }.toSet().size == words.size
}


fun main(args: Array<String>) {
    assertEquals(true, valid("aa bb cc dd ee"))
    assertEquals(false, valid("aa bb cc dd aa"))
    assertEquals(true, valid("aa bb cc dd aaa"))

    println(File("data/day4.txt").readLines().filter { valid(it) }.size)

    assertEquals(true, valid2("abcde fghij"))
    assertEquals(false, valid2("abcde xyz ecdab"))
    assertEquals(true, valid2("a ab abc abd abf abj"))
    assertEquals(true, valid2("iiii oiii ooii oooi oooo"))
    assertEquals(false, valid2("oiii ioii iioi iiio"))

    println(File("data/day4.txt").readLines().filter { valid2(it) }.size)
}