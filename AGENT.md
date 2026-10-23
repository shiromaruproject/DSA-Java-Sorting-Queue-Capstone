# AGENT.md

Instructions for AI coding agents working on the **DSA Java Sorting & Queue Capstone** (IT2A Data Structures and Algorithms).

The goal is a console-based Java application that solves a real-world problem using **manually implemented sorting algorithms** and a **FIFO queue**, written in a **procedural style (no OOP)**. The repository is the official submission and is graded against the rubric at the bottom of this file.

---

## 1. Hard Rules (never violate)

1. **No OOP.**
   - Do NOT create custom classes for entities (customers, patients, orders, tickets, etc.).
   - Do NOT use inheritance, polymorphism, encapsulation, or abstraction.
   - Do NOT put sorting or queue logic in custom classes.
   - The only class allowed is the single `Main` class that holds `main` and `static` methods.
2. **No built-in sorting.**
   - Never use `Collections.sort()`, `Arrays.sort()`, `List.sort()`, streams `.sorted()`, or comparators for sorting.
   - Implement at least **two** sorting algorithms by hand (e.g., Bubble, Selection, Insertion, Shell, Merge, Quick).
3. **Queue uses Java Collections.**
   - Use `Queue<...>` with `LinkedList` or `ArrayDeque` (`offer`/`add`, `poll`/`remove`, `peek`, `isEmpty`).
   - Queue must be part of the real workflow, not decorative.
4. **Records live in ArrayLists.**
   - Use multiple parallel `ArrayList`s (one per field).
   - Index `i` in every list must refer to the **same record**. Any add, remove, update, or sort must keep all lists synchronized.
5. **Language/environment:** Java, console/terminal only, menu-driven.
6. **Must not crash on bad input.** Validate all input; show clear error messages.

---

## 2. Required Features (minimum)

- Main menu with clear options (loops until the user exits)
- Add and store records
- Display stored records
- At least two manual sorting algorithms, showing records **before and after** sorting
- Queue operations: **enqueue, dequeue, peek, display queue, check empty**
- Queue and sorting integrated into the same workflow
- Search appropriate to the use case
- Input validation and invalid-menu-choice handling
- Messages for empty lists, empty queue, missing records, failed operations
- Proper exit option

Optional extras: update/delete records, reports, transaction history, completed vs. pending lists.

---

## 3. Design Guidance

- **Sorting must not silently change queue order.** If the queue is FIFO, items are processed in arrival order. Sorting applies to the record lists (or a copy), not to the queue's processing order, unless the user is explicitly told otherwise.
- Pick one use case and keep it consistent. Suggested options: Clinic Queue, Customer Service Queue, Online Order Processing, Library Borrowing Requests, IT Help Desk Tickets, Student Registration Queue, Parking Entry Queue, Food Ordering/Preparation, Job Application Tracking, Event Check-In.
- Store queue items as something that can be linked back to the records (e.g., a record ID or index). If records are removed or re-sorted, make sure queued references stay valid (prefer stable IDs over raw indexes).
- Sorting multiple parallel lists: when swapping elements, swap the same positions in **every** list (write a helper like `swapRecords(i, j)`).
- Use static methods to keep code organized: `addRecord()`, `displayRecords()`, `enqueue()`, `dequeue()`, `peek()`, `bubbleSort()`, `insertionSort()`, `searchRecord()`, etc.
- Use clear variable names and meaningful comments (explain why, not just what). Comment each sorting algorithm's logic.
- Use `Scanner` for input. Read lines as strings and parse them (`Integer.parseInt` in try/catch) to avoid `InputMismatchException` and leftover-newline bugs.

---

## 4. Repository Structure

```
DSA-Java-Sorting-Queue-Capstone/
├── src/
│   └── Main.java
├── screenshots/
│   ├── main-menu.png
│   ├── add-and-display-records.png
│   ├── queue-operations.png
│   ├── fifo-behavior.png
│   ├── sorting-algorithm-1.png
│   ├── sorting-algorithm-2.png
│   └── input-validation.png
├── AGENT.md
└── README.md
```

The structure is a guide; all required files must be present and easy to find.

---

## 5. README.md Requirements

Filename must be exactly `README.md`, written in Markdown. Required sections:

1. **Project Title**
2. **Project Description**: the problem and how the system solves it
3. **Objectives**
4. **Use Cases and Features**
5. **Data Structure Explanation**: how ArrayLists store records and how the queue is used
6. **Sorting Algorithm Explanation**: the two algorithms, their purpose, why they suit the use case, and how they are used in the app
7. **How to Run the Program**: compile and run instructions (e.g., `javac src/Main.java` then `java -cp src Main`)
8. **Screenshots of the System**: each with a short caption

Required screenshots (must be real output from the running app):

- Main menu
- Adding records and displaying stored records
- Queue operations (enqueue, peek, dequeue, display)
- FIFO behavior (first in = first processed)
- First sorting algorithm: before and after
- Second sorting algorithm: before and after
- Input validation and error messages

**Agents cannot capture real screenshots of the user's run.** Do not fabricate or mock screenshots. Create the `screenshots/` folder, reference expected filenames in the README, and tell the user which screenshots they need to capture themselves.

---

## 6. Git and Submission

- Commit in small steps with meaningful messages (e.g., `Add bubble sort for patient records`, `Implement queue dequeue and peek`). Avoid a single giant commit.
- Typical flow:
  ```
  git init
  git add .
  git commit -m "Add Java sorting and queue capstone project"
  git branch -M main
  git remote add origin https://github.com/YOUR-USERNAME/DSA-Java-Sorting-Queue-Capstone.git
  git push -u origin main
  ```
- Updates: `git add .` then `git commit -m "..."` then `git push`.
- Repository should be **Public** (or grant the instructor access).
- Final submission is the **GitHub repository URL**, submitted on the course platform (Canvas) before **Sat Oct 31, 2026, 11:59 PM**.
- Do not commit compiled `.class` files; add a `.gitignore` with `*.class`.
- Do not push, create repos, or submit on the user's behalf without being asked.

---

## 7. Agent Behavior

- **Originality matters.** The work must reflect the student's own understanding, and they must be able to explain the code. Prefer simple, readable code the student can follow over clever tricks. Explain the logic of each algorithm and method when asked, and add comments that teach.
- Match what was taught in class: loops, conditionals, static methods, arrays/ArrayLists, basic Collections.
- Before finishing any change, verify:
  - [ ] Code compiles and runs
  - [ ] No custom classes or OOP constructs
  - [ ] No built-in sort calls
  - [ ] Parallel ArrayLists stay in sync after add/remove/sort
  - [ ] Queue is FIFO and used in the real workflow
  - [ ] Bad input (letters, empty input, out-of-range choices, empty queue/list) is handled gracefully
  - [ ] README sections and screenshot list are complete
- If requirements are ambiguous (e.g., which use case or algorithms), ask the user rather than guessing.

---

## 8. Grading Rubric (100 points)

| Criteria | Points |
|---|---|
| 1. Application Functionality: all required features work and are integrated in one use case | 20 |
| 2. Sorting Algorithms: at least two implemented manually, no built-in sorting | 20 |
| 3. Queue Implementation: correct operations and demonstrated FIFO behavior | 20 |
| 4. Data Management Using ArrayLists: store, access, update, manage records | 10 |
| 5. Input Validation and Error Handling | 5 |
| 6. Procedural Programming Compliance: no custom classes or OOP | 5 |
| 7. README.md and Code Readability | 5 |
| 8. Git Usage: meaningful commits, pushed to GitHub | 5 |
| 9. Submission Completeness: source, README, screenshots in the repo | 10 |
| **Total** | **100** |
