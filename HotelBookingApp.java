import java.util.*;

class Reservation {
    private String id;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public String toString() {
        return id + " " + guestName + " " + roomType;
    }
}

class Inventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public Inventory() {
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
    }
}
