package classloaders;

import java.rmi.server.*;
import java.rmi.*;
import java.net.*;


public class ClassLoading {
  public static void main(String argv[])
  {
    ClassLoading cl=new ClassLoading();
    cl.haveARide();
  }

  String Name_of_Vehicle;

  public void haveARide() {
    Name_of_Vehicle = "Car";
    // ...

    Name_of_Vehicle = "Truck";
    // ...

    Name_of_Vehicle = "Bicycle";
    // ...

    Name_of_Vehicle = "Motorcycle";
    // ...

  }

}
