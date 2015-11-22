import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class LSM330
{
    public final static int LSM330_ADDRESS_GYRO = 0x6A;
    
	public final static int LSM330_ADDRESS_ACC = 0x1E;
    
public final static int CTRL_REG1 = 0x20;
    
    byte[] b = new byte[6];
    int result = 0;
    
    public final static int accelSensitivity_ = 2;
    double accelScale_ = 0.0000637; // manually calibrated
    public final static byte accelCtrlReg4Val_ = 0x08;
    
    public final static int gyroSensitivity_ = 250;
    double gyroScale_ = 0.00875; // from the datasheet
    public final static byte gyroCtrlReg4Val_ = 0x00;
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice lsm330;
    
    
  
    
   /* public LSM330()
    {
        this(LSM330_ADDRESS);
    }
    */
    public LSM330(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            lsm330 = bus.getDevice(address);
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
            //LSM330.write(config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    public void readAccData() throws IOException
    {
        
        int raw_X = 0 ;
        int raw_Y = 0;
        int raw_Z = 0;
        
        lsm330.write( CTRL_REG1, (byte)0x97); //select acc.
        
        
        try
        {
            lsm330.read(0x28,b,0,6);
            
            raw_X = ( b[0] ) <<8 | ( b[1] & 0xFF );
            raw_Y = ( b[2] ) <<8 | ( b[3] & 0xFF  );
            raw_Z = ( b[4] ) <<8 | ( b[5] & 0xFF  );
            Thread.sleep(300);
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
        System.out.println(" X ACC : "  +  raw_X * accelScale_ + " g");
        System.out.println(" Y ACC : "  +  raw_Y * accelScale_ + " g");
        System.out.println(" Z ACC : "  +  raw_Z * accelScale_ + " g");
        
    }
    
    public void readGyroData() throws IOException
    {
        
        int raw_X = 0 ;
        int raw_Y = 0;
        int raw_Z = 0;
        
        lsm330.write( CTRL_REG1, (byte)0x0f);   //select gyro
        
        
        try
        {
            lsm330.read(0x28,b,0,6);
            
            raw_X = ( b[0] ) <<8 | ( b[1] & 0xFF );
            raw_Y = ( b[2] ) <<8 | ( b[3] & 0xFF  );
            raw_Z = ( b[4] ) <<8 | ( b[5] & 0xFF  );
		
		Thread.sleep(300);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
        System.out.println(" X GYRO : "  +  raw_X * gyroScale_ + " ˚/s");
        System.out.println(" Y GYRO : "  +  raw_Y * gyroScale_ + " ˚/s");
        System.out.println(" Z GYRO : "  +  raw_Z * gyroScale_ + " ˚/s");
        
    }
    
    
    public static void main(String args[]) throws IOException
    {
        LSM330 sensor_gyro =  new LSM330(LSM330_ADDRESS_GYRO);
        
	LSM330 sensor_acc =  new LSM330(LSM330_ADDRESS_ACC);
        	
        while(true){

            sensor_acc.readAccData();
            
             sensor_gyro.readGyroData();
        }
        
        
    }
    
    
}
