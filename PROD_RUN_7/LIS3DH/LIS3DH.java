import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class LIS3DH
{
    
    public final static int LIS3DH_ADDRESS = 0x18;
    
    byte[] b = new byte[6];
    int result = 0;
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice lis3dh;
    
    public LIS3DH()
    {
        this(LIS3DH_ADDRESS);
    }
    
    public LIS3DH(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            lis3dh = bus.getDevice(address);
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
            
            //initialise the Acclelerometer
           
           
            lis3dh.write(0x20,(byte)0x27);
            lis3dh.write(0x23,(byte)0x00);
            
            Thread.sleep(200);
            
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
            
            
            //lis3dh.read(0x28,b,0,6);
            
            
            b[0]  = (byte)lis3dh.read(0x28);
            b[1]  = (byte)lis3dh.read(0x29);
            
            raw_X = ( ( b[1] & 0xff ) <<8 ) | ( b[0]  );
            
            Thread.sleep(50);
            
            b[0]  = (byte)lis3dh.read(0x2A);
            b[1]  = (byte)lis3dh.read(0x2B);
            
            
            raw_Y = ( ( b[1] & 0xff )  <<8 ) | ( b[0]  );
            
            Thread.sleep(50);
            
            b[0]  = (byte)lis3dh.read(0x2C);
            b[1]  = (byte)lis3dh.read(0x2D);
            
            raw_Z = ( ( b[1] & 0xff ) <<8 ) | ( b[0]  );
        
            Thread.sleep(50);
            
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
        System.out.println(" X ACC : "  +  raw_X);
        System.out.println(" Y ACC : "  +  raw_Y);
        System.out.println(" Z ACC : "  +  raw_Z);
        
        
    
        //Convert Acclelerometer values to degrees
        double AcclXangle =  (Math.atan2(raw_Y,raw_Z)+3.14)*57.3 ;
        double AcclYangle =  (Math.atan2(raw_Z,raw_X)+3.14)*57.3 ;
        
        System.out.println(" AcclXangle : "  +  AcclXangle);
        System.out.println(" AcclYangle : "  +  AcclYangle);
        
    }
    
    
    public static void main(String args[])
    {
        LIS3DH sensor =  new LIS3DH();  
        
        sensor.setConfig();
        
        while(true){
    
            sensor.readAccData();
            
        }
        
        
    }
    
    
}

