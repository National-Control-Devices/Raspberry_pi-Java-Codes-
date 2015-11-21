import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AD5667
{
  public final static int AD5667_ADDRESS = 0x0E;
  
    // AD5667 Registers
    
    
    /* DAC CHANNELS */
    public final static int AD5667_DAC_CHANNEL_A    =    0x00;
    public final static int AD5667_DAC_CHANNEL_B    =    0x01;
    public final static int AD5667_DAC_CHANNEL_AB    =    0x07;
    
    /* Intructions */
    public final static int AD5667_WRITE_INP_REG    =	0x00;
    public final static int AD5667_UPDATE_DAC_REG   =   0x01;
    public final static int AD5667_WRITE_INPUT_REG_UPDATE_ALL_DAC   =  0x02;
    public final static int AD5667_WRITE_UPDATE_DAC_CHANNEL_N 	 =     0x03;
    
    public final static int AD5667_TOLR1INT    =  0x1A ;// Sign,7bit integer values of RDAC1 tolerance
    public final static int AD5667_TOLR1DEC    =  0x1B ;// 8-bit decimal value of RDAC1 tolerance
    public final static int AD5667_TOLR2INT    =  0x1E ;// Sign,7bit integer values of RDAC3 tolerance
    public final static int AD5667_TOLR2DEC    =  0x1F ;// 8-bit decimal value of RDAC3 tolerance
    
   
    private static boolean verbose = true; //.equals(System.getProperty("htu21df.verbose", "false"));

  private I2CBus bus;
  private I2CDevice ad5667;

  public AD5667()
  {
    this(AD5667_ADDRESS);
  }

  public AD5667(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ad5667 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }


    public void AD5667_WriteRDAC(int data)
    {

        byte[] b = new byte[3];
     
        b[0] = (byte) (((AD5667_DAC_CHANNEL_AB << 3 ) |  AD5667_WRITE_INP_REG ) &  0xFF ) ;
        b[1] = (byte) (data >> 8);
        b[2] = (byte) (data);
        
          try{
                  ad5667.write(b,0,3);
                  Thread.sleep(50);
              
              System.out.println("INPUT : " + data);
            }
        
          catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }


    }

 
  public static void main(String[] args)
  {
    
      AD5667 sensor = new AD5667();
    
     
      while(true)
      {
          
          for(int i=0;i<65535;i++)
          sensor.AD5667_WriteRDAC(i);
      
      }
      
      
  
  }
}

