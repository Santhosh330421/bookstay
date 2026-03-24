import java.util.*;

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

    public String toString() {
        return id + " " + guestName + " " + roomType;
    }
}

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
    }
}
