
import java.io.*;
import java.util.*;

class Reservation implements Serializable {
=======
import java.util.*;


class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
=======
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public void validateRoom(String type) throws InvalidBookingException {
        if (!rooms.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type");
        }
    }

    public void checkAvailability(String type) throws InvalidBookingException {
        if (rooms.get(type) <= 0) {
            throw new InvalidBookingException("Room not available");
        }
    }

    public void bookRoom(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }
}


class Reservation {

    private String id;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;



        this.guestName = guestName;
        this.roomType = roomType;
    }


=======

    public String getGuestName() {


    public String getId() {
        return id;

    }

    public String getRoomType() {
        return roomType;
    }

}






    public String toString() {
        return id + " " + guestName + " " + roomType;
    }
}


class Inventory implements Serializable {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public void allocate(String type) {
        if (rooms.containsKey(type) && rooms.get(type) > 0) {
            rooms.put(type, rooms.get(type) - 1);
        }
    }

    public void print() {
=======


class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {

        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public synchronized boolean allocateRoom(String type) {
        if (rooms.containsKey(type) && rooms.get(type) > 0) {
            rooms.put(type, rooms.get(type) - 1);
            return true;
        }
        return false;
    }

    public void printInventory() {

        for (Map.Entry<String, Integer> e : rooms.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
    }


    public Map<String, Integer> getRooms() {
        return rooms;
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> reservations = new ArrayList<>();

    public void add(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getAll() {
        return reservations;
    }
}

class PersistenceService {
    private static final String FILE_NAME = "data.ser";

    public void save(Inventory inventory, BookingHistory history) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            out.writeObject(inventory);
            out.writeObject(history);
            out.close();
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    public Object[] load() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME));
            Inventory inventory = (Inventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();
            in.close();
            return new Object[]{inventory, history};
        } catch (Exception e) {
            return null;
        }
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PersistenceService service = new PersistenceService();

        Inventory inventory;
        BookingHistory history;

        Object[] data = service.load();
        if (data != null) {
            inventory = (Inventory) data[0];
            history = (BookingHistory) data[1];
            System.out.println("Data Restored");
        } else {
            inventory = new Inventory();
            history = new BookingHistory();
            System.out.println("Fresh Start");
        }

        System.out.println("Enter number of bookings:");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter Reservation ID:");
            String id = sc.nextLine();
            System.out.println("Enter Guest Name:");
            String name = sc.nextLine();
            System.out.println("Enter Room Type:");
            String type = sc.nextLine();

            inventory.allocate(type);
            Reservation r = new Reservation(id, name, type);
            history.add(r);
        }

        System.out.println("Current Bookings:");
        for (Reservation r : history.getAll()) {
            System.out.println(r);
        }

        System.out.println("Inventory:");
        inventory.print();

        service.save(inventory, history);
        System.out.println("Data Saved");
=======
}

class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notifyAll();
    }

    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return queue.poll();
    }
}

class BookingProcessor extends Thread {
    private BookingQueue queue;
    private Inventory inventory;

    public BookingProcessor(BookingQueue queue, Inventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        for (int i = 0; i < 3; i++) {
            BookingRequest req = queue.getRequest();
            boolean success = inventory.allocateRoom(req.getRoomType());
            if (success) {
                System.out.println("Booked for " + req.getGuestName() + " " + req.getRoomType());
            } else {
                System.out.println("Failed for " + req.getGuestName() + " " + req.getRoomType());
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) {
        BookingQueue queue = new BookingQueue();
        Inventory inventory = new Inventory();

        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();

        queue.addRequest(new BookingRequest("A", "Single"));
        queue.addRequest(new BookingRequest("B", "Single"));
        queue.addRequest(new BookingRequest("C", "Single"));
        queue.addRequest(new BookingRequest("D", "Double"));
        queue.addRequest(new BookingRequest("E", "Suite"));
        queue.addRequest(new BookingRequest("F", "Suite"));

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
        }

        System.out.println("Final Inventory:");
        inventory.printInventory();

        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public boolean isAvailable(String type) {
        return rooms.containsKey(type) && rooms.get(type) > 0;
    }

    public void allocate(String type) {
        rooms.put(type, rooms.get(type) - 1);
    }

    public void release(String type) {
        rooms.put(type, rooms.get(type) + 1);
    }
}

class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(Reservation r) {
        bookings.put(r.getId(), r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void removeReservation(String id) {
        bookings.remove(id);
    }

    public Collection<Reservation> getAll() {
        return bookings.values();
    }
}

class CancellationService {
    private Stack<String> rollbackStack = new Stack<>();

    public void cancel(String id, BookingHistory history, Inventory inventory) {
        Reservation r = history.getReservation(id);
        if (r == null) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }
        rollbackStack.push(id);
        inventory.release(r.getRoomType());
        history.removeReservation(id);
        System.out.println("Cancellation Successful for " + id);
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Inventory inventory = new Inventory();
            BookingHistory history = new BookingHistory();
            CancellationService service = new CancellationService();


public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inventory inventory = new Inventory();

        try {
            System.out.println("Enter Reservation ID:");
            String id = sc.nextLine();

            System.out.println("Enter Guest Name:");
            String name = sc.nextLine();

            System.out.println("Enter Room Type:");
            String type = sc.nextLine();

            inventory.validateRoom(type);
            inventory.checkAvailability(type);

            inventory.bookRoom(type);

            Reservation r = new Reservation(id, name, type);
            System.out.println("Booking Successful: " + r);

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }


class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double cost;

    public Reservation(String reservationId, String guestName, String roomType, double cost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.cost = cost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getCost() {
        return cost;
    }
}

class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

class BookingReportService {
    public void printAllBookings(List<Reservation> reservations) {
        for (Reservation r : reservations) {
            System.out.println(r.getReservationId() + " " + r.getGuestName() + " " + r.getRoomType() + " " + r.getCost());
        }
    }

    public void printSummary(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0;
        for (Reservation r : reservations) {
            totalRevenue += r.getCost();
        }
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: " + totalRevenue);
    }
}

public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("Enter number of bookings:");
            int n = sc.nextInt();
            sc.nextLine();

            for (int i = 0; i < n; i++) {
                System.out.println("Enter Reservation ID:");
                String id = sc.nextLine();
                System.out.println("Enter Guest Name:");
                String name = sc.nextLine();
                System.out.println("Enter Room Type:");

                String type = sc.nextLine();

                if (inventory.isAvailable(type)) {
                    inventory.allocate(type);
                    Reservation r = new Reservation(id, name, type);
                    history.addReservation(r);
                    System.out.println("Booked: " + r);
                } else {
                    System.out.println("Booking Failed: Room not available");
                }
            }

            System.out.println("Enter Reservation ID to cancel:");
            String cancelId = sc.nextLine();
            service.cancel(cancelId, history, inventory);

            System.out.println("Remaining Bookings:");
            for (Reservation r : history.getAll()) {
                System.out.println(r);
            }
        }

                String room = sc.nextLine();
                System.out.println("Enter Cost:");
                double cost = sc.nextDouble();
                sc.nextLine();
                history.addReservation(new Reservation(id, name, room, cost));
            }
        }

        List<Reservation> reservations = history.getAllReservations();

        System.out.println("Booking History:");
        reportService.printAllBookings(reservations);

        System.out.println("Summary Report:");
        reportService.printSummary(reservations);

import java.util.*;

class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}

class AddOnServiceManager {
    private Map<String, List<Service>> serviceMap = new HashMap<>();

    public void addService(String reservationId, Service service) {
        serviceMap.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    public List<Service> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (Service s : getServices(reservationId)) {
            total += s.getCost();
        }
        return total;
    }
}

public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.println("Enter Reservation ID:");
        String reservationId = sc.nextLine();

        System.out.println("Enter number of services:");
        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter service name:");
            String name = sc.nextLine();
            System.out.println("Enter service cost:");
            double cost = sc.nextDouble();
            sc.nextLine();
            manager.addService(reservationId, new Service(name, cost));
        }

        List<Service> services = manager.getServices(reservationId);

        System.out.println("Services for Reservation ID " + reservationId + ":");
        for (Service s : services) {
            System.out.println(s.getName() + " - " + s.getCost());
        }

        System.out.println("Total Additional Cost: " + manager.calculateTotalCost(reservationId));

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class RoomAllocatorService {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> allocatedRoomsByType;
    private HashMap<String, Integer> roomCounters;

    public RoomAllocatorService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.allocatedRoomsByType = new HashMap<>();
        this.roomCounters = new HashMap<>();
    }

    public void processNextReservation(BookingRequestQueue bookingQueue) {
        Reservation reservation = bookingQueue.getNextRequest();

        if (reservation == null) {
            System.out.println("No booking requests available.");
            return;
        }

        String roomType = reservation.getRoomType();

        System.out.println("Processing request for " + reservation.getGuestName() + "...");
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("Reservation could not be confirmed. No rooms available for " + roomType);
            return;
        }

        String roomId = generateUniqueRoomId(roomType);

        reservation.confirmReservation(roomId);
        inventory.decrementAvailability(roomType);

        allocatedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

        System.out.println("Reservation confirmed successfully.");
        reservation.displayReservation();
    }

    private String generateUniqueRoomId(String roomType) {
        String prefix = getRoomPrefix(roomType);
        int nextNumber = roomCounters.getOrDefault(roomType, 0) + 1;
        String roomId = prefix + nextNumber;

        while (allocatedRoomIds.contains(roomId)) {
            nextNumber++;
            roomId = prefix + nextNumber;
        }

        roomCounters.put(roomType, nextNumber);
        allocatedRoomIds.add(roomId);

        return roomId;
    }

    private String getRoomPrefix(String roomType) {
        if (roomType.equalsIgnoreCase("Single Room")) {
            return "S";
        } else if (roomType.equalsIgnoreCase("Double Room")) {
            return "D";
        } else if (roomType.equalsIgnoreCase("Suite Room")) {
            return "SU";
        } else {
            return "R";
        }
    }

    public void displayAllocatedRooms() {
        System.out.println("=== Allocated Rooms ===");
        if (allocatedRoomsByType.isEmpty()) {
            System.out.println("No rooms have been allocated yet.");
            return;
        }

        for (Map.Entry<String, Set<String>> entry : allocatedRoomsByType.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Single Room"));
        bookingQueue.addRequest(new Reservation("David", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Eva", "Single Room"));

        RoomAllocatorService allocationService = new RoomAllocatorService(inventory);

        System.out.println("=== Book My Stay App ===");
        System.out.println("Use Case 6: Reservation Confirmation & Room Allocation");
        System.out.println();

        while (!bookingQueue.isEmpty()) {
            allocationService.processNextReservation(bookingQueue);
        }

        System.out.println();
        allocationService.displayAllocatedRooms();

        System.out.println();
        inventory.displayInventory();




    }
}
