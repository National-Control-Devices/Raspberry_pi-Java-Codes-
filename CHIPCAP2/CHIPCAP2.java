import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class CHIPCAP2
{
    
    public final static int CHIPCAP2_ADDRESS = 0x28;
    
    byte[] b = new byte[4];
    int result = 0;
    
    double humidity = 0;
    double temperatureC = 0;
    double temperatureF = 0;
    
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice device;
    
    public CHIPCAP2()
    {
        this(CHIPCAP2_ADDRESS);
    }
    
    public CHIPCAP2(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            device = bus.getDevice(address);
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
            device.write(config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
    }
    
    
    
    public void readData(){
        
        //byte b=0;
        
        try {
            
            //b = (byte)CHIPCAP2.read((byte)0x00);
            
            device.read(b,0,4);
            
            humidity = (((b[0] & 63) << 8) + b[1]) / 163.84;
            temperatureC = (((b[2] << 6) + (b[3] / 4)) / 99.29) - 40;
            temperatureF = temperatureC * 1.8 + 32;
            
            System.out.println("TEMP(˚C )  :  "  + temperatureC);
            System.out.println("TEMP( F )  :  "  + temperatureF);
            System.out.println("HUMIDITY :  "  + humidity);
            
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
        
    }
    
    
    public static void main(String args[])
    {
        CHIPCAP2 sensor =  new CHIPCAP2();  
        
        while(true){  
            sensor.readData();
            
        }
        
        
    }
    
    
}

