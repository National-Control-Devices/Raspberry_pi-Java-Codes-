import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class KMA36
{

   public final static int KMA36_ADDRESS = 0x59;
 
   byte[] data = new byte[7];
   byte[] b = new byte[4];
   

  private static boolean verbose = true;
  
  private I2CBus bus;
  private I2CDevice kma36;
 
 private double angle = 0.0;
 
 public KMA36()
{
  this(KMA36_ADDRESS);
}

  public KMA36(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      kma36 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }




public void setConfig(){


	b[0] = 0x03;
	b[1] = (byte)0x7F;
	b[2] = (byte)0xFF;
	b[3] = (byte)(0x7F);
		
      try
    {
          kma36.write(b,0,4);
	Thread.sleep(200);
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }

}

public void fetchData(){
	
      try
    {
 
          kma36.read(data,0,10);
	Thread.sleep(500);
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }
	
	System.out.println(data[0]);
	System.out.println(data[1]);
	

    int msb = data[1] ;
    int lsb = data[0] ;
    
    int raw_angle = msb << 8 | lsb ;
    
    angle = raw_angle  * 360.0 / ((256 * 8) + 128)  ;
    
}

public void printData()
{

   NumberFormat NF = new DecimalFormat("##00.00");
   System.out.println("Angle   : " + NF.format(angle) + " ˚");
   
}


public static void main(String args[])
{
	KMA36 sensor =  new KMA36();  

	sensor.setConfig();

     while(true){  

         sensor.fetchData();
         sensor.printData();

   }

 }


}
