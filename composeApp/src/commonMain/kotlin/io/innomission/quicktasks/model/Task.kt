package io.innomission.quicktasks.model

data class Task(
    val id: String,
    val name: String = "",
    val description: String = "",
    val dueDate: String? = null,
    val priority: Priority = Priority.LOW,
    val isCompleted: Boolean = false,
)

enum class Priority { LOW, MEDIUM, HIGH }

val TASK_LIST = listOf(
    Task(
        id = "1",
        name = "Finish Kotlin project",
        description = "Complete the development of the Kotlin multiplatform project.",
        dueDate = "2024-10-13",
        priority = Priority.HIGH,
        isCompleted = false,
    ),
    Task(
        id = "2",
        name = "Prepare for CELPIP exam",
        description = "Review materials and practice speaking, listening, and writing tasks for the CELPIP exam.",
        dueDate = "2024-10-20",
        priority = Priority.HIGH,
        isCompleted = true
    ),
    Task(
        id = "3",
        name = "Sunday school lesson planning",
        description = "Prepare Galatians study materials and lesson plan for Sunday school.",
        dueDate = "2024-10-13",
        priority = Priority.MEDIUM,
        isCompleted = true,
    ),
    Task(
        id = "4",
        name = "Research Machine Learning frameworks",
        description = "Explore and compare different machine learning frameworks for business project.",
        priority = Priority.MEDIUM,
        isCompleted = false
    ),
    Task(
        id = "5",
        name = "Buy groceries for food",
        description = "Purchase essentials for the week: fruits, vegetables, and snacks.",
        priority = Priority.LOW,
        isCompleted = true
    ),
    Task(
        id = "6",
        name = "Review Apache Hive presentation",
        description = "Rehearse the presentation slides and prepare for potential questions.",
        dueDate = "2024-10-10",
        priority = Priority.MEDIUM,
        isCompleted = true
    ),
    Task(
        id = "7",
        name = "Update LMS migration proposal",
        description = "Refine the 'eCentennial of Tomorrow' business plan for the LMS upgrade project.",
        dueDate = "2024-10-12",
        priority = Priority.HIGH,
        isCompleted = false
    ),
    Task(
        id = "8",
        name = "Complete NVV statement assignment",
        description = "Finalize and submit the NVV statement for the Business & Entrepreneurship course.",
        dueDate = "2024-10-11",
        priority = Priority.HIGH,
        isCompleted = true
    ),

    Task(
        id = "9",
        name = "Conduct ClickHouse datastore analysis",
        description = "Evaluate the efficiency and features of ClickHouse for high-performance analytics.",
        priority = Priority.HIGH,
        isCompleted = false
    ),
    Task(
        id = "10",
        name = "Design UI for delivery app",
        description = "Create a user-friendly interface for the driverless point-to-point delivery app.",
        dueDate = "2024-10-25",
        priority = Priority.MEDIUM,
        isCompleted = false,
    ),
    Task(
        id = "11",
        name = "Team meeting for business venture",
        description = "Discuss and finalize the lean canvas analysis for instant delivery service.",
        priority = Priority.MEDIUM,
        isCompleted = false
    ),
    Task(
        id = "12",
        name = "Submit expense report",
        description = "File the business expenses report for the last quarter.",
        dueDate = "2024-10-11",
        priority = Priority.LOW,
        isCompleted = true
    ),
    Task(
        id = "13",
        name = "Organize project documents",
        description = "Arrange and categorize important project-related documents in the cloud storage.",
        dueDate = "2024-10-19",
        priority = Priority.LOW,
        isCompleted = false
    ),
    Task(
        id = "14",
        name = "Prepare sermon for Sunday service",
        description = "Write and practice the sermon for this Sunday's church service, focusing on faith and works.",
        dueDate = "2024-10-20",
        priority = Priority.HIGH,
        isCompleted = false,
    ),
    Task(
        id = "15",
        name = "Review Kotlin Multiplatform codebase",
        description = "Go through the codebase to ensure it's optimized and follows best practices.",
        dueDate = "2024-10-17",
        priority = Priority.MEDIUM,
        isCompleted = false
    ),
    Task(
        id = "16",
        name = "Follow up with Centennial College cafeteria survey",
        description = "Check the results and feedback of the meal and snack ordering survey at the Progress campus.",
        dueDate = "2024-10-16",
        priority = Priority.LOW,
        isCompleted = false
    ),
    Task(
        id = "17",
        name = "Finalize vision statement",
        description = "Complete the vision statement for the business venture plan 'Instant Point-to-Point Delivery.'",
        dueDate = "2024-10-13",
        priority = Priority.HIGH,
        isCompleted = true
    ),
    Task(
        id = "18",
        name = "Book appointment with financial advisor",
        description = "Set up a meeting to discuss financial strategies for the new business venture.",
        dueDate = "2024-10-22",
        priority = Priority.MEDIUM,
        isCompleted = false
    )
)
