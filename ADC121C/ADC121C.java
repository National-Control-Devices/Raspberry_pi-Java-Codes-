import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class ADC121C
{


   public final static int ADC121C_ADDRESS = 0x50;
   public final static int CONFIG_REGISTER_ADDRESS = 0x02 ;
    
    byte[] data = new byte[2];
    int result = 0;


    private static boolean verbose = true;//"true".equals(System.getProperty("htu21df.verbose", "false"));

//  private static boolean verbose = "true";
  
  private I2CBus bus;
  private I2CDevice adc121c;
  //private int result = 0.0;


 public ADC121C()
{
  this(ADC121C_ADDRESS);
}

  public ADC121C(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      adc121c = bus.getDevice(address);
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
            adc121c.write(CONFIG_REGISTER_ADDRESS , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    
    }
    
    public  int fetchData(){

      
        
     try { 
        
         adc121c.read(data,0,2);
         
         
         int lsb = data[0] & 0XFF;
         int msb = data[1] & 0X0F;
         
          result = (msb<<8) | lsb;
        
     }
   
    catch(Exception ex){ex.printStackTrace(); }
        
        return result;
        
 }

 


public static void main(String args[])
{
     ADC121C sensor =  new ADC121C();  

     while(true){  
     
        System.out.println ( "OUTPUT : " + sensor.fetchData());
    
   }


}


}

