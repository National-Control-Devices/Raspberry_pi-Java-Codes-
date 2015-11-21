import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class HDC1000
{

    public final static int HDC1000_ADDRESS = 0x40;
    
    
    byte[] b = new byte[2];

    double humidity =0.0;
    double temperature =0.0;
    
    
    public final static int HDC1000_TEMP	= 0x00;
    public final static int HDC1000_HUMI	= 0x01;
    public final static int HDC1000_CONFIG	= 0x02;
    
    
    public final static int HDC1000_RST				=	0x80;
    public final static int	HDC1000_HEAT_ON			=	0x20;
    public final static int	HDC1000_HEAT_OFF		=	0x00;
    public final static int	HDC1000_BOTH_TEMP_HUMI	=	0x10;
    public final static int	HDC1000_SINGLE_MEASUR	=	0x00;
    public final static int	HDC1000_TEMP_HUMI_14BIT	=	0x00;
    public final static int	HDC1000_TEMP_11BIT		=	0x40;
    public final static int HDC1000_HUMI_11BIT		=	0x01;
    public final static int	HDC1000_HUMI_8BIT		=	0x02;
    

    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice hdc1000;
 
    public final static byte config = HDC1000_BOTH_TEMP_HUMI | HDC1000_TEMP_HUMI_14BIT | HDC1000_HEAT_ON ;

    
    public HDC1000()
    {
        this(HDC1000_ADDRESS);
    }

  public HDC1000(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      hdc1000 = bus.getDevice(address);
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
            hdc1000.write(HDC1000_CONFIG , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    
    }
    

    public double readTemp(){
     
        byte[] b = new byte[2];
        
        try {
            hdc1000.write((byte)0x00);

            Thread.sleep(100);
            hdc1000.read(b , 0 , 2);
            
            int msb = b[0] & 0xff ;
            int lsb = b[1] & 0xff;
            
            int result  = ( msb << 8 ) | lsb ;
            
            
            temperature  = result/65536.0*165.0-40.0;
            
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
    
        return temperature;
    
    
    }
    
    public double readHum(){
        
        byte[] b = new byte[2];
        
        try {
         hdc1000.write((byte)0x01);
        Thread.sleep(100); 
            hdc1000.read( b , 0 , 2);
            
            int msb = b[0] & 0xff;
            int lsb = b[1] & 0xff;
            
            int result  = ( msb << 8 ) | lsb ;
            
            
            humidity  = result/65536.0*100.0;
            
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
        
        return humidity;
        
        
    }
    
   


public static void main(String args[])
{
     HDC1000 sensor =  new HDC1000();  

    
    sensor.setConfig(config);
    
     while(true){
      
         
         System.out.println("TEMPERATURE :  "  +  sensor.readTemp());
        
         System.out.println("HUMIDITY :  "  +  sensor.readHum());
         
       
   }


}


}

