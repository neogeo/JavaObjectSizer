
/**
* Measures the approximate size of an object in memory, given a Class which
* has a no-argument constructor.
* http://www.javapractices.com/topic/TopicAction.do?Id=83
*/
public final class ObjectSizer {

  /**
  * First and only argument is the package-qualified name of a class
  * which has a no-argument constructor.
  */
  public static void main(String args[]){
	String name = "java.util.Hashtable";
	Class theClass = null;
	try {
	  theClass = Class.forName(name);
	}
	catch (Exception ex) {
	  System.err.println("Cannot build a Class object: " + name);
	  System.err.println("Use a package-qualified name, and check classpath.");
	}
	long size = ObjectSizer.getObjectSize( theClass );
	System.out.println("Approximate size of " + theClass + " objects in bytes:" + size);
  }


  /**
  * Return the approximate size in bytes, and return zero if the class
  * has no default constructor.
  *
  * @param aClass refers to a class which has a no-argument constructor.
  */
  public static long getObjectSize( Class aClass ){
    long result = 0;

    //if the class does not have a no-argument constructor, then
    //inform the user and return 0.
    try {
      aClass.getConstructor( new Class[]{} );
    }
    catch ( NoSuchMethodException ex ) {
      System.err.println(aClass + " does not have a no-argument constructor.");
      return result;
    }

    //this array will simply hold a bunch of references, such that
    //the objects cannot be garbage-collected
    Object[] objects = new Object[fSAMPLE_SIZE];

    //build many identical objects a certain number of times 
    try {
      long startMemoryUse = getMemoryUse();
      for (int i=0; i < objects.length ; ++i) {
        objects[i] = aClass.newInstance();
		//Point temp = new Point(1,1,"blah");
		//Vector<Point> list = new Vector<Point>();
		//list.add(temp);
        //objects[idx] = new Node(list, 1, Node.Type.LEAF,1,2,3,3, 0);
      }
      long endMemoryUse = getMemoryUse();

      float approximateSize = ( endMemoryUse - startMemoryUse ) /100f;
      result = Math.round( approximateSize );
    }
    catch (Exception ex) {
      System.err.println("Cannot create object using " + aClass);
    }
    return result;
  }

  // PRIVATE //
  private static int fSAMPLE_SIZE = 100;
  private static long fSLEEP_INTERVAL = 100;

  private static long getMemoryUse(){
    throwOutTheGarbage();
    long totalMemory = Runtime.getRuntime().totalMemory();

    throwOutTheGarbage();
    long freeMemory = Runtime.getRuntime().freeMemory();

    return (totalMemory - freeMemory);
  }

  private static void throwOutTheGarbage() {
    collectGarbage();
    collectGarbage();
  }

  private static void collectGarbage() {
    try {
      System.gc();
      Thread.currentThread().sleep(fSLEEP_INTERVAL);
      System.runFinalization();
      Thread.currentThread().sleep(fSLEEP_INTERVAL);
    }
    catch (InterruptedException ex){
      ex.printStackTrace();
    }
  }
}