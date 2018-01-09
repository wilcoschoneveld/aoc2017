import java.io.File
import kotlin.math.absoluteValue
import kotlin.test.assertEquals


data class Vector3(val x: Long, val y: Long, val z: Long) {
    fun add(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
    fun multiply(other: Vector3) = Vector3(x * other.x, y * other.y,z * other.z)
    fun abs() = Vector3(x.absoluteValue, y.absoluteValue, z.absoluteValue)
    fun sum() = x + y + z
    fun anyNegative() = (x < 0) || (y < 0) || (z < 0)
    fun distance() = abs().sum()
}


class Particle(val id: Int, var pos: Vector3, var vel: Vector3, var acc: Vector3) {
    var collided = false

    companion object {
        private val parseDigit = Regex("-?\\d+")

        fun parse(id: Int, buffer: String): Particle {
            val values = parseDigit.findAll(buffer).map { it.value.toLong() }.toList()

            val pos = Vector3(values[0], values[1], values[2])
            val vel = Vector3(values[3], values[4], values[5])
            val acc = Vector3(values[6], values[7], values[8])

            return Particle(id, pos, vel, acc)
        }
    }

    fun copy() = Particle(id, pos, vel, acc)

    fun tick() {
        vel = vel.add(acc)
        pos = pos.add(vel)
    }

    fun isExpanding(): Boolean {
        // A particle is considered to be 'expanding' if its position, velocity and acceleration are not pointing
        // in the same direction, otherwise it means that the particle can move closer to the origin in one axis
        return setOf(pos.multiply(vel),
                     vel.multiply(acc),
                     acc.multiply(pos)).none { it.anyNegative() }
    }
}


fun simulate(input: List<Particle>, collide: Boolean): List<Particle> {
    val particles = input.map { it.copy() }

    while (true) {
        val alive = particles.filter { !it.collided }

        alive.forEach { it.tick() }

        if (collide) {
            // Group particles by position, and any group with more than one particle means a collision
            alive.groupBy { it.pos }
                    .map { it.value }
                    .filter { it.size > 1 }
                    .flatten()
                    .forEach { it.collided = true }
        }

        // If all alive particles are expanding, it means that no particle will move closer to the origin
        if (alive.all { it.isExpanding() }) break
    }

    return particles
}


fun main(args: Array<String>) {
    assertEquals(Vector3(100, -2, 5), Vector3(25, 3, 128).add(Vector3(75, -5, -123)))
    assertEquals(Vector3(15, 0, -8), Vector3(3, 12, -4).multiply(Vector3(5, 0, 2)))

    val data = File("data/day20.txt").readLines()

    val particles = data.mapIndexed { i, str -> Particle.parse(i, str) }

    // First simulate all particles, then sort them in order by pos, vel and acc. The particle with the smallest
    // acceleration will remain closest to the origin
    val closest = simulate(particles, false)
            .sortedBy { it.pos.distance() }
            .sortedBy { it.vel.distance() }
            .sortedBy { it.acc.distance() }
            .first().id

    println(closest)

    // Simulate all particles with collisions and count the number of alive particles, assuming that
    // when monotonous expansion has been reached no more additional collisions will occur
    val numAlive = simulate(particles, true).filter { !it.collided }.size

    println(numAlive)
}