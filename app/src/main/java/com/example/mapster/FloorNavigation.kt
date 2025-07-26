package com.example.mapster

import java.util.PriorityQueue

data class DestinationInfo(
    val distance: Double,
    val direction: String
)

data class DijkstraResult(
    val distances: Map<String, Double>,
    val previousNodes: Map<String, String?>,
    val directions: Map<String, List<Triple<String, String, Double>>>
)

class FloorNavigation {
    private val floorGraph = mapOf(
        "306 IPDC Lab" to listOf(
            Pair("307A Classroom", DestinationInfo(1.5, "LEFT then take RIGHT")),
            Pair("Elevator", DestinationInfo(1.0, "RIGHT"))
        ),
        "307A Classroom" to listOf(
            Pair("306 IPDC Lab", DestinationInfo(1.5, "LEFT then again take LEFT")),
            Pair("307B Control Lab", DestinationInfo(2.0, "STRAIGHT"))
        ),
        "307B Control Lab" to listOf(
            Pair("307A Classroom", DestinationInfo(2.0, "STRAIGHT")),
            Pair("308A Classroom", DestinationInfo(0.5, "STRAIGHT"))
        ),
        "308A Classroom" to listOf(
            Pair("307B Control Lab", DestinationInfo(0.5, "STRAIGHT")),
            Pair("309A Classroom", DestinationInfo(2.0, "STRAIGHT"))
        ),
        "309A Classroom" to listOf(
            Pair("308A Classroom", DestinationInfo(2.0, "STRAIGHT")),
            Pair("309B Analog Lab", DestinationInfo(2.5, "RIGHT then take LEFT")),
            Pair("Washroom", DestinationInfo(4.5, "STRAIGHT"))
        ),
        "309B Analog Lab" to listOf(
            Pair("309A Classroom", DestinationInfo(2.5, "RIGHT then again take RIGHT")),
            Pair("310 Classroom", DestinationInfo(2.5, "STRAIGHT"))
        ),
        "Washroom" to listOf(
            Pair("309A Classroom", DestinationInfo(4.5, "STRAIGHT")),
            Pair("313B Classroom", DestinationInfo(1.5, "RIGHT")),
            Pair(
                "314A Food Science Lab",
                DestinationInfo(4.0, "STRAIGHT towards balcony then take RIGHT")
            )
        ),
        "310 Classroom" to listOf(
            Pair("309B Analog Lab", DestinationInfo(2.5, "STRAIGHT")),
            Pair("311 Faculty Room", DestinationInfo(1.5, "RIGHT"))
        ),
        "311 Faculty Room" to listOf(
            Pair("310 Classroom", DestinationInfo(1.5, "LEFT")),
            Pair("312A Classroom", DestinationInfo(2.5, "STRAIGHT"))
        ),
        "312A Classroom" to listOf(
            Pair("311 Faculty Room", DestinationInfo(2.5, "STRAIGHT")),
            Pair("313A Classroom", DestinationInfo(1.5, "RIGHT"))
        ),
        "313A Classroom" to listOf(
            Pair("312A Classroom", DestinationInfo(1.5, "LEFT")),
            Pair("313B Classroom", DestinationInfo(2.5, "STRAIGHT"))
        ),
        "313B Classroom" to listOf(
            Pair("313A Classroom", DestinationInfo(2.5, "STRAIGHT")),
            Pair("Washroom", DestinationInfo(1.5, "LEFT"))
        ),
        "314A Food Science Lab" to listOf(
            Pair("Washroom", DestinationInfo(4.0, "towards Balcony then take LEFT")),
            Pair("314B Biology Lab", DestinationInfo(2.0, "STRAIGHT"))
        ),
        "314B Biology Lab" to listOf(
            Pair("314A Food Science Lab", DestinationInfo(2.0, "STRAIGHT")),
            Pair("Elevator", DestinationInfo(3.0, "STRAIGHT towards Junction then take RIGHT"))
        ),
        "Elevator" to listOf(
            Pair(
                "314B Biology Lab",
                DestinationInfo(3.0, "After Exiting from lift take RIGHT then LEFT")
            ),
            Pair("306 IPDC Lab", DestinationInfo(1.0, "After exiting from Lift take LEFT"))
        )
    )

    private fun dijkstra(start: String): DijkstraResult {
        val distances = mutableMapOf<String, Double>()
        val previousNodes = mutableMapOf<String, String?>()
        val directions = mutableMapOf<String, MutableList<Triple<String, String, Double>>>()
        val priorityQueue = PriorityQueue<Pair<Double, String>>(compareBy { it.first })
        var nodesExpanded = 0

        floorGraph.keys.forEach { node ->
            distances[node] = Double.POSITIVE_INFINITY
            previousNodes[node] = null
            directions[node] = mutableListOf()
        }
        distances[start] = 0.0
        priorityQueue.offer(Pair(0.0, start))

        while (priorityQueue.isNotEmpty()) {
            val (currentDistance, currentNode) = priorityQueue.poll()!!
            nodesExpanded++

            if (currentDistance > distances[currentNode]!!) continue

            floorGraph[currentNode]?.forEach { (neighbor, info) ->
                val distance = currentDistance + info.distance
                if (distance < distances[neighbor]!!) {
                    distances[neighbor] = distance
                    previousNodes[neighbor] = currentNode
                    val newDirections = directions[currentNode]!!.toMutableList()
                    newDirections.add(Triple(neighbor, info.direction, info.distance))
                    directions[neighbor] = newDirections
                    priorityQueue.offer(Pair(distance, neighbor))
                }
            }
        }


        return DijkstraResult(
            distances = distances,
            previousNodes = previousNodes,
            directions = directions
        )
    }

    private fun reconstructPathWithDirections(
        directions: Map<String, List<Triple<String, String, Double>>>,
        target: String
    ): List<List<String>>? {
        if (!directions.containsKey(target)) return null

        val pathDirections = directions[target] ?: return null
        val steps = mutableListOf<List<String>>()

        pathDirections.forEachIndexed { index, (location, direction, distance) ->
            steps.add(
                listOf(
                    (index + 1).toString(),                    // Step number
                    "Go $direction to $location",              // Full direction with destination
                    distance.toString()                        // Distance
                )
            )
        }

        return steps
    }

    private fun mapLocation(qrLocation: String): String {
        val locationMapping = mapOf(
            "IPDC Lab" to "306 IPDC Lab",
            "Control/Comm Lab" to "307B Control/Comm Lab",
            "Classroom 307A" to "307A Classroom",
            "Classroom 308A" to "308A Classroom",
            "Classroom 309A" to "309A Classroom",
            "Analog Circuit Lab" to "309B Analog Circuit Lab",
            "Classroom 310" to "310 Classroom",
            "Faculty Room 311" to "311 Faculty Room",
            "Classroom 312A" to "312A Classroom",
            "Classroom 313A" to "313A Classroom",
            "Classroom 313B" to "313B Classroom",
            "Food Science Lab" to "314A Food Science Lab",
            "Cyber Security Lab" to "315 Cyber Security Lab",
            "CAD Simulation Lab" to "316 CAD Simulation Lab",
            "Male Washroom" to "WR016 Male Washroom",
            "Female Washroom" to "WR013 Female Washroom",
            "Faculty Room 317" to "317 Faculty Room",
            "Dept of CS & App" to "301 Dept of CS & App",
            "Board Room" to "302 Board Room",
            "HOD Room" to "303 CS HOD Room",
            "Molecular Biology Lab" to "304/305 Molecular Biology",
            "Library" to "Library",
            "CSE Office" to "302D CSE Office"
        )
        return locationMapping[qrLocation] ?: qrLocation
    }

    fun navigate(currentLocation: String, targetLocation: String): List<List<String>>? {
        val mappedCurrentLocation = mapLocation(currentLocation)

        if (!floorGraph.containsKey(mappedCurrentLocation)) {
            return null
        }

        val result = dijkstra(mappedCurrentLocation)
        return reconstructPathWithDirections(
            result.directions,
            targetLocation
        )
    }
}