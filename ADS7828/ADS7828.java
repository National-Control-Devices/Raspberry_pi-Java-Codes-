
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class ADS7828
{


    public final static int ADS7828_ADDRESS = 0x48;
    
    public final static int CH0_Analog_Input_Channel = 0x00;
    public final static int CH1_Analog_Input_Channel = 0x01;
    public final static int CH2_Analog_Input_Channel = 0x02;
    public final static int CH3_Analog_Input_Channel = 0x03;
    public final static int CH4_Analog_Input_Channel = 0x04;
    public final static int CH5_Analog_Input_Channel = 0x05;
    public final static int CH6_Analog_Input_Channel = 0x06;
    public final static int CH7_Analog_Input_Channel = 0x07;
    
    public final static int Single_Ended_Input = 0x00;
    public final static int Differential_Input = 0x01;
    
    public final static int Power_Down = 0x01;
    public final static int Power_Down_Selection = 0x00;
    
    
    
    
    byte[] b = new byte[2];
    int result = 0;


    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice ads7828;
 
    public ADS7828()
    {
        this(ADS7828_ADDRESS);
    }

  public ADS7828(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      ads7828 = bus.getDevice(address);
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
            ads7828.write(config);
            
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
        
         //b = (byte)ADS7828.read((byte)0x00);
         
         ads7828.read(b,0,2);
         
         int msb = b[0] & 0XFF;
         int lsb = b[1] & 0XFF;
         
         result = (msb << 8)  | lsb;
         
         System.out.println("VALUE : "  + result);
    
     }
   
    catch(Exception ex){ex.printStackTrace(); }
        
        return result;
        
 }


public static void main(String args[])
{
     ADS7828 sensor =  new ADS7828();  

     while(true){  
   sensor.setConfig((byte)0x00); 
   sensor.readData();
    
   }


}


}

