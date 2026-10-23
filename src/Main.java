import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    static final Scanner input = new Scanner(System.in);

    // One index represents one patient across ALL four lists.
    static final ArrayList<String> patientNames = new ArrayList<>();
    static final ArrayList<Integer> patientAges = new ArrayList<>();
    static final ArrayList<String> appointmentTimes = new ArrayList<>();
    static final ArrayList<Integer> patientIds = new ArrayList<>();

    // Stable IDs keep queued patients valid when record indexes change.
    static final Queue<Integer> consultationQueue = new LinkedList<>();
    static long nextPatientId = 1;

    public static void main(String[] args) {
        System.out.println("Welcome to the Clinic Queue and Patient Management System!");
        boolean running = true;
        while (running) {
            displayMenu();
            Integer choice = readInt("Choose an option: ", 0, 10);
            if (choice == null) {
                System.out.println("\nInput closed. Exiting safely.");
                break;
            }
            switch (choice) {
                case 1: addPatient(); break;
                case 2: displayRecords(); break;
                case 3: removePatient(); break;
                case 4: searchPatient(); break;
                case 5: sortRecords(); break;
                case 6: enqueuePatient(); break;
                case 7: dequeuePatient(); break;
                case 8: peekPatient(); break;
                case 9: displayQueue(); break;
                case 10: checkQueueEmpty(); break;
                case 0: running = false; break;
                default: break; // readInt already rejects invalid choices.
            }
        }
        System.out.println("Goodbye! Patient records are kept only during this session.");
        input.close();
    }

    static void displayMenu() {
        System.out.println("\n=== CLINIC MAIN MENU ===");
        System.out.println("1. Add patient");
        System.out.println("2. Display patient records");
        System.out.println("3. Remove patient");
        System.out.println("4. Search patients");
        System.out.println("5. Sort patient records");
        System.out.println("6. Enqueue patient for consultation");
        System.out.println("7. Call next patient (dequeue)");
        System.out.println("8. Peek at next patient");
        System.out.println("9. Display consultation queue");
        System.out.println("10. Check whether queue is empty");
        System.out.println("0. Exit");
    }

    // Reading complete lines avoids leftover newlines and InputMismatchException.
    // null signals end-of-input so even redirected or interrupted input exits safely.
    static String readLine(String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) {
            return null;
        }
        return input.nextLine().trim();
    }

    static Integer readInt(String prompt, int minimum, int maximum) {
        while (true) {
            String value = readLine(prompt);
            if (value == null) {
                return null;
            }
            try {
                int number = Integer.parseInt(value);
                if (number >= minimum && number <= maximum) {
                    return number;
                }
            } catch (NumberFormatException exception) {
                // Includes letters, blank input, decimals, and overflowing integers.
            }
            System.out.println("Invalid input. Enter a whole number from "
                    + minimum + " to " + maximum + ".");
        }
    }

    static String readName() {
        while (true) {
            String name = readLine("Patient name (1-60 characters): ");
            if (name == null) {
                return null;
            }
            boolean valid = !name.isEmpty() && name.length() <= 60;
            for (int i = 0; i < name.length(); i++) {
                if (Character.isISOControl(name.charAt(i))) {
                    valid = false;
                }
            }
            if (valid) {
                return name;
            }
            System.out.println("Invalid name. Use 1-60 characters without control characters.");
        }
    }

    static String readAppointmentTime() {
        while (true) {
            String time = readLine("Appointment time (24-hour HH:mm): ");
            if (time == null) {
                return null;
            }
            if (time.matches("[0-9]{2}:[0-9]{2}")) {
                int hour = Integer.parseInt(time.substring(0, 2));
                int minute = Integer.parseInt(time.substring(3, 5));
                if (hour <= 23 && minute <= 59) {
                    return time;
                }
            }
            System.out.println("Invalid time. Use HH:mm from 00:00 to 23:59 (for example, 09:30).");
        }
    }

    static void addPatient() {
        if (nextPatientId > Integer.MAX_VALUE) {
            System.out.println("Cannot add a patient: this session has used all patient IDs.");
            return;
        }
        String name = readName();
        if (name == null) return;
        Integer age = readInt("Age (0-130): ", 0, 130);
        if (age == null) return;
        String time = readAppointmentTime();
        if (time == null) return;

        // Commit all fields together only after every input has been validated.
        int id = (int) nextPatientId++;
        patientNames.add(name);
        patientAges.add(age);
        appointmentTimes.add(time);
        patientIds.add(id);
        System.out.println("Patient added. Patient ID: " + id
                + ". Use option 6 to join the consultation queue.");
    }

    static void printHeader() {
        System.out.printf("%-10s %-60s %-5s %-11s%n", "ID", "Name", "Age", "Appointment");
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    static void printPatient(int index) {
        System.out.printf("%-10d %-60s %-5d %-11s%n", patientIds.get(index),
                patientNames.get(index), patientAges.get(index), appointmentTimes.get(index));
    }

    static void displayRecords() {
        if (patientIds.isEmpty()) {
            System.out.println("No patient records available.");
            return;
        }
        printHeader();
        for (int i = 0; i < patientIds.size(); i++) {
            printPatient(i);
        }
        System.out.println("Total patients: " + patientIds.size());
    }

    static int findPatientIndex(int id) {
        return patientIds.indexOf(id);
    }

    static void removePatient() {
        if (patientIds.isEmpty()) {
            System.out.println("No patient records available to remove.");
            return;
        }
        Integer id = readInt("Patient ID to remove: ", 1, Integer.MAX_VALUE);
        if (id == null) return;
        int index = findPatientIndex(id);
        if (index == -1) {
            System.out.println("Patient ID not found.");
            return;
        }
        String name = patientNames.get(index);
        // Remove the ID from the queue without rearranging the remaining patients.
        boolean wasQueued = consultationQueue.remove(id);
        patientNames.remove(index);
        patientAges.remove(index);
        appointmentTimes.remove(index);
        patientIds.remove(index);
        System.out.println("Removed patient " + id + " (" + name + ").");
        if (wasQueued) {
            System.out.println("Also removed from the consultation queue.");
        }
    }

    static void searchPatient() {
        if (patientIds.isEmpty()) {
            System.out.println("No patient records available to search.");
            return;
        }
        System.out.println("1. Search by name (case-insensitive exact match)");
        System.out.println("2. Search by patient ID");
        Integer choice = readInt("Search option: ", 1, 2);
        if (choice == null) return;
        if (choice == 2) {
            Integer id = readInt("Patient ID: ", 1, Integer.MAX_VALUE);
            if (id == null) return;
            int index = findPatientIndex(id);
            if (index == -1) {
                System.out.println("Patient ID not found.");
            } else {
                printHeader();
                printPatient(index);
            }
            return;
        }
        String name = readName();
        if (name == null) return;
        int matches = 0;
        for (int i = 0; i < patientNames.size(); i++) {
            if (patientNames.get(i).equalsIgnoreCase(name)) {
                if (matches == 0) printHeader();
                printPatient(i);
                matches++;
            }
        }
        if (matches == 0) {
            System.out.println("No patients found with that name.");
        } else {
            System.out.println("Matching patients: " + matches);
        }
    }

    static void sortRecords() {
        if (patientIds.isEmpty()) {
            System.out.println("No patient records available to sort.");
            return;
        }
        System.out.println("1. Bubble Sort");
        System.out.println("2. Insertion Sort");
        Integer algorithm = readInt("Sorting algorithm: ", 1, 2);
        if (algorithm == null) return;
        System.out.println("1. Name (A-Z, case-insensitive)");
        System.out.println("2. Age (youngest first)");
        System.out.println("3. Appointment time (earliest first)");
        Integer field = readInt("Sort by: ", 1, 3);
        if (field == null) return;

        System.out.println("\nBEFORE SORTING:");
        displayRecords();
        if (algorithm == 1) {
            bubbleSort(field);
        } else {
            insertionSort(field);
        }
        System.out.println("\nAFTER SORTING ("
                + (algorithm == 1 ? "Bubble Sort" : "Insertion Sort") + "):");
        displayRecords();
        System.out.println("Consultation queue order is unchanged (FIFO).");
    }

    // Compare values directly; no Comparator or library sorting is used.
    // Fixed-width HH:mm strings compare in chronological order within a day.
    static int compareRecords(int first, int second, int field) {
        if (field == 1) {
            return patientNames.get(first).compareToIgnoreCase(patientNames.get(second));
        }
        if (field == 2) {
            return Integer.compare(patientAges.get(first), patientAges.get(second));
        }
        return appointmentTimes.get(first).compareTo(appointmentTimes.get(second));
    }

    static void swapRecords(int first, int second) {
        String name = patientNames.get(first);
        int age = patientAges.get(first);
        String time = appointmentTimes.get(first);
        int id = patientIds.get(first);
        patientNames.set(first, patientNames.get(second));
        patientAges.set(first, patientAges.get(second));
        appointmentTimes.set(first, appointmentTimes.get(second));
        patientIds.set(first, patientIds.get(second));
        patientNames.set(second, name);
        patientAges.set(second, age);
        appointmentTimes.set(second, time);
        patientIds.set(second, id);
    }

    static void bubbleSort(int field) {
        // Compare neighboring records and swap them if they are out of order.
        // Each pass moves the greatest remaining value to the right, so the
        // next pass can stop one position earlier. No swaps means we are done.
        // Swapping ALL fields preserves each patient's identity. Equal keys
        // are never swapped, making this a stable sort. Worst case: O(n^2).
        for (int end = patientIds.size() - 1; end > 0; end--) {
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                if (compareRecords(i, i + 1, field) > 0) {
                    swapRecords(i, i + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    static void insertionSort(int field) {
        // The prefix before i is already sorted. Save the entire next record,
        // shift larger records one position right, then insert the saved record
        // into the gap. Every shift copies ALL four fields together.
        // Strictly larger keys move; equal keys retain their order (stable).
        // Worst case: O(n^2); an already sorted list needs only O(n) comparisons.
        for (int i = 1; i < patientIds.size(); i++) {
            String name = patientNames.get(i);
            int age = patientAges.get(i);
            String time = appointmentTimes.get(i);
            int id = patientIds.get(i);
            int j = i - 1;
            while (j >= 0) {
                int comparison;
                if (field == 1) {
                    comparison = patientNames.get(j).compareToIgnoreCase(name);
                } else if (field == 2) {
                    comparison = Integer.compare(patientAges.get(j), age);
                } else {
                    comparison = appointmentTimes.get(j).compareTo(time);
                }
                if (comparison <= 0) break;
                patientNames.set(j + 1, patientNames.get(j));
                patientAges.set(j + 1, patientAges.get(j));
                appointmentTimes.set(j + 1, appointmentTimes.get(j));
                patientIds.set(j + 1, patientIds.get(j));
                j--;
            }
            patientNames.set(j + 1, name);
            patientAges.set(j + 1, age);
            appointmentTimes.set(j + 1, time);
            patientIds.set(j + 1, id);
        }
    }

    static void enqueuePatient() {
        if (patientIds.isEmpty()) {
            System.out.println("No patient records available. Add a patient before enqueueing.");
            return;
        }
        Integer id = readInt("Patient ID to enqueue: ", 1, Integer.MAX_VALUE);
        if (id == null) return;
        int index = findPatientIndex(id);
        if (index == -1) {
            System.out.println("Patient ID not found. Enqueue failed.");
        } else if (consultationQueue.contains(id)) {
            System.out.println("Patient is already in the consultation queue.");
        } else {
            consultationQueue.offer(id);
            System.out.println("Enqueued patient " + id + " (" + patientNames.get(index)
                    + "). Queue position: " + consultationQueue.size());
        }
    }

    static void dequeuePatient() {
        if (consultationQueue.isEmpty()) {
            System.out.println("Consultation queue is empty. No patient to call.");
            return;
        }
        int id = consultationQueue.poll();
        System.out.println("Calling next patient (FIFO):");
        printHeader();
        printPatient(findPatientIndex(id));
        System.out.println("Patient record retained; removed from the waiting queue.");
    }

    static void peekPatient() {
        if (consultationQueue.isEmpty()) {
            System.out.println("Consultation queue is empty. No next patient.");
            return;
        }
        int id = consultationQueue.peek();
        System.out.println("Next patient (still in queue):");
        printHeader();
        printPatient(findPatientIndex(id));
    }

    static void displayQueue() {
        if (consultationQueue.isEmpty()) {
            System.out.println("Consultation queue is empty.");
            return;
        }
        System.out.println("Consultation queue (FRONT to REAR):");
        int position = 1;
        for (int id : consultationQueue) {
            int index = findPatientIndex(id);
            System.out.println(position + ". ID " + id + " | " + patientNames.get(index)
                    + " | Age " + patientAges.get(index)
                    + " | Appointment " + appointmentTimes.get(index));
            position++;
        }
        System.out.println("Waiting patients: " + consultationQueue.size());
    }

    static void checkQueueEmpty() {
        if (consultationQueue.isEmpty()) {
            System.out.println("Consultation queue is empty.");
        } else {
            System.out.println("Consultation queue is not empty. Waiting patients: "
                    + consultationQueue.size());
        }
    }
}
