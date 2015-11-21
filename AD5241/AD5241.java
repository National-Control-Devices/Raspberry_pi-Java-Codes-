import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/*
 * Humidity, Temperature
 */
public class AD5241
{
  public final static int AD5241_ADDRESS = 0x2C;
   
    
    /* Addresses for Writing Data Byte Contents to RDAC Registers */
    public final static int AD5241_RDAC0     =    0x00; // For quad-channel device software
    public final static int AD5241_RDAC1     =    0x01; // For quad-channel device software
    public final static int AD5241_RDAC3      =   0x03 ;// compatibility, the dual potentiometers in
    
  private static boolean verbose = true;//.equals(System.getProperty("htu21df.verbose", "false"));

  private I2CBus bus;
  private I2CDevice ad5241;

  public AD5241()
  {
    this(AD5241_ADDRESS);
  }

  public AD5241(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ad5241 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }

  
    public void AD5241_WriteRDAC()
    {
 
       
        
       for(int i=0;i<255;i++)
            {
            
                try{
                    
                    ad5241.write((byte)i);
                    ad5241.write(0x80,(byte)i);
                    
                   System.out.println("tap " + i);
   
                    Thread.sleep(200);
                }
                catch (Exception ex)
                {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
            
            } 
       
    }
    
  public static void main(String[] args)
  {
   
        AD5241 sensor = new AD5241();
      
      while(true)
      {
     
            sensor.AD5241_WriteRDAC();
 
      }
  }


}

