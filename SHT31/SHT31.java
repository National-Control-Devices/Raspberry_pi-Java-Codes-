import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class SHT31
{

   public final static int SHT_ADDRESS = 0x44;
   
    private static boolean verbose = true;
   
  private I2CBus bus;
  private I2CDevice sht31;
 
 private double humidity = 0.0;
 private double tempC = 0.0;
 private double tempF = 0.0;
    


 public SHT31()
{
  this(SHT_ADDRESS);
}

  public SHT31(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      sht31 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }

    public void setmode()
    {
    
        byte[] command =  new byte[2];
 
        command[0] = (byte)0x27;
        command[1] = (byte)0x21;
        
        try {
            
            sht31.write(command,0,2);
            
        }
        
        catch(Exception ex){ex.printStackTrace();}

    
    
    }
    

public  void fetchData(){

    
    byte[] command =  new byte[2];
    byte[] b = new byte[6];

    
    command[0] = (byte)0xE0;
    command[1] = (byte)0x00;
    
        try {
   
            sht31.write(command,0,2);
            
            Thread.sleep(500);
            
            sht31.read(b,0,6);
     }
    
    catch(Exception ex){ex.printStackTrace();}

    
    int raw_TEMP  =  ( (b[0] & 0Xff) << 8 ) | ( b[1] & 0xff ) ;
    int raw_HUMID =  ( (b[3] & 0xff) << 8 ) | ( b[4] & 0xff );
    
    System.out.println("raw temp" + raw_TEMP);
    System.out.println("raw humid" + raw_HUMID);
    
    
    humidity =  ( (double)raw_HUMID/0xffff) * 100 ;
    tempC  =    ( (double)raw_TEMP /0xffff)  * 175  -  45 ;
    tempF =     ( (double)raw_TEMP /0xffff)  * 315  -  49 ;

    
    System.out.println("HUMIDITY  : " + humidity + "%");
    System.out.println("TEMPRATURE  : " + tempC + " C");
    System.out.println("TEMPRATURE  : " + tempF + " F");

    
}

  
public static void main(String args[])
{
     SHT31 sensor =  new SHT31();
    
     sensor.setmode();
    

     while(true){
         
     sensor.fetchData();
      
      }


}


}

