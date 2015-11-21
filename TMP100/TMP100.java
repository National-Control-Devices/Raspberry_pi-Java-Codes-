import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class TMP100
{


   public final static int TMP100_ADDRESS = 0x4A;
   public final static int CONFIG_REGISTER_ADDRESS = 0x02 ;
    
    byte[] data = new byte[2];
    int result = 0;
    double F = 0 ;
    double C = 0;
    


    private static boolean verbose = true;//"true".equals(System.getProperty("htu21df.verbose", "false"));

//  private static boolean verbose = "true";
  
  private I2CBus bus;
  private I2CDevice tmp100;
  //private int result = 0.0;


 public TMP100()
{
  this(TMP100_ADDRESS);
}

  public TMP100(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      tmp100 = bus.getDevice(address);
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
            tmp100.write(CONFIG_REGISTER_ADDRESS , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    
    }
    
    public void readTemp(){

        int tempData = 0;
        
     try {

        
         tmp100.read(data,0,2);
         
         int msb = data[0] & 0xFF;
         int lsb = data[1] & 0xFF;
         
         tempData = (msb<<8) | lsb;
        
         C = (tempData >> 4) * 0.0625 * 1.8 + 32;
         F = (tempData >> 4) * 0.0625 ;
         
         System.out.println("Temprature in celcius : " + C );
         System.out.println("Temprature in Farenhiet  : "  + F );
         
         
         
     }
   
    catch(Exception ex){ex.printStackTrace(); }
        
        
 }

    
    public static void main(String args[])
    {
   
        TMP100 sensor =  new TMP100();

     while(true){  
    
         sensor.readTemp();
         
   }


}


}

