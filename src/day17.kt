import kotlin.test.assertEquals

// Simple linked list implementation
class Entry(val value: Int, var next: () -> Entry) {
    fun toSequence() = generateSequence(this) { it.next() }
    fun toValueList(size: Int) = toSequence().take(size).toList().map { it.value }
}

fun spinLock(speed: Int, size: Int): Entry {
    // Use lateinit to create a circular buffer with a single entry
    lateinit var firstEntry: Entry
    firstEntry = Entry(0, { firstEntry })

    // Start at first entry
    var currentEntry = firstEntry

    // Insert numbers until size is reached
    for (value in 1 until size) {
        // Step forward number of steps through the circular buffer
        repeat(speed) { currentEntry = currentEntry.next() }

        // Store reference to next item in buffer
        val next = currentEntry.next()

        // Create new entry with reference to next item
        val newEntry = Entry(value, { next })

        // Change pointer of current entry to new entry
        currentEntry.next = { newEntry }

        // Move an additional step forward
        currentEntry = newEntry
    }

    return firstEntry
}

fun main(args: Array<String>) {
    assertEquals(listOf(0), spinLock(3, 1).toValueList(1))
    assertEquals(listOf(0, 1), spinLock(3, 2).toValueList(2))
    assertEquals(listOf(0, 5, 2, 4, 3, 6, 1), spinLock(3, 7).toValueList(7))

    val spinLock1 = spinLock(394, 2018)

    println(spinLock1.toSequence().dropWhile { it.value != 2017 }.drop(1).first().value)

    val spinLock2 = spinLock(394, 50000001)

    println(spinLock2.next().value)
}