import java.rmi.server.*;
import java.net.*;


public class ClassLoading {
    String Name_of_Vehicle;

    public static void main(String argv[]) {
        ClassLoading cl = new ClassLoading();
        cl.haveARide();
    }

    private void load(String name) {
        try {
            Class c = Class.forName(name);
            Vehicle v = (Vehicle) c.newInstance();
            v.ride();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rmiLoad(String name) {
        try {
            URL codebase = new URL("file:./" + name + ".java");
            System.out.println(codebase);
            Class c = RMIClassLoader.loadClass(codebase, name);
            Vehicle v = (Vehicle) c.newInstance();
            v.ride();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rmiLoadRemote(String name) {
        try {
            URL codebase = new URL("http://localhost:8080/");
            System.out.println(codebase);
            Class c = RMIClassLoader.loadClass(codebase, name);
            Vehicle v = (Vehicle) c.newInstance();
            v.ride();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void customClassLoader(String name) {
        try {
            ClassLoader cl = new MyClassLoader();
            Class c = cl.loadClass(name);
            Vehicle v = (Vehicle) c.newInstance();
            v.ride();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void haveARide() {

        Name_of_Vehicle = "Car";
        load(Name_of_Vehicle);
        Name_of_Vehicle = "Truck";
        rmiLoad(Name_of_Vehicle);
        Name_of_Vehicle = "Bicycle";
        rmiLoadRemote(Name_of_Vehicle);
        Name_of_Vehicle = "Motorcycle";
        customClassLoader(Name_of_Vehicle);
    }

}
