import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MAG3110
{
    
    public final static int MAG3110_ADDRESS = 0x0E;
    
    
    public final static int CTRL_REG1_DR = 0x00;
    public final static int CTRL_REG1_OS = 0x18;
    public final static int CTRL_REG1_STANDBY = (CTRL_REG1_DR |  CTRL_REG1_OS);
 
        private final static int CTRL_REG2_AUTO_MRST_EN = 0x80;
	private final static int CTRL_REG2_RAW = 0x00;
	private final static int CTRL_REG2_MAG_RST = 0x00;


	byte[] b = new byte[6];
    
    
    
	double heading = 0.0;
    
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice mag3110;
    
    
    
    public MAG3110()
    {
        this(MAG3110_ADDRESS);
    }
    
    public MAG3110(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            mag3110 = bus.getDevice(address);
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

           mag3110.write((byte) 0x11, (byte) (CTRL_REG2_AUTO_MRST_EN | CTRL_REG2_MAG_RST | CTRL_REG2_RAW));

			Thread.sleep(15);
	// Write bits in CTRL_REG1 (set output rate and over sample ratio)

	mag3110.write((byte) 0x10,(byte) (0x01 | CTRL_REG1_DR | CTRL_REG1_OS));

			System.out.println("Sensor configuration OK!");
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    
        public void getAcc(){
        
        int raw_x = 0;
        int raw_y = 0;
        int raw_z = 0;
            
        try {
            
            mag3110.read(0x01,b,0,6);
            
            raw_x = (( b[0] & 0xff ) << 8) | ( b[1] & 0xff );
            raw_y = (( b[2] & 0xff ) << 8) | ( b[3] & 0xff );
            raw_z = (( b[4] & 0xff ) << 8) | ( b[5] & 0xff );
            

	Thread.sleep(400);
        }
        
        catch(Exception ex){ex.printStackTrace(); }
       
            System.out.println("X : " + raw_x );
            System.out.println("Y : " + raw_y ) ;
            System.out.println("Z : " + raw_z );
            
 
    }
    
    
    public static void main(String args[])
    {
        
        MAG3110 sensor =  new MAG3110();
        
         sensor.setConfig();
        
        while(true){
            
            
            sensor.getAcc();
            
        }
        
        
    }
    
    
}
