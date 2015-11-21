import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class HYT939
{

    public final static int HYT939_ADDRESS = 0x28;
    
    
    byte[] buffer = new byte[4];

    double humidity =0.0;
    double temperature =0.0;
    


    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice hyt939;
 
    public HYT939()
    {
        this(HYT939_ADDRESS);
    }

  public HYT939(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      hyt939 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }


    public void setConfig(byte config){
    
        try
        {
           // hyt939.write(CONFIG_REG , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    
    }
    

    public void readData(){

     //byte b=0;
        
     try { 
        
         hyt939.read(buffer,0,4);
         
         humidity   = ((buffer[0] & 0x3f) << 8 | buffer[1]) * (100.0 / 0x3fff);
         temperature = (buffer[2] << 8 | (buffer[3] & 0xfc)) * (165.0 / 0xfffc) - 40;

         System.out.println("TEMP(˚C )  :  "  + temperature);
         System.out.println("HUMIDITY :  "  + humidity);
         
         
     }
   
    catch(Exception ex){ex.printStackTrace(); }
   
        
 }


public static void main(String args[])
{
     HYT939 sensor =  new HYT939();  

     while(true){  
     sensor.readData();
    
   }


}


}

