import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class BH1715
{
    
    public final static int BH1715_ADDRESS = 0x23;
    
    byte[] b = new byte[2];
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice bh1715;
    
    public BH1715()
    {
        this(BH1715_ADDRESS);
    }
    
    public BH1715(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            bh1715 = bus.getDevice(address);
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
           bh1715.write((byte)0x01); //power on

	   Thread.sleep(20);		
           bh1715.write((byte)0x10); // Continous conversion with 1 lx resolution

            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    public void getData(){
        
        
        int raw_high = 0;
        int raw_low  = 0;
        
        int output = 0;
        
        try {
            
            bh1715.read(b,0,2);
            
            raw_high =   ( b[0] & 0xff );
            raw_low  =    ( b[1] & 0xff );

	 Thread.sleep(200);
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
    
        output  = raw_high << 8 | raw_low ;
        
        System.out.println("lux output : " + output+  "  lux" );
        
    }
    
    public static void main(String args[])
    {
        
        BH1715 sensor =  new BH1715();
        
       sensor.setConfig();
        
        while(true){
            
            sensor.getData();
            
        }
        
        
    }
    
    
}
