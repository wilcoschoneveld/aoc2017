import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class Node(val name: String, val weight: Int, var parent: Node? = null, var children: List<Node> = listOf()) {
    // Calculate totalWeight only once by lazy, be careful that changing children corrupts the calculated value!
    val totalWeight: Int by lazy { weight + children.sumBy { it.totalWeight } }
}


fun buildTree(programs: List<String>): Node {
    // Regex to match the "name (weight) -> child1, child2, ..." pattern
    val pattern = Regex("([a-z]+) \\((\\d+)\\)( -> ([\\w, ]+))?")

    // Mutable map to keep track of nodes and connections
    val nodes = mutableMapOf<String, Node>()
    val connections = mutableMapOf<String, List<String>>()

    // For each program definition
    programs.forEach {
        // Try to match the regex pattern
        val match = pattern.matchEntire(it)

        match ?: throw AssertionError("Could not match pattern with " + it)

        // Unpack into the respective variables
        val (_, name, weight, _, children) = match.groupValues

        // Create and store a new Node instance with given name and weight
        nodes[name] = Node(name, weight.toInt())

        // Store a list of child node names to connect later (after all nodes are created)
        if (children.isNotEmpty()) connections[name] = children.split(", ")
    }

    // For all connections (parent name -> children names)
    connections.forEach {
        // Find the parent node from cache
        val parent = nodes.getValue(it.key)

        // Connect all children nodes to the parent
        parent.children = it.value.map { nodes.getValue(it) }

        // Connect the parent to each child
        parent.children.forEach { it.parent = parent }
    }

    // Try to find the single node in cache which does not have a parent
    val root = nodes.values.singleOrNull { it.parent == null }

    root ?: throw AssertionError("Could not find single parent")

    return root
}


fun assertBalanceTree(root: Node) {
    // First try to recursively balance children
    root.children.forEach { assertBalanceTree(it) }

    // Group the children by weights
    val weights = root.children.groupBy { it.totalWeight }

    // If the number of groups is bigger than 1, it means there is an imbalance
    if (weights.size > 1) {
        // A correct node is found in the group with a size of bigger than 1
        val correct = weights.values.single { it.size > 1 }.first()

        // The incorrect node is found in solitude
        val incorrect = weights.values.single { it.size == 1 }.first()

        // A correction weight can be calculated as the difference between total weights
        val diff = correct.totalWeight - incorrect.totalWeight

        // Throw an assertion to bring imbalance to attention
        throw AssertionError("Node ${incorrect.name} is imbalanced with $diff, " +
                "set weight to ${incorrect.weight + diff}")
    }
}


fun main(args: Array<String>) {
    val input = """
        |pbga (66)
        |xhth (57)
        |ebii (61)
        |havc (66)
        |ktlj (57)
        |fwft (72) -> ktlj, cntj, xhth
        |qoyq (66)
        |padx (45) -> pbga, havc, qoyq
        |tknk (41) -> ugml, padx, fwft
        |jptl (61)
        |ugml (68) -> gyxo, ebii, jptl
        |gyxo (61)
        |cntj (57)
        """.trimMargin().split('\n')

    val testRoot = buildTree(input)
    assertEquals("tknk", testRoot.name)
    assertFailsWith(AssertionError::class, { assertBalanceTree(testRoot) })

    val data = File("data/day7.txt").readLines()

    val root = buildTree(data)
    println(root.name)
    assertBalanceTree(root)
}