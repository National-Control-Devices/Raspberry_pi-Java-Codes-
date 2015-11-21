

import java.io.IOException;


import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class ADS1100
{
    
    private static int ADS1100_ADDRESS = 0x48; // Disable the
    
    private static boolean verbose = true;//.equals(System.getProperty("htu21df.verbose", "false"));
    
    private I2CBus bus;
    private I2CDevice ads1100;
    
    public ADS1100()
    {
        this(ADS1100_ADDRESS);
    }
    
    public ADS1100(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            ads1100 = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    
    
    public int read16Bits()
    {
        byte[] buffer = new byte[2];
       
        try
        {
            ads1100.read(buffer, 0, buffer.length);
            
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        
        
        int msb = buffer[0];
        int lsb = buffer[1];
        if (lsb < 0)
            lsb = 256 + lsb;
        
        return ((msb << 8) + lsb);
        
    }
    
    void write16Bits(int address, int value) throws IOException
    {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) (value << 8);// msb
        buffer[1] = (byte) (value & 0xFF); // lsb
        
        try
        {
            ads1100.write(address, buffer, 0, buffer.length);
            
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        
        
        
    }
    
    public static void main(String[] args)
    {
        
        ADS1100 sensor = new ADS1100();
        
        while(true)
        {
            System.out.println("OUTPUT  :  " + sensor.read16Bits());
            
        }
        
    }
    
    
}

