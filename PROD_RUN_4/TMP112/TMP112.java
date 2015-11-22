import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class TMP112
{

    public final static int TMP112_ADDRESS = 0x48;
    
    
    byte[] b = new byte[2];

     double temperature =0.0;
    
    public final static double DEG_PER_COUNT = 0.0625;
    
    public final static int TMP112_TEMP	= 0x00;
    public final static int TMP112_CONFIG= 0x01;
    public static final int TEMP_L_REG = 0x02;
    public static final int TEMP_H_REG = 0x03;
    
   //  public final static int RESOLUTION_BITS = 1 << 13
    
 
    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice tmp112;
 
    //public final static byte config = TMP112_BOTH_TEMP_HUMI | TMP112_TEMP_HUMI_14BIT | TMP112_HEAT_ON ;

    
    public TMP112()
    {
        this(TMP112_ADDRESS);
    }

  public TMP112(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      tmp112 = bus.getDevice(address);
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
            tmp112.write(TMP112_CONFIG , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    }
    

    // helper function
    public int twos_comp(int value,int mask) {
       
        value = ~(value & mask) + 1;
        
        return value & mask;
    }
    
    public double readTemp(){
     
        int temp = 0;
        
        try {
            
            tmp112.read(TMP112_TEMP , b , 0 , 2);
            temp = (b[0] << 8) + b[1];
            
            // Shift the temperature into the correct range as per spec
            temp = (temp >> 4) & 0x0FFF;
            
            // Check if its a negative value
            if ( (temp & 0x0800) != 0) {
                temp = -1 * (twos_comp(temp, 0x0FFF));
            }
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
    
        // Convert to a floating point value
        return temp * DEG_PER_COUNT;
        
    }
    

    public static void main(String args[])
    {
   
        TMP112 sensor =  new TMP112();
    
        // sensor.setConfig(config);
    
     while(true){
         
         System.out.println("TEMPERATURE :  "  +  sensor.readTemp());
         
   }


}


}
