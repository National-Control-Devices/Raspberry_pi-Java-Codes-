import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class HIH6130
{


   public final static int HIH_ADDRESS = 0x27 ;
 
   byte[] data = new byte[4];


  private static boolean verbose = "true".equals(System.getProperty("htu21df.verbose", "false"));

//  private static boolean verbose = "true";
  
  private I2CBus bus;
  private I2CDevice hih6130;
 
 private double humidity = 0.0;
 private double tempC = 0.0;

 private double status = 0.0;


 public HIH6130()
{
  this(HIH_ADDRESS);
}

  public HIH6130(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      hih6130 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }


public  void fetchData(){

try
    {
     // hum = sensor.readHumidity();
     //hih6130.write( (byte) 0x27);
    }
    catch (Exception ex)
    {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
    //hih6130.write( (byte) 0x27);
    // Wait for it.  
  
    try { Thread.sleep(50); } catch (Exception ex) { System.err.println("error" + ex.toString()); }
    
    // Read the data we requested.
 //   data = self.HIH6130.read_i2c_block_data(0x27, 0)


     try { 
     hih6130.read(data,0,4);
    
         System.out.println("RAW 0  : " + data[0] );
         System.out.println("RAW 1  : " + data[1] );
         System.out.println("RAW 2  : " + data[2] );
         System.out.println("RAW 3  : " + data[3] );
         
         
         
     }
   catch(Exception ex){ex.printStackTrace(); }

//   Process the data.
    status = (data[0] & 0xc0) >> 6;
    
    humidity = (((data[0] & 0x3f) << 8) | data[1] ) * 100.0 / 16383.0;
    tempC = ((((data[2] & 0xff )<< 8) | data[3] ) >> 2) * 165.0 / 16383.0 - 40.0;
    
    //humidity = (((data[0] & 0x3f) << 8) + (data[1] & 0xff)) * 100.0 / 16383.0;
    //tempC = (((data[2] & 0xff )<< 6) + ((data[3] & 0xfc) >> 2) * 165.0 / 16383.0 - 40.0;
    // tempF = tempC*9.0/5.0 + 32.0
} 

public void printData()
{
  

   NumberFormat NF = new DecimalFormat("##00.00");
 
   System.out.println("HUMIDITY  : " + NF.format(humidity) + "%");
 
   System.out.println("TEMPRATURE  : " + NF.format(tempC) + " C");
}


public static void main(String args[])
{
     HIH6130 sensor =  new HIH6130();  

     while(true){  
     
         sensor.fetchData();
         sensor.printData();

         
     try {Thread.sleep(500);}catch(Exception ex){ex.printStackTrace(); }



   }


}


}

