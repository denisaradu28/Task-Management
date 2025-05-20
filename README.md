# Task-Management

A simple Java project for managing tasks, implementing a seializable object model for both simple and complex tasks.

## Project Structure


## Main Classes

### `Task` (abstract, sealed)
- Represents a generic task.
- Attributes:
  - `idTask`: unique ID, automatically generated.
  - `statusTask`: task status (e.g., "New", "In Progress", "Completed").
  - `taskName`: task name.
- Methods:
  - `estimateDuration()`: abstract, implemented in derived classes.
  - `toString()`: provides a user-friendly description of the task.

### `SimpleTask` (extends `Task`)
- Represents a simple task with a fixed estimated duration.

### `ComplexTask` (extends `Task`)
- Represents a task composed of multiple sub-tasks.
- Can contain a list of `SimpleTask` or even other `Task` instances.

## How to Run the Project

1. Make sure you have JDK 17+ installed (required for sealed classes).
2. Clone the project:
   ```bash
   git clone https://github.com/username/task-manager-java.git
