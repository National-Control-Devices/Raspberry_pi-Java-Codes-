import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class LIS331
{
    
    public final static int LIS331_ADDRESS = 0x18;
    public final static int CTRL_REG1 = 0x20;
    public final static int CTRL_REG1_PM = 1<<5 ;
	

    
    public final static int adr_fs2 = 0x00;
    public final static int adr_fs4 = 0x10;
    public final static int adr_fs8 = 0x30;
     
    public final static double SENS_FS2 = 0.001;
    public final static double SENS_FS4 = 0.002;
    public final static double SENS_FS8 = 0.0039;
    public  static int _ctrlReg1 = 0x07;
    byte[] b = new byte[6];
    int result = 0;
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice lis331;
    
    public LIS331()
    {
        this(LIS331_ADDRESS);
    }
    
    public LIS331(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            lis331 = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
   

	public void sleep(boolean enable){
        
        try
        {

	if (enable)
        _ctrlReg1 &= ~CTRL_REG1_PM;
    	
	else
        _ctrlReg1 |= CTRL_REG1_PM;

    		lis331.write(0x20,(byte)_ctrlReg1);  		  
        		
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }

	public void setRange(){
        
        try
        {
		lis331.write(0x23,(byte)adr_fs8);  //set senstivity		  
          
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    public void setConfig(){
        
        try
        {
            lis331.write(0x20,(byte)0x27);
            lis331.write(0x23,(byte)0x00);
            
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
            	b[0]  = (byte)lis331.read(0x28);		
		b[1]  = (byte)lis331.read(0x29);		

	    raw_X = ( b[1]   <<8 ) | ( b[0] & 0xFF );
            

		b[0]  = (byte)lis331.read(0x2A);		
		b[1]  = (byte)lis331.read(0x2B);		
	
	raw_Y = ( b[1]   <<8 ) | ( b[0] & 0xFF  );
            
	
		b[0]  = (byte)lis331.read(0x2C);		
		b[1]  = (byte)lis331.read(0x2D);		
        
	raw_Z = ( b[1]   <<8 ) | ( b[0] & 0xFF  );
             
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
    }
    
    
    public static void main(String args[])
    {
        LIS331 sensor =  new LIS331();  

	//sensor.setRange();
  	//sensor.sleep(false);

	sensor.setConfig();      
     
	   while(true){  
    
            sensor.readAccData();
            
        }
        
        
    }
    
    
}
