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
public class AD5259
{
  public final static int AD5259_ADDRESS = 0x18;
  
    // AD5259 Registers
    
    
    
    /* Instruction byte */
    public final static int AD5259_CMD_REG     =  (1 << 7);
    public final static int AD5259_EE_RDAC      = (1 << 5);
    
    /* Addresses for Writing Data Byte Contents to RDAC Registers */
    public final static int AD5259_RDAC0     =    0x00; // For quad-channel device software
    public final static int AD5259_RDAC1     =    0x01; // For quad-channel device software
    public final static int AD5259_RDAC3      =   0x03 ;// compatibility, the dual potentiometers in
    
    // the parts are designated as RDAC1 and RDAC3
    /* EEMEM Registers */
    public final static int AD5259_EEMEM1    =    0x01 ;// Store RDAC1 setting to EEMEM1
    public final static int AD5259_EEMEM3    =    0x03 ;// Store RDAC3 setting to EEMEM3
    public final static int AD5259_EEMEM4    =    0x04 ;// Store user data to EEMEM4
    public final static int AD5259_EEMEM5    =    0x05 ;// Store user data to EEMEM5
    public final static int AD5259_EEMEM6    =    0x06 ;// Store user data to EEMEM6
    public final static int AD5259_EEMEM7    =    0x07 ;// Store user data to EEMEM7
    public final static int AD5259_EEMEM8    =    0x08 ;// Store user data to EEMEM8
    public final static int AD5259_EEMEM9    =    0x09 ;// Store user data to EEMEM9
    public final static int AD5259_EEMEM10   =    0x0A ;// Store user data to EEMEM10
    public final static int AD5259_EEMEM11   =    0x0B ;// Store user data to EEMEM11
    public final static int AD5259_EEMEM12   =    0x0C ;// Store user data to EEMEM12
    public final static int AD5259_EEMEM13   =    0x0D ;// Store user data to EEMEM13
    public final static int AD5259_EEMEM14   =    0x0E ;// Store user data to EEMEM14
    public final static int AD5259_EEMEM15   =    0x0F ;// Store user data to EEMEM15

    /* Intructions */
    public final static int AD5259_NOP		=	  0x00 ;// NOP
    public final static int AD5259_RESTORE  =     0x01 ;// Restore EEMEM (A1, A0) to RDAC (A1, A0)
    public final static int AD5259_STORE    =     0x02 ;// Store RDAC (A1, A0) to EEMEM (A1, A0)
    public final static int AD5259_6DBDOWN	=     0x03 ;// Decrement RDAC (A1, A0) 6 dB
    public final static int AD5259_6DBDOWNALL =	  0x04 ;// Decrement all RDACs 6 dB
    public final static int AD5259_1STEPDOWN	= 0x05 ;// Decrement RDAC (A1, A0) one step
    public final static int AD5259_1STEPDOWNALL	= 0x06 ;// Decrement all RDACs one step
    public final static int AD5259_RESETALL	=	  0x07 ;// Reset: restore EEMEMs to all RDACs
    public final static int AD5259_6DBUP	 =    0x08 ;// Increment RDACs (A1, A0) 6 dB
    public final static int AD5259_6DBUPALL   =   0x09 ;// Increment all RDACs 6 dB
    public final static int AD5259_1STEPUP     =  0x0A ;// Increment RDACs (A1, A0) one step
    public final static int AD5259_1STEPUPALL   = 0x0B ;// Increment all RDACs one step
    
    public final static int AD5259_INSTRUCTION(int x)
    {
    return (((x) & 0x0F) << 3);
    }
    
    
    public final static int AD5259_TOLR1INT    =  0x1A ;// Sign,7bit integer values of RDAC1 tolerance
    public final static int AD5259_TOLR1DEC    =  0x1B ;// 8-bit decimal value of RDAC1 tolerance
    public final static int AD5259_TOLR2INT    =  0x1E ;// Sign,7bit integer values of RDAC3 tolerance
    public final static int AD5259_TOLR2DEC    =  0x1F ;// 8-bit decimal value of RDAC3 tolerance
    
   
  private static boolean verbose = true;//.equals(System.getProperty("htu21df.verbose", "false"));

  private I2CBus bus;
  private I2CDevice ad5259;

  public AD5259()
  {
    this(AD5259_ADDRESS);
  }

  public AD5259(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ad5259 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }

  
    public void AD5259_WriteRDAC()
    {
 
       
        byte[] b = new byte[2];
        b[0] = (byte) 0x00;
        
       for(int i=0;i<255;i++)
            {
            
                try{
                    
                    b[1] = (byte)i;

                    ad5259.write(b,0,2);
                    
                System.out.println("tap " + i);
   
                    Thread.sleep(500);
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
   
  //final NumberFormat NF = new DecimalFormat("##00.00");
      AD5259 sensor = new AD5259();
while(true)
{
      sensor.AD5259_WriteRDAC();
 
}
  }


}

