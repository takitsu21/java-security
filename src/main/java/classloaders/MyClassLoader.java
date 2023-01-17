package classloaders;

import java.io.*;
import java.util.Hashtable;

public class MyClassLoader extends ClassLoader {

  boolean resolveIt;

  public synchronized Class loadClass(String name, boolean resolve)
       throws ClassNotFoundException
  {
    resolveIt = resolve;
    return loadClass(name);
  }

  public synchronized Class loadClass(String name)
       throws ClassNotFoundException
  {
    // See if type as already been loaded by
    // this class loader
    Class result = findLoadedClass(name);
    if (result != null) {
      // Return an already-loaded class
      return result;
    }

    // Check with the primordial class loader
    try {
      result = super.findSystemClass(name);
      // Return a system class
      return result;
    }
    catch (ClassNotFoundException e) { }

    // Don't attempt to load a system file except
    // through the primordial class loader
    if (name.startsWith("java.")) {
      throw new ClassNotFoundException();
    }

    // Try to load it from subdirectory "Motorbikes".
    byte typeData[] = getTypeFromDir(name);
    if (typeData == null) {
      throw new ClassNotFoundException();
    }

    // Parse it
    result = defineClass(name, typeData, 0,
			 typeData.length);
    if (result == null) {
      throw new ClassFormatError();
    }

    if (resolveIt) {
      resolveClass(result);
    }

    // Return class from
    return result;
  }

   private byte[] getTypeFromDir(String typeName) {

     FileInputStream fis;
     String fileName = "Motorbikes" + File.separatorChar +
       typeName.replace('.', File.separatorChar)
       + ".class";

     try {
       fis = new FileInputStream(fileName);
     }
     catch (FileNotFoundException e) {
       return null;
     }

     BufferedInputStream bis =
       new BufferedInputStream(fis);
     ByteArrayOutputStream out =
       new ByteArrayOutputStream();

     try {
       int c = bis.read();
       while (c != -1) {
	 out.write(c);
	 c = bis.read();
       }
     }
     catch (IOException e) {
       return null;
     }
     return out.toByteArray();
   }

}
