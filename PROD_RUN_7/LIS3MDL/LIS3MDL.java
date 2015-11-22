import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LIS3MDL
{
    
    public final static int LIS3MDL_ADDRESS = 0x1C;
    
    byte[] b = new byte[6];
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice lis3dml;
    
    public LIS3MDL()
    {
        this(LIS3MDL_ADDRESS);
    }
    
    public LIS3MDL(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            lis3dml = bus.getDevice(address);
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
            // lis3dml.write(CONTROL_REG_ACC_AND_G,(byte)0x00);
            
            //initialise the Magnetometer
            lis3dml.write(0x20,(byte)0xFF);   // Temp enable, M data rate = 50Hz
            lis3dml.write(0x21,(byte)0x60);   // +/-12gauss
            
            
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    
    public void getData(){
        
        int raw_x_g = 0;
        int raw_y_g = 0;
        int raw_z_g = 0;
        
        
        try {
            
            lis3dml.write(0x22,(byte)0x03);   // Continuous-conversion mode
            
            lis3dml.write(0x23,(byte)0x0C);

            //lis3dml.read(0x28,b,0,6);
         
            Thread.sleep(300);
            
            b[0] = (byte) lis3dml.read(0x28);
            b[1] = (byte) lis3dml.read(0x29);
            b[2] = (byte) lis3dml.read(0x2A);
            b[3] = (byte) lis3dml.read(0x2B);
            b[4] = (byte) lis3dml.read(0x2C);
            b[5] = (byte) lis3dml.read(0x2D);
            
            
            raw_x_g = (( b[1] & 0xff ) << 8) | ( b[0] & 0xff );
            raw_y_g = (( b[3] & 0xff ) << 8) | ( b[2] & 0xff );
            raw_z_g = (( b[5] & 0xff ) << 8) | ( b[4] & 0xff );
            
            Thread.sleep(400);
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
        
        System.out.println("RAW OUTPUT");
        
        System.out.println("X_g: " + raw_x_g);
        System.out.println("Y_g : " + raw_y_g);
        System.out.println("Z_g : " + raw_z_g);
        
     
        //Calculate heading
       
        double heading = 180 * Math.atan2(raw_y_g,raw_x_g)/3.14;
        
        
        
        /*
        if (heading < 0)
            heading += 360;
        */
        
        System.out.println("heading : " + heading);
        
        
    }
    
    
    public static void main(String args[])
    {
        
        LIS3MDL sensor =  new LIS3MDL();
        
        sensor.setConfig();
        
        while(true){
            
            sensor.getData();
            
        }
        
        
    }
    
    
}

