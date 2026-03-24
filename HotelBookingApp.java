
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
=======
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
