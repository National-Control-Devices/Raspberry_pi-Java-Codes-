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
public class AD5252
{
  public final static int AD5252_ADDRESS = 0x2C;
  
    // AD5252 Registers
    
    /* Instruction byte */
    public final static int AD5252_CMD_REG     =  (1 << 7);
    public final static int AD5252_EE_RDAC      = (1 << 5);
    
    /* Addresses for Writing Data Byte Contents to RDAC Registers */
    public final static int AD5252_RDAC1     =    0x01; // For quad-channel device software
    public final static int AD5252_RDAC3      =   0x03 ;// compatibility, the dual potentiometers in
    // the parts are designated as RDAC1 and RDAC3
    /* EEMEM Registers */
    public final static int AD5252_EEMEM1    =    0x01 ;// Store RDAC1 setting to EEMEM1
    public final static int AD5252_EEMEM3    =    0x03 ;// Store RDAC3 setting to EEMEM3
    public final static int AD5252_EEMEM4    =    0x04 ;// Store user data to EEMEM4
    public final static int AD5252_EEMEM5    =    0x05 ;// Store user data to EEMEM5
    public final static int AD5252_EEMEM6    =    0x06 ;// Store user data to EEMEM6
    public final static int AD5252_EEMEM7    =    0x07 ;// Store user data to EEMEM7
    public final static int AD5252_EEMEM8    =    0x08 ;// Store user data to EEMEM8
    public final static int AD5252_EEMEM9    =    0x09 ;// Store user data to EEMEM9
    public final static int AD5252_EEMEM10   =    0x0A ;// Store user data to EEMEM10
    public final static int AD5252_EEMEM11   =    0x0B ;// Store user data to EEMEM11
    public final static int AD5252_EEMEM12   =    0x0C ;// Store user data to EEMEM12
    public final static int AD5252_EEMEM13   =    0x0D ;// Store user data to EEMEM13
    public final static int AD5252_EEMEM14   =    0x0E ;// Store user data to EEMEM14
    public final static int AD5252_EEMEM15   =    0x0F ;// Store user data to EEMEM15
    
    /* Intructions */
    public final static int AD5252_NOP		=	 0x00 ;// NOP
    public final static int AD5252_RESTORE  =     0x01 ;// Restore EEMEM (A1, A0) to RDAC (A1, A0)
    public final static int AD5252_STORE    =     0x02 ;// Store RDAC (A1, A0) to EEMEM (A1, A0)
    public final static int AD5252_6DBDOWN	=     0x03 ;// Decrement RDAC (A1, A0) 6 dB
    public final static int AD5252_6DBDOWNALL =	 0x04 ;// Decrement all RDACs 6 dB
    public final static int AD5252_1STEPDOWN	= 0x05 ;// Decrement RDAC (A1, A0) one step
    public final static int AD5252_1STEPDOWNALL	= 0x06 ;// Decrement all RDACs one step
    public final static int AD5252_RESETALL	=	 0x07 ;// Reset: restore EEMEMs to all RDACs
    public final static int AD5252_6DBUP	 =    0x08 ;// Increment RDACs (A1, A0) 6 dB
    public final static int AD5252_6DBUPALL   =   0x09 ;// Increment all RDACs 6 dB
    public final static int AD5252_1STEPUP     =  0x0A ;// Increment RDACs (A1, A0) one step
    public final static int AD5252_1STEPUPALL   = 0x0B ;// Increment all RDACs one step
    
    public final static int AD5252_INSTRUCTION(int x)
    {
    return (((x) & 0x0F) << 3);
    }
    
    
    public final static int AD5252_TOLR1INT    =  0x1A ;// Sign,7bit integer values of RDAC1 tolerance
    public final static int AD5252_TOLR1DEC    =  0x1B ;// 8-bit decimal value of RDAC1 tolerance
    public final static int AD5252_TOLR2INT    =  0x1E ;// Sign,7bit integer values of RDAC3 tolerance
    public final static int AD5252_TOLR2DEC    =  0x1F ;// 8-bit decimal value of RDAC3 tolerance
    
   
  private static boolean verbose = "true".equals(System.getProperty("htu21df.verbose", "false"));

  private I2CBus bus;
  private I2CDevice ad5252;

  public AD5252()
  {
    this(AD5252_ADDRESS);
  }

  public AD5252(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ad5252 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }


  
    public void AD5252_WriteRDAC(int data)
    {


       byte b = (byte)( data ); 

        //b[0]=(byte)(data & 0xFF);
 
        try{
            
       for(int i=0;i<255;i++)
            {
            
               ad5252.write(AD5252_RDAC1,(byte)i);
            
                System.out.println("tap  " + i); 

                
                try{
                    Thread.sleep(50);
                }
                catch (Exception ex)
                {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
            
            } 
       
        

	}
        
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }



    }



 public int AD5252_ReadRDAC(int rDacAddress)
    {
        
        byte[] i2cWord = new byte[2];
        
        int dataRead = 0;
        
        i2cWord[0] = AD5252_ADDRESS;
        i2cWord[1] = (byte)( AD5252_INSTRUCTION(AD5252_NOP) | (byte)rDacAddress);
        
        
        try{
            
            ad5252.write(i2cWord[1]);
            
            i2cWord[1] = (byte)0xFF;
            
            ad5252.read(i2cWord,0,1);
            //I2C_Read(i2cWord, 1);
            dataRead = (int)i2cWord[0];
        }
        
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
        return(dataRead);
    }

  
  
    
    public int AD5252_ReadEEMEM(int memAddress)
    {
     
        byte[] i2cWord = new byte[2];
        
        int dataRead = 0;
        
        i2cWord[0] = AD5252_ADDRESS;
        i2cWord[1] = (byte)( AD5252_EE_RDAC | (byte)memAddress);
      
        try{
        

        ad5252.write(i2cWord[1]);
        
        i2cWord[1] = (byte)0xFF;
        
        ad5252.read(i2cWord,0,1);
        //I2C_Read(i2cWord, 1);
        dataRead = (int)(i2cWord[0] & 0xFF);
        }
        
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        


return(dataRead);
    }
    
  
    
  public static void main(String[] args)
  {
    final NumberFormat NF = new DecimalFormat("##00.00");
    AD5252 sensor = new AD5252();
    float hum = 0;
    float temp = 0;
 
     try
      {
       sensor.AD5252_WriteRDAC(75000); 
 
      }
      catch (Exception ex)
      {
          System.err.println(ex.getMessage());
          ex.printStackTrace();
      } 
      
      
  
  }
}

