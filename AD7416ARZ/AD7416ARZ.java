import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class AD7416ARZ
{


    public final static int AD7416ARZ_ADDRESS = 0x48;
    
    public final static int TEMP_REG =  0x00;
    public final static int CONFIG_REG = 0x01;
    public final static int TEMP_HYST = 0x03;
    
    
    
    byte[] b = new byte[2];
    int result = 0;


    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice ad7416arz;
 
    public AD7416ARZ()
    {
        this(AD7416ARZ_ADDRESS);
    }

  public AD7416ARZ(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ad7416arz = bus.getDevice(address);
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
            ad7416arz.write(CONFIG_REG , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    
    }
    
    public int readData(){

     //byte b=0;
        
     try { 
        
         //b = (byte)AD7416ARZ.read((byte)0x00);
         
         ad7416arz.read(TEMP_REG,b,0,2);
         
         int msb = b[0] ;
         int lsb = b[1] & 0XFF;
         
         result = (msb << 8)  | lsb ;
         result  = result >> 6 ;
         
         System.out.println("TEMP :  "  + (double)result/4);
    
     }
   
    catch(Exception ex){ex.printStackTrace(); }
        
        return result;
        
 }


public static void main(String args[])
{
     AD7416ARZ sensor =  new AD7416ARZ();  

     while(true){  
     sensor.readData();
    
   }


}


}

