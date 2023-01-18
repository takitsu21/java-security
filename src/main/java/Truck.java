public class Truck implements Vehicle {

    public static void main(String[] args) {
        Vehicle vehicle = new Truck();
        vehicle.ride();
    }
    public void ride() {
        System.out.println("Riding a truck");
    }
}
