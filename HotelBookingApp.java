import java.util.*;

class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

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
    }
}
