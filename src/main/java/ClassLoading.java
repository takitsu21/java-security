import java.io.File;
import java.rmi.RMISecurityManager;
import java.rmi.server.*;
import java.net.*;
import java.security.AccessController;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.PrivilegedAction;


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
            URL codebase = new File("src/main/").toURI().toURL();
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
        System.setSecurityManager(new RMISecurityManager());
        Name_of_Vehicle = "Bicycle";
        rmiLoadRemote(Name_of_Vehicle);
        Name_of_Vehicle = "Motorcycle";
        customClassLoader(Name_of_Vehicle);
    }
}
