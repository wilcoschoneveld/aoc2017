import java.io.File
import kotlin.test.assertEquals

fun captcha(seq: String): Int {
    // Start with last character in the sequence
    var last = seq.last()
    var sum = 0

    // Loop over all characters in the sequence
    for (char in seq) {
        // If current character matches last character
        if (char == last) {
            // Add current character (digit) to sum
            sum += char.toString().toInt()
        }

        // New last character is current character
        last = char
    }

    return sum
}


fun captcha2(seq: String): Int {
    val n = seq.length / 2

    // Rotate sequence by n
    val seq2 = seq.drop(n) + seq.take(n)

    // Return numeric sum of characters which are equal in both lists
    return seq.zip(seq2)
            .filter { it.first == it.second }
            .map { it.first.toString().toInt() }
            .sum()
}


fun main(args: Array<String>) {
    assertEquals(3, captcha("1122"))
    assertEquals(4, captcha("1111"))
    assertEquals(0, captcha("1234"))
    assertEquals(9, captcha("91212129"))

    val data = File("data/day01.txt").readLines().first()

    print(captcha2(data))
}