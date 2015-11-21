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
public class AD5669
{
  public final static int AD5669_ADDRESS = 0x57;
  
    // AD5669 Registers
    
    
    /* DAC CHANNELS */
    public final static int AD5669_DAC_CHANNEL_A    =    0x00;
    public final static int AD5669_DAC_CHANNEL_B    =    0x01;
    public final static int AD5669_DAC_CHANNEL_C    =    0x02;
    public final static int AD5669_DAC_CHANNEL_D    =    0x03;
    public final static int AD5669_DAC_CHANNEL_E    =    0x04;
    public final static int AD5669_DAC_CHANNEL_F    =    0x05;
    public final static int AD5669_DAC_CHANNEL_G    =    0x06;
    public final static int AD5669_DAC_CHANNEL_H    =    0x07;
    public final static int AD5669_DAC_CHANNEL_ALL  =    0x08;
    
    
    /* Intructions */
    public final static int AD5669_WRITE_INP_REG    =	0x00;
    public final static int AD5669_UPDATE_DAC_REG   =   0x01;
    public final static int AD5669_WRITE_INPUT_REG_UPDATE_ALL_DAC   =  0x02;
    public final static int AD5669_WRITE_UPDATE_DAC_CHANNEL_N 	 =     0x03;
    
    public final static int AD5669_TOLR1INT    =  0x1A ;// Sign,7bit integer values of RDAC1 tolerance
    public final static int AD5669_TOLR1DEC    =  0x1B ;// 8-bit decimal value of RDAC1 tolerance
    public final static int AD5669_TOLR2INT    =  0x1E ;// Sign,7bit integer values of RDAC3 tolerance
    public final static int AD5669_TOLR2DEC    =  0x1F ;// 8-bit decimal value of RDAC3 tolerance
    
   
    private static boolean verbose = true; //.equals(System.getProperty("htu21df.verbose", "false"));

  private I2CBus bus;
  private I2CDevice ad5669;

  public AD5669()
  {
    this(AD5669_ADDRESS);
  }

  public AD5669(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ad5669 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }


    public void AD5669_WriteRDAC(int data)
    {

        byte[] b = new byte[3];
        
        byte[] out = new byte[3];
        //byte command =0;
        
        // b[0]
        b[0] = (byte) (((AD5669_DAC_CHANNEL_ALL << 4 ) |  AD5669_WRITE_UPDATE_DAC_CHANNEL_N ) &  0xFF ) ;
        
        b[1] = (byte) (data >> 8);
        b[2] = (byte) (data);
        
        try{
            
            //ad5669.write(command);
            
            Thread.sleep(50);
            
            ad5669.write(b,0,3);
        
            
            Thread.sleep(50);
            
            ad5669.read(out,0,3);
            
            int msb = out[1] & 0xff;
            int lsb = out[2] & 0xff;
            
            System.out.println("DEBUG read value : " +  ((msb << 8) | lsb) );
            
        }

        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

    }

 
  public static void main(String[] args)
  {
    
      AD5669 sensor = new AD5669();
    
     
      while(true)
      {
          
          for(int i=0;i<65535;i++)
          {
              
          sensor.AD5669_WriteRDAC(i);
          System.out.println("tap  : " + i );
          
          }
      
      }
      
      
  
  }
}

