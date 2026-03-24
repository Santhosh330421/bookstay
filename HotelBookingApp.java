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
        try (Scanner sc = new Scanner(System.in)) {
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
        }
    }
}
