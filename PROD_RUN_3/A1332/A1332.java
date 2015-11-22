import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class A1332
{

   public final static int A1332_ADDRESS = 0x0D;
 
   byte[] data = new byte[2];

  private static boolean verbose = true;
  
  private I2CBus bus;
  private I2CDevice a1332;
 
 private double angle = 0.0;
 
 public A1332()
{
  this(A1332_ADDRESS);
}

  public A1332(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      a1332 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }


public void fetchData(){

      try
    {
        
          a1332.read(data,0,2);
          Thread.sleep(400);
    
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }

    int msb = data[0] ;
    int lsb = data[1] ;
    
    int raw_angle = msb  << 8 | lsb ;

	raw_angle =  raw_angle >> 4;

    if(raw_angle < 0)
	raw_angle =  raw_angle + 2096 ;

    System.out.println(raw_angle);
    
    angle =  (double)  (360 * raw_angle) / 4096.0     ;
    
}

public void printData()
{

   NumberFormat NF = new DecimalFormat("##00.00");
   System.out.println("Angle   : " + NF.format(angle) + " ˚");
   
}


public static void main(String args[])
{
     A1332 sensor =  new A1332();  

     while(true){  

         sensor.fetchData();
         sensor.printData();

   }

 }


}
