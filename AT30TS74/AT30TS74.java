import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class AT30TS74
{

    public final static int AT30TS74_ADDRESS = 0x48;
    
    
    byte[] b = new byte[2];

     double temperature =0.0;
    
    
    public final static int AT30TS74_TEMP	= 0x00;
    public final static int AT30TS74_CONFIG	= 0x01;
    
   //  public final static int RESOLUTION_BITS = 1 << 13
    
 
    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice at30ts74;
 
    //public final static byte config = AT30TS74_BOTH_TEMP_HUMI | AT30TS74_TEMP_HUMI_14BIT | AT30TS74_HEAT_ON ;

    
    public AT30TS74()
    {
        this(AT30TS74_ADDRESS);
    }

  public AT30TS74(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      at30ts74 = bus.getDevice(address);
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
            at30ts74.write(AT30TS74_CONFIG , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    
    }
    
    public double readTemp(){
     
        try {
            
            at30ts74.read(AT30TS74_TEMP , b , 0 , 2);
            
            //at30ts74.read(b , 0 , 2);
            
            //System.out.println("raw 0 :  "  +  b[0]);
        
            //System.out.println("raw 1 :  "  +  b[1]);
            
            int msb = (b[0] & 0xff );
            int lsb = b[1] & 0xff;
            
            int result = msb << 8 | lsb;
            
            
            
            temperature =  ( (double) (result)/0x3fff )  * 165 - 40;
            Thread.sleep(200);
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
    
        return temperature;
    
    }
    
    
public static void main(String args[])
{
     AT30TS74 sensor =  new AT30TS74();  

    
    // sensor.setConfig(config);
    
     while(true){
      
         
         System.out.println("TEMPERATURE :  "  +  sensor.readTemp() + " ˚C");
         
        
         
   }


}


}

