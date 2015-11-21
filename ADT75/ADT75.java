import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class ADT75
{


    public final static int ADT75_ADDRESS = 0x48;
    
    public final static int TEMP_REG =  0x00;
    public final static int CONFIG_REG = 0x01;
    
    
    byte[] b = new byte[2];
    int result = 0;


    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice adt75;
 
    public ADT75()
    {
        this(ADT75_ADDRESS);
    }

  public ADT75(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      adt75 = bus.getDevice(address);
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
            adt75.write(config);
            
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
        
         //b = (byte)ADT75.read((byte)0x00);
         
         adt75.read(TEMP_REG,b,0,2);
         
         int msb = b[0] ;
         int lsb = b[1] & 0XFF;
         
         result = (msb << 8)  | lsb ;
         result = result >> 4 ;
         
         
         System.out.println("TEMP :  "  + (double)result/16 + " ˚C");
    
         Thread.sleep(200);
     }
   
    catch(Exception ex){ex.printStackTrace(); }
        
        return result;
        
 }


public static void main(String args[])
{
     ADT75 sensor =  new ADT75();  

     while(true){
         
     sensor.readData();
    
   }


}


}

