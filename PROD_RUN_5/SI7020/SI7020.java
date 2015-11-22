import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class SI7020
{
    
    public final static int SI7021_ADDRESS = 0x40;
    
    /* Si7021 CMD Code */
    public final static int Measure_RH_M = 0xE5;
    public final static int Measure_T_M  = 0xE0;
  
    /* Coefficients */
    public final static double TEMPERATURE_OFFSET  =  46.85;
    public final static double TEMPERATURE_MULTIPLE = 175.72;
    public final static long TEMPERATURE_SLOPE   = 65536;
    public final static int HUMIDITY_OFFSET    =  6;
    public final static int HUMIDITY_MULTIPLE  =  125;
    public final static long HUMIDITY_SLOPE     =  65536;
    
    byte[] b = new byte[2];
    
    double temperature =0.0;
    double humidity =0.0;

    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice si7021;
    
    
    public SI7020()
    {
        this(SI7021_ADDRESS);
    }
    
    public SI7020(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            si7021 = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    public void setConfig(byte mode){
        
        try
        {
            si7021.write((byte)mode);
	Thread.sleep(100);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    public double readTemp(){
        
        int temp = 0;
        
        try {
            
            si7021.read(b , 0 , 2);
            temp = (( b[0] & 0xff ) << 8) | ( b[1] & 0xff );
Thread.sleep(100);
        }
        
        catch(Exception ex){ex.printStackTrace(); }
        
        temperature = ( temp * TEMPERATURE_MULTIPLE) / TEMPERATURE_SLOPE - TEMPERATURE_OFFSET;

        
        // Convert to a floating point value
        return temperature;
        
    }
    
    public double readHum(){
        
        int hum = 0;
        
        try {
            
            si7021.read(b , 0 , 2);
           
            hum = (( b[0] & 0xff ) << 8) | ( b[1] & 0xff );
Thread.sleep(100);
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
        
        humidity = (hum * HUMIDITY_MULTIPLE) / HUMIDITY_SLOPE - HUMIDITY_OFFSET;
        
        // Convert to a floating point value
        return humidity;
        
    }
    
    
    public static void main(String args[])
    {
        
        SI7020 sensor =  new SI7020();
        
        // sensor.setConfig(config);
        
        while(true){
            
            sensor.setConfig((byte)Measure_RH_M);
           
            System.out.println("HUMIDITY :  "  +  sensor.readHum());
            
            sensor.setConfig((byte)Measure_T_M);
            
            System.out.println("TEMPERATURE :  "  +  sensor.readTemp());
            
        }
        
        
    }
    

}
