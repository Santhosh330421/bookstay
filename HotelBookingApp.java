import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String id;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
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
    }
}
