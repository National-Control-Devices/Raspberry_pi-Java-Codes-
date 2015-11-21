import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class HYT271
{

    public final static int HYT271_ADDRESS = 0x28;
    
    
  
    double humidity =0.0;
    double temperature =0.0;

   private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice hyt271;
 
    public HYT271()
    {
        this(HYT271_ADDRESS);
    }

  public HYT271(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      hyt271 = bus.getDevice(address);
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
           hyt271.write(config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    }
    

    public void readData(){

     //byte b=0;

        byte[] buffer = new byte[4];
        
        
        byte[] command = new byte[3];
        
        command[0] = (byte)0xB0;
        command[1] = (byte)0x00;
        command[2] = (byte)0x00;
        
        
        
     try {
        

         hyt271.write(command,0,3);
         
         Thread.sleep(1000);
         
         hyt271.read(buffer,0,4);
         
         humidity   =  ((buffer[0] & 0x3f)  << 8 | (buffer[1] & 0xff)) * (100.0 / 16383);
         temperature = ((buffer[2] << 8 | (buffer[3] & 0xfc)) >> 2 ) * (165.0 / 16383) - 40;

        // System.out.println("byte 0 :  "  + ((buffer[0] << 8) | buffer[1]));
         
         System.out.println("TEMP(˚C )  :  "  + temperature);
         System.out.println("HUMIDITY :  "  + humidity);
         
         
     }
   
    catch(Exception ex){ex.printStackTrace(); }
   
        
 }


public static void main(String args[])
{
    HYT271 sensor =  new HYT271();
    
     while(true){
         
         //sensor.setConfig();
     sensor.readData();
    
   }


}


}

