import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class HCPA
{

    public final static int HCPA_ADDRESS = 0x28;
    
    
    byte[] b = new byte[4];

    double humidity =0.0;
    double temperature =0.0;
    double temp_hum=0;
    
    
    public final static int HCPA_TEMP	= 0x00;
    public final static int HCPA_HUMI	= 0x01;
    public final static int HCPA_CONFIG	= 0x02;
    
    
    public final static int HCPA_RST				=	0x80;
    public final static int	HCPA_HEAT_ON			=	0x20;
    public final static int	HCPA_HEAT_OFF		=	0x00;
    public final static int	HCPA_BOTH_TEMP_HUMI	=	0x10;
    public final static int	HCPA_SINGLE_MEASUR	=	0x00;
    public final static int	HCPA_TEMP_HUMI_14BIT	=	0x00;
    public final static int	HCPA_TEMP_11BIT		=	0x40;
    public final static int HCPA_HUMI_11BIT		=	0x01;
    public final static int	HCPA_HUMI_8BIT		=	0x02;
    

    private static boolean verbose = true;

    private I2CBus bus;
    private I2CDevice hcpa;
 
    public final static byte config = HCPA_BOTH_TEMP_HUMI | HCPA_TEMP_HUMI_14BIT | HCPA_HEAT_ON ;

    
    public HCPA()
    {
        this(HCPA_ADDRESS);
    }

  public HCPA(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      hcpa = bus.getDevice(address);
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
            hcpa.write(HCPA_CONFIG , config);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    
    
    }
    

    public void readData(){
     
        
        try {
            
            //hcpa.write((byte)0x39);
            
            hcpa.read(b , 0 , 4);
            
            Thread.sleep(100);
            
            temp_hum = humidity;
            
            /*b[0]= b[0] & 0xff ;
            b[1]= b[1] & 0xff ;
            b[2]= b[2] & 0xff ;
            b[3]= b[3] & 0xff ;
            */
           /* System.out.println("raw: 0" + b[0] );
            System.out.println("raw: 1" + b[0] );
            System.out.println("raw: 2" + b[0] );
            System.out.println("raw: 3" + b[0] );
            */
            
            humidity =  (double)( (b[0] & 0xff ) * 256 + ( b[1] & 0xff ) ) / 0x3fff * 100;
            temperature  =  (double)( (b[2]  & 0xff ) * 64 + ( b[3]  & 0xff )/4 ) /  0x3fff  * 165.0 - 40.0;
          
            
            if(humidity < 100)
                temp_hum = humidity;
            
            
            System.out.println("TEMEPRATURE ˚C  : " + temperature );
            System.out.println("HUMIDITY  : " + temp_hum );
            
            
        }
        
        catch(Exception ex){ex.printStackTrace(); }
        
    }
    
   


public static void main(String args[])
{
     HCPA sensor =  new HCPA();  

    
     sensor.setConfig(config);
    
     while(true){
      
           sensor.readData();
         
   }


}


}

