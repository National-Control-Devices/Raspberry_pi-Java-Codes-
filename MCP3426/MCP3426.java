import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MCP3426
{
    public final static int MCP3426_ADDRESS = 0x68;
    
    
    public final static byte L = 0;
    public final static byte H = 1;
    public final static byte F = 2;
    
    public final static int CHANNEL_0 = 0;
    public final static int CHANNEL_1 = 1;
    
    public final static int GAIN_1 = 0;
    public final static int GAIN_2 = 1;
    public final static int GAIN_4 = 2;
    public final static int GAIN_8 = 3;
    double VRef = 2.048;
    
    
    
    //communication register
    public final static int BIT_RDY = 7; //data ready
    public final static int BIT_C1 = 6; //channel select
    public final static int BIT_C0 = 5; //channel select
    public final static int BIT_OC = 4; //conversion mode (one shot/continuous)
    public final static int BIT_S1 = 3; //sample rate
    public final static int BIT_S0 = 2; //sample rate
    public final static int BIT_G1 = 1; //gain
    public final static int BIT_G0 = 0; //gain
    
    
    
    private static boolean verbose = "true".equals(System.getProperty("htu21df.verbose", "false"));
    
    private I2CBus bus;
    private I2CDevice mcp3426;
    
    public MCP3426()
    {
        this(MCP3426_ADDRESS);
    }
    
    public MCP3426(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            mcp3426 = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    public double readADC()
    
    {
        
        byte[] b = new byte[3];
        
        try{
            
            Thread.sleep(500);
            
            mcp3426.read(b,0,3);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        int h = b[0] & 0xff;
        int l = b[1] & 0xff;
        int r = b[2] & 0xff;
        
        long t = h << 8 |  l;
        
        
        if (t >= 32768)
            t = 65536l - t;
        
        System.out.println("ADC output :  " + t);
        
        double v = (double) t * VRef/32768.0 ;

        return v;
        
    }
    
    
    public void selectChannel(int channel, int gain)
    {
        
        //configuration register, assuming 16 bit resolution
        byte reg =  (byte)((1 << BIT_RDY) | (channel << BIT_C0) | (1 << BIT_OC) | (1 << BIT_S1) | gain);
        
        try{
            
            mcp3426.write(reg);
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
    }
    
    public static void main(String[] args)
    {
        
        MCP3426 sensor = new MCP3426();
        
        while(true)
        {
            
            sensor.selectChannel(CHANNEL_0,GAIN_1);
            
            System.out.println("OUTPUT FROM CHANNEL 1  : " + sensor.readADC());
            
            sensor.selectChannel(CHANNEL_1,GAIN_1);
            
            System.out.println("OUTPUT FROM CHANNEL 2  : " + sensor.readADC());
            
           
        }
        
        
    }
}


