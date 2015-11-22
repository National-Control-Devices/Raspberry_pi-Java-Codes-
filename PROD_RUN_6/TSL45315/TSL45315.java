import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.lang.Math;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class TSL45315
{
    
    
    public final static int TSL_ADDRESS = 0x29 ;
    
    byte[] data = new byte[2];
    
    
    private static boolean verbose = "true".equals(System.getProperty("htu21df.verbose", "false"));
    
    //  private static boolean verbose = "true";
    
    private I2CBus bus;
    private I2CDevice tsl45315;
    
    //private double lux = 0.0;
    private double tempC = 0.0;
    
    private double status = 0.0;

    private long lux =0 ;


    
    
    public TSL45315()
    {
        this(TSL_ADDRESS);
    }
    
    public TSL45315(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            tsl45315 = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    
    public  void fetchData(){
        
        try
        {
            tsl45315.write( (byte) 0x80);
        
    		tsl45315.write( (byte) 0x03);
        
}
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        try {
            tsl45315.read(0x84,data,0,2);
		Thread.sleep(200);
        }
        catch(Exception ex){ex.printStackTrace(); }
        
         lux = ((data[1] & 0xFF) <<8) |  (data[0] & 0xFF )    ;
         
	System.out.println(lux);	
	
    }
     
    
    public static void main(String args[])
    {
        TSL45315 sensor =  new  TSL45315();
        
        while(true){
          
	    sensor.fetchData();
             
        }
        
        
    }
    
    
}
