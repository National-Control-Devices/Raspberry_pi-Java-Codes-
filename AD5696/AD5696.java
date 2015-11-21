import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

 public class AD5696
{
  public final static int AD5696_ADDRESS = 0x0C;
  
    // AD5696 Registers
 
    
    /* DAC CHANNELS */
    public final static int AD5696_DAC_CHANNEL_A    =    0x01;
    public final static int AD5696_DAC_CHANNEL_B    =    0x02;
    public final static int AD5696_DAC_CHANNEL_AB   =    0x03;
    public final static int AD5696_DAC_CHANNEL_C    =    0x04;
    public final static int AD5696_DAC_CHANNEL_AC   =    0x05;
    public final static int AD5696_DAC_CHANNEL_BC    =   0x06;
    public final static int AD5696_DAC_CHANNEL_ABC   =   0x07;
    public final static int AD5696_DAC_CHANNEL_D    =    0x08;
    public final static int AD5696_DAC_CHANNEL_ALL  =    0x0F;
    
    
    /* Intructions */
    public final static int AD5696_WRITE_INP_REG    =	0x01;
    public final static int AD5696_UPDATE_DAC_REG   =   0x02;
    public final static int AD5696_WRITE_INPUT_REG_UPDATE_ALL_DAC   =  0x03;
    public final static int AD5696_WRITE_UPDATE_DAC_CHANNEL_N 	 =     0x04;
   
    private static boolean verbose = true; //.equals(System.getProperty("htu21df.verbose", "false"));

    private I2CBus bus;
    private I2CDevice ad5696;

    public AD5696()
    {
        this(AD5696_ADDRESS);
    }

    public AD5696(int address)
    {
        try
        {
            // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ad5696 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }


    public void AD5696_WriteRDAC(int data)
    {

        byte[] b = new byte[3];
     
       // byte command =0;
        
        //b[0] = (byte)0x11;
        //b[1] = (byte)0x01;
        
        //b[1] = (byte)0x05;
        //b[2] = (byte)0xFF;
        
        
       // b[0]
        b[0] = (byte) (((AD5696_DAC_CHANNEL_ALL << 4 ) |  AD5696_WRITE_INP_REG ) &  0xFF ) ;
       
        b[1] = (byte) (data >> 8);
        b[2] = (byte) (data);
        
          try{
           
              //ad5696.write(command);
              
              Thread.sleep(50);
              
              ad5696.write(b,0,3);
            }
        
          catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }


    }

 
  public static void main(String[] args)
  {
    
      AD5696 sensor = new AD5696();
    
     
      while(true)
      {
          
          for(int i=0;i<65535;i++)
          {
              sensor.AD5696_WriteRDAC(i);
              System.out.println("tap  : " + i );
      
          }
       
      }
      
  }
}

