import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class H3LIS331DL
{
    
    public final static int H3LIS331DL_ADDRESS = 0x18;
    
    
    byte[] b = new byte[6];
    
    double ACC_X = 0.0;
    double ACC_Y = 0.0;
    double ACC_Z = 0.0;
    
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice h3liS331dl;
    
    
    
    public H3LIS331DL()
    {
        this(H3LIS331DL_ADDRESS);
    }
    
    public H3LIS331DL(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            h3liS331dl = bus.getDevice(address);
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
            h3liS331dl.write(0x20,(byte)0x27);
            h3liS331dl.write(0x23,(byte)0x00);
            
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
            
            h3liS331dl.read(0x28,b,0,2);
            
            raw_x = (( b[1] & 0xff ) << 8) | ( b[0] & 0xff );
            h3liS331dl.read(0x2A,b,0,2);
           
 	    raw_y = (( b[1] & 0xff ) << 8) | ( b[0] & 0xff );
	    h3liS331dl.read(0x2C,b,0,2);
                        
            raw_z = (( b[1] & 0xff ) << 8) | ( b[0] & 0xff );
            Thread.sleep(300);
        

 		if(raw_x > 32768)
		raw_x = raw_x - 65536;	
 		
	if(raw_y > 32768)
	raw_y = raw_y - 65536;	
        
		 if(raw_z > 32768)
		raw_z = raw_z - 65536;	

} 
        catch(Exception ex){ex.printStackTrace(); }
       
            System.out.println("X : " + raw_x  / 32678.0 + " g" );
            System.out.println("Y : " + raw_y  / 32678.0 + " g");
            System.out.println("Z : " + raw_z  / 32678.0 + " g");
            
    }
    
    
    public static void main(String args[])
    {
        
        H3LIS331DL sensor =  new H3LIS331DL();
        
         sensor.setConfig();
        
        while(true){
            
            sensor.getAcc();
            
        }
        
        
    }
    
    
}
