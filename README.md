# Clinic Queue and Patient Management System

## Project Description

A console-based Java capstone for IT2A Data Structures and Algorithms. A clinic receptionist can register patients, find their details, organize records, and call waiting patients for consultation in arrival order.

The application uses one `Main` class with static methods. Patient fields are stored in parallel ArrayLists, and a LinkedList-backed Queue stores the IDs of waiting patients. Bubble Sort and Insertion Sort are implemented manually.

The repository's supplied instructions are in `AGENT.md`; there is no `AGENTS.md` file.

## Objectives

- Manage patient information using synchronized parallel ArrayLists.
- Demonstrate and explain two manual sorting algorithms.
- Process consultations using a real FIFO queue.
- Preserve queue order while sorting or removing patient records.
- Validate console input and handle empty or missing data clearly.

## Use Cases and Features

| Menu | Feature | Behavior |
| --- | --- | --- |
| 1 | Add patient | Collects name, age, and appointment time; assigns a unique session ID. |
| 2 | Display records | Shows all four fields and the patient count. |
| 3 | Remove patient | Deletes all four fields at the same index and removes that ID from the waiting queue if present. |
| 4 | Search patients | Finds an exact patient ID or all case-insensitive exact name matches. |
| 5 | Sort records | Select Bubble Sort or Insertion Sort, then name, age, or appointment time; displays before and after. |
| 6 | Enqueue patient | Adds an existing patient ID at the rear; rejects duplicate queue entries. |
| 7 | Call next patient | Dequeues the front patient and displays their details. |
| 8 | Peek | Displays the next patient without removing them. |
| 9 | Display queue | Shows waiting patients from front to rear with their positions. |
| 10 | Check empty | Reports whether the queue is empty and the waiting count when nonempty. |
| 0 | Exit | Ends the menu loop. |

Registration and enqueueing are separate steps: adding a patient does not automatically join the queue. Calling a patient removes their waiting entry but retains their patient record. They may be enqueued again afterward. Patient IDs start at 1, increase during the session, and are not reused after removal.

Names must contain 1-60 characters after trimming, with no control characters. Ages must be whole numbers from 0 to 130. Appointment times use the 24-hour format `HH:mm`, from `00:00` to `23:59`. Blank input, letters in numeric fields, decimal ages, overflowing integers, invalid choices, and missing IDs produce clear messages. End-of-input exits safely, including during registration; an incomplete registration is not saved.

All data lives in memory and is lost when the program exits. Appointment times represent a single day's schedule; the app does not store appointment dates or patient files on disk.

## Data Structure Explanation

### Parallel ArrayLists

| List | Type | Meaning at index `i` |
| --- | --- | --- |
| `patientNames` | `ArrayList<String>` | Patient's name |
| `patientAges` | `ArrayList<Integer>` | Same patient's age |
| `appointmentTimes` | `ArrayList<String>` | Same patient's validated appointment time |
| `patientIds` | `ArrayList<Integer>` | Same patient's unique ID |

An index always identifies one complete patient record across all four lists. `addPatient()` validates every field before adding any values. `removePatient()` removes the same index from every list. Bubble Sort uses `swapRecords()` to swap every field together; Insertion Sort saves, shifts, and restores every field together.

### Consultation Queue

`Queue<Integer> consultationQueue = new LinkedList<>();` stores stable patient IDs. `offer()` enqueues at the rear, `poll()` dequeues from the front, `peek()` reads the front, and `isEmpty()` checks for waiting patients. Iteration displays the queue in FIFO order without changing it.

`findPatientIndex()` locates the current record index for an ID. Because the queue holds IDs instead of ArrayList indexes, changing record order does not change who is called next. Removing a queued patient also removes their ID from the queue, preserving the order of the remaining patients.

Record lookup and duplicate-queue checks take O(n) time. Queue insertion and removal at its ends take O(1). Displaying all queue details uses repeated record lookups and can take O(n²), which is acceptable for this small teaching application.

## Sorting Algorithm Explanation

Choose menu option **5**, select **1 for Bubble Sort** or **2 for Insertion Sort**, and select a field: **1 for name**, **2 for age**, or **3 for appointment time**. Both algorithms show the records before and after sorting and leave the consultation queue untouched.

All sorting is ascending. Names use Java's case-insensitive string comparison; ages use numeric comparison; validated, zero-padded `HH:mm` strings compare in time order. Equal keys retain their current relative order because both algorithms move records only when a key is strictly greater.

### Bubble Sort

`bubbleSort()` repeatedly compares adjacent records and swaps complete records when their chosen fields are out of order. Each pass places the greatest remaining value at the end, so the next pass checks a shorter range. If a pass makes no swaps, the algorithm stops early.

This algorithm suits a small clinic demonstration because neighboring comparisons and swaps are easy to trace. It can arrange names alphabetically, group ages from youngest to oldest, or show appointments from earliest to latest. Comments in the code explain the loop, early exit, and preservation of patient fields.

### Insertion Sort

`insertionSort()` grows a sorted prefix from left to right. It saves the next complete patient record, shifts larger records one position right, and inserts the saved record into the gap. Every shift copies the name, age, time, and ID together.

This algorithm suits a small list, especially one already mostly ordered, such as a schedule with a newly registered patient at the end. Its comments explain the sorted prefix, shifting, insertion, and handling of ties.

| Algorithm | Best time | Average / worst time | Extra sorting space | Stable |
| --- | --- | --- | --- | --- |
| Bubble Sort with early exit | O(n) | O(n²) | O(1) | Yes |
| Insertion Sort | O(n) | O(n²) | O(1) | Yes |

The source uses no built-in sorting methods, streams, or sorting comparators.

## How to Run the Program

Install a Java Development Kit (JDK) version 8 or later and make its `bin` directory available on PATH. Open a terminal in the project folder, then run:

```sh
javac src/Main.java
java -cp src Main
```

If `java` or `javac` is not recognized, use the full paths to the JDK executables. On the Windows machine used to verify this project:

```powershell
& "$env:USERPROFILE\.jdks\openjdk-25\bin\javac.exe" src/Main.java
& "$env:USERPROFILE\.jdks\openjdk-25\bin\java.exe" -cp src Main
```

No external libraries or build tools are required. Recompile after editing the source. Generated `.class` files are ignored by Git.

### Example workflow

Start a fresh session and add these illustrative patients using option **1** three times:

| Assigned ID | Name | Age | Appointment |
| --- | --- | --- | --- |
| 1 | Zoe Cruz | 40 | 08:15 |
| 2 | Ana Reyes | 21 | 10:30 |
| 3 | Ben Santos | 35 | 09:00 |

1. Use **2** to display the original records.
2. Use **6** three times to enqueue IDs **1, 2, 3**, in that order.
3. Use **8** to peek at ID **1**, and **9** to display the queue.
4. Use **5**, then **1**, then **1** to Bubble Sort by name. Record order becomes **2, 3, 1**.
5. Use **9** to verify that the queue remains **1, 2, 3**.
6. Use **5**, then **2**, then **3** to Insertion Sort by appointment time. Record order becomes **1, 3, 2**.
7. Use **7** three times. The called IDs are still **1, 2, 3**.
8. Use **10** to confirm the queue is empty and **0** to exit.

These are example inputs and expected behavior, not screenshots or captured output.

## Screenshots of the System

**Status: awaiting your real screenshots.** The `screenshots/` folder contains capture instructions only. No screenshot images have been generated, fabricated, or included. Run the app yourself and capture actual terminal output using the following filenames. The captions describe what each image must demonstrate.

| Filename to save | Caption / output to capture |
| --- | --- |
| `screenshots/main-menu.png` | The running system's main menu with its options and input prompt. |
| `screenshots/add-and-display-records.png` | Successful patient registration and the displayed records, including IDs, names, ages, and appointment times. |
| `screenshots/queue-operations.png` | Enqueue, peek, dequeue, and queue display output, showing the front patient and remaining patients. |
| `screenshots/fifo-behavior.png` | Patients enqueued in a known order and called in the same order, even after sorting their records. |
| `screenshots/sorting-algorithm-1.png` | Bubble Sort output with both BEFORE SORTING and AFTER SORTING tables visible. |
| `screenshots/sorting-algorithm-2.png` | Insertion Sort output with both BEFORE SORTING and AFTER SORTING tables visible. |
| `screenshots/input-validation.png` | Invalid input and recovery, such as a letter at the menu, a rejected age or time, and an empty queue message. |

Use the example workflow above to demonstrate records, sorting, and FIFO. For validation, try `abc` at the menu, age `-1`, appointment `25:00`, and calling the next patient when the queue is empty; then enter valid values to show recovery. Keep the relevant terminal output readable, using terminal scrollback or additional real screenshots if one image cannot show every operation. After capture, add the images to this folder and embed each in this section with its caption.

## Verification Checklist

The implementation was compiled with the installed OpenJDK 25 compiler, including a Java 8 compatibility compilation (`--release 8`). Scripted console verification passed **688 assertions across 44 sessions**. This covers all six algorithm/field combinations, repeated sorting, stable ties, single records, reverse order, field synchronization, queue order, removal, duplicate enqueue prevention, search, invalid input, and end-of-input at every prompt.

- [x] Code compiles and runs.
- [x] Only one `Main` class with static methods; no custom entity classes or OOP design.
- [x] No built-in sort calls.
- [x] Parallel ArrayLists stay synchronized after add, remove, and both sorts.
- [x] The consultation queue is FIFO and used in the clinic workflow.
- [x] Letters, blank input, out-of-range values, missing records, and empty queue/list are handled gracefully.
- [x] All required README sections and the screenshot list are complete.

### Remaining submission tasks

- [ ] Capture and add the seven real screenshots above, then embed them with their captions.
- [ ] Publish/push the repository to GitHub and make it public or grant the instructor access.
- [ ] Submit the GitHub repository URL on Canvas before **October 31, 2026, at 11:59 PM**, as specified in `AGENT.md`.

Local Git commits separate the implementation from documentation. No GitHub remote was configured when this project was built; publishing and course submission remain your responsibility.
