import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MPU6000
{
    
    public final static int MPU9150_ADDRESS = 0x68;
    
    // MPU 9150 registers
    public final static int MPUREG_SMPLRT_DIV =  0x19; //
    public final static int MPUREG_CONFIG  = 0x1A; //
    public final static int MPUREG_GYRO_CONFIG  = 0x1B;
    public final static int MPUREG_ACCEL_CONFIG  = 0x1C;
    public final static int MPUREG_FIFO_EN  = 0x23;
    public final static int MPUREG_INT_PIN_CFG =  0x37;
    public final static int MPUREG_INT_ENABLE  = 0x38;
    public final static int MPUREG_INT_STATUS  =  0x3A;
    public final static int MPUREG_ACCEL_XOUT_H = 0x3B; //
    public final static int MPUREG_ACCEL_XOUT_L = 0x3C; //
    public final static int MPUREG_ACCEL_YOUT_H = 0x3D; //
    public final static int MPUREG_ACCEL_YOUT_L = 0x3E; //
    public final static int MPUREG_ACCEL_ZOUT_H = 0x3F; //
    public final static int MPUREG_ACCEL_ZOUT_L = 0x40; //
    public final static int MPUREG_TEMP_OUT_H = 0x41;//
    public final static int MPUREG_TEMP_OUT_L = 0x42;//
    public final static int MPUREG_GYRO_XOUT_H = 0x43; //
    public final static int MPUREG_GYRO_XOUT_L = 0x44; //
    public final static int MPUREG_GYRO_YOUT_H = 0x45; //
    public final static int MPUREG_GYRO_YOUT_L = 0x46; //
    public final static int MPUREG_GYRO_ZOUT_H = 0x47; //
    public final static int MPUREG_GYRO_ZOUT_L = 0x48; //
    public final static int MPUREG_USER_CTRL = 0x6A; //
    public final static int MPUREG_PWR_MGMT_1 = 0x6B; //
    public final static int MPUREG_PWR_MGMT_2 = 0x6C; //
    public final static int MPUREG_FIFO_COUNTH = 0x72;
    public final static int MPUREG_FIFO_COUNTL  = 0x73;
    public final static int MPUREG_FIFO_R_W  = 0x74;
    public final static int MPU9150_CMPS_XOUT_L      =  0x03;
    
    
    public final static int MPU9150_I2C_ADDRESS = 0x0C;      //change Adress to Compass
   
    
    
    
    byte[] b = new byte[6];
    int result = 0;
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice mpu9150;
    
    public MPU6000()
    {
        this(MPU9150_ADDRESS);
    }
    
    public MPU6000(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            mpu9150 = bus.getDevice(address);
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
            mpu9150.write(0x6B,(byte)0x00);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    
    public void setCompass(){
        
       try
        {
            
            mpu9150.write(0x0A, (byte)0x00); //PowerDownMode
            mpu9150.write(0x0A, (byte)0x0F); //SelfTest
            mpu9150.write(0x0A, (byte)0x00); //PowerDownMode
            
        /*
        mpu9150.write(0x24,(byte) 0x40); //Wait for Data at Slave0
        mpu9150.write(0x25,(byte)0x8C); //Set i2c address at slave0 at 0x0C
        mpu9150.write(0x26,(byte)0x02); //Set where reading at slave 0 starts
        mpu9150.write(0x27,(byte) 0x88); //set offset at start reading and enable
        mpu9150.write(0x28,(byte) 0x0C); //set i2c address at slv1 at 0x0C
        mpu9150.write(0x29,(byte) 0x0A); //Set where reading at slave 1 starts
        mpu9150.write(0x2A,(byte)0x81); //Enable at set length to 1
        mpu9150.write(0x64,(byte) 0x01); //overvride register
        mpu9150.write(0x67,(byte) 0x03); //set delay rate
        mpu9150.write(0x01,(byte) 0x80);
        
        mpu9150.write(0x34,(byte) 0x04); //set i2c slv4 delay
        mpu9150.write(0x64,(byte) 0x00); //override register
        mpu9150.write(0x6A,(byte) 0x00); //clear usr setting
        mpu9150.write(0x64,(byte) 0x01); //override register
        mpu9150.write(0x6A,(byte) 0x20); //enable master i2c mode
        mpu9150.write(0x34,(byte) 0x13); //disable slv4
        
            */
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
            mpu9150.read(MPUREG_ACCEL_XOUT_H,b,0,6);
            
            raw_X = ( b[0] ) <<8 | ( b[1] & 0xFF );
            raw_Y = ( b[2] ) <<8 | ( b[3] & 0xFF  );
            raw_Z = ( b[4] ) <<8 | ( b[5] & 0xFF  );
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
	System.out.println(" X ACC : "  +  raw_X/16384.0 + " g");
        System.out.println(" Y ACC : "  +  raw_Y/16384.0 + " g");
        System.out.println(" Z ACC : "  +  raw_Z/16384.0 + " g");
        
    }
    
    public void readGyroData()
    {
        
        int raw_X = 0 ;
        int raw_Y = 0;
        int raw_Z = 0;
        
        try
        {
            mpu9150.read(MPUREG_GYRO_XOUT_H,b,0,6);
            
            raw_X = ( b[0] ) <<8 | ( b[1] & 0xFF );
            raw_Y = ( b[2] ) <<8 | ( b[3] & 0xFF  );
            raw_Z = ( b[4] ) <<8 | ( b[5] & 0xFF  );
            
           Thread.sleep(200); 
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
        System.out.println(" X GYRO : "  +  raw_X/131 + " /s");
        System.out.println(" Y GYRO : "  +  raw_Y/131 + " /s");
        System.out.println(" Z GYRO : "  +  raw_Z/131 + " /s");
        
    }
    
   
    public void readMagData()
    {
        
        int raw_X = 0 ;
        int raw_Y = 0;
        int raw_Z = 0;
        
        try
        {
            mpu9150.read(MPU9150_CMPS_XOUT_L,b,0,6);
            
            raw_X = ( b[0] ) <<8 | ( b[1] & 0xFF );
            raw_Y = ( b[2] ) <<8 | ( b[3] & 0xFF  );
            raw_Z = ( b[4] ) <<8 | ( b[5] & 0xFF  );
           Thread.sleep(200); 
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        
        System.out.println(" X MAG : "  +  raw_X);
        System.out.println(" Y MAG : "  +  raw_Y);
        System.out.println(" Z MAG : "  +  raw_Z);
        
    }
    
    

    
    public static void main(String args[])
    {
        MPU6000 sensor =  new MPU6000();  
        
            sensor.setConfig();
        
        while(true){  
    
            
            sensor.readAccData();
            sensor.readGyroData();
       
           // sensor.readMagData();
            
        }
        
        
    }
    
    
}

