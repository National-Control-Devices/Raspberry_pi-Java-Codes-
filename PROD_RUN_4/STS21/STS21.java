import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class STS21
{

    public final static int STS21_ADDRESS = 0x40;
    
    byte[] b = new byte[2];

     double temperature =0.0;
    
    public final static int STS21_TEMP	= 0x00;
    public final static int STS21_CONFIG= 0x01;
    public static final int TEMP_L_REG = 0x02;
    public static final int TEMP_H_REG = 0x03;
    
   //  public final static int RESOLUTION_BITS = 1 << 13
    
 
    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice sts21;
 
    //public final static byte config = STS21_BOTH_TEMP_HUMI | STS21_TEMP_HUMI_14BIT | STS21_HEAT_ON ;

    
    public STS21()
    {
        this(STS21_ADDRESS);
    }

  public STS21(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      sts21 = bus.getDevice(address);
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
         //   STS21.write(STS21_CONFIG , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    }
    
    
    public double readTemp(){
     
        int temp = 0;
        
        try {
            
            sts21.read(b,0,2);
            
            temp = ( (b[0] & 0xff ) << 8) + ( b[1] & 0xff );

            System.out.println(temp);            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
    
        // Convert to a floating point value
        return -46.85 + 175.72 * temp / 0xffff ;
        
    }
    

    public static void main(String args[])
    {
   
        STS21 sensor =  new STS21();
    
        // sensor.setConfig(config);
    
     while(true){
         
         System.out.println("TEMPERATURE :C  "  +  sensor.readTemp());
   }

  }

}
