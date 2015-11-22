import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MMA7455
{
    
    public final static int MMA7455_ADDRESS = 0x1D;
   
  
 
    
    byte[] b = new byte[6];
    int result = 0;
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice mma7455;
    
    public MMA7455()
    {
        this(MMA7455_ADDRESS);
    }
    
    public MMA7455(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            mma7455 = bus.getDevice(address);
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
            mma7455.write(0x16 ,(byte)0x01);
            mma7455.write(0x06 ,(byte)0x00);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    public void readAccData()
    {
        
        int raw_X = 0 ;
        int raw_Y = 0;
        int raw_Z = 0;
        
        try
        {
            mma7455.read(0x00,b,0,6);
            
            raw_X = ( b[1]  <<8 ) | ( b[0]  );
            raw_Y = ( b[3]  <<8 ) | ( b[2]  );
            raw_Z = ( b[5]  <<8 ) | ( b[4]  );
            
            Thread.sleep(400);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        System.out.println(" X ACC : "  +  raw_X );
        System.out.println(" Y ACC : "  +  raw_Y );
        System.out.println(" Z ACC : "  +  raw_Z );
        
        
        //convert Acclelerometer values to degrees
        double AcclXangle =  (Math.atan2(raw_Y,raw_Z)+3.14)*57.3;
        double AcclYangle =  (Math.atan2(raw_Z,raw_X)+3.14)*57.3;
        
        
        System.out.println(" AcclXangle  "  + AcclXangle );
        System.out.println(" AcclYangle  "  + AcclYangle );
        
        
    }
    
    
    public static void main(String args[])
    {
        MMA7455 sensor =  new MMA7455();  
        
        
        
        while(true){  
    
            sensor.setConfig();
            
            sensor.readAccData();
            
            
            
            
        }
        
        
    }
    
    
}

