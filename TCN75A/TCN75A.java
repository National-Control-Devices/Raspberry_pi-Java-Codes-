import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class TCN75A
{


    public final static int TCN75A_ADDRESS = 0x48;
    public final static int TCN75A_ADDRESS_MASK=0x07;
    
    public final static int REG_TEMP=0x00;
    public final static int REG_CONFIG=0x01;
    
    public final static int CONFIG_ONE_SHOT=(1<<7);
    public final static int CONFIG_12BIT=(3<<5);
    public final static int CONFIG_SHUTDOWN=(1<<0);
    
    
    public final static int Single_Ended_Input = 0x00;
    public final static int Differential_Input = 0x01;
    
    public final static int Power_Down = 0x01;
    public final static int Power_Down_Selection = 0x00;
    
    
    
    
    byte[] b = new byte[2];
    int result = 0;


    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice tcn75a;
 
    public TCN75A()
    {
        this(TCN75A_ADDRESS);
    }

  public TCN75A(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      tcn75a = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }



    public void setConfig(){
    
        try
        {
            
            tcn75a.write(REG_CONFIG, (byte) (CONFIG_ONE_SHOT|CONFIG_12BIT|CONFIG_SHUTDOWN));
            
            
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
        
         //b = (byte)TCN75A.read((byte)0x00);
         
         tcn75a.read(REG_TEMP,b,0,2);
         
         int msb = b[0] ;
         int lsb = b[1] & 0xFF;
         
         result = (msb << 8)  | lsb;
         
         System.out.println("Temprature : "  + (double)result/256 + " ˚C");
    
     }
   
    catch(Exception ex){ex.printStackTrace(); }
        
        return result;
        
 }


public static void main(String args[])
{
     TCN75A sensor =  new TCN75A();  

     while(true){  
     sensor.readData();
    
   }


}


}

