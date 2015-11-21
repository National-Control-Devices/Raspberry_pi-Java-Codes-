
import java.nio.ByteBuffer;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

/*
 * Humidity, Temperature
 */
public class AD7999
{
    public final static int AD7999_ADDRESS = 0x29;
    
    // AD7999 Registers
    
    
    public final static int  AD7999_REG_INP    =   0x00;
    public final static int  AD7999_REG_OUT    =   0x01;
    public final static int  AD7999_REG_POL    =   0x02;
    public final static int  AD7999_REG_CTR    =   0x03;
    
    
    private static boolean verbose = true ; //.equals(System.getProperty("htu21df.verbose", "false"));
    
    private I2CBus bus;
    private I2CDevice ad7999;
    
    public AD7999()
    {
        this(AD7999_ADDRESS);
    }
    
    public AD7999(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            ad7999 = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    public void read_input(int chan){
    
        byte[] b= new byte[2];
        
        int result = 0;
        int channel = 0;
        
        try{
            
            ad7999.write((byte)chan);
            
            Thread.sleep(100);
            
            ad7999.read(b, 0, 2);
            //channel = b[0] >>4 ;
            result = ( b[0] & 0x0f) <<8 |  b[1] & 0xff;
           
            channel = b[0] >>4;
            
            System.out.println("Reading from channel  "  + channel);

            System.out.println("value  : "  + result  );
            Thread.sleep(100);
        
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    public void send_output(int data)
    {
        
        
        byte b = (byte)(data);
        
 
        try{
            
            ad7999.write(AD7999_REG_OUT,b);
            
            
        }
        
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
        
    }
    
    
    public static void main(String[] args)
    {
        
        AD7999 sensor = new AD7999();
        
        while(true)
        {
        
            sensor.read_input(0x10);
            sensor.read_input(0x20);
            sensor.read_input(0x40);
            sensor.read_input(0x80);
            
        }
        
        
        
    }
}

