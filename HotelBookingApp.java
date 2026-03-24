import java.util.*;

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
    }
}
