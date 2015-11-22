import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/*
 * Humidity, Temperature
 */
public class HTU20D
{
  public final static int HTU20D_ADDRESS = 0x40;
  // HTU20D Registers
  public final static int HTU20D_READTEMP = 0xE3;
  public final static int HTU20D_READHUM  = 0xE5;

  public final static int HTU20D_READTEMP_NH = 0xF3; // NH = no hold
  public final static int HTU20D_READHUMI_NH = 0xF5;

  public final static int HTU20D_WRITEREG = 0xE6;
  public final static int HTU20D_READREG  = 0xE7;
  public final static int HTU20D_RESET    = 0xFE;

  private static boolean verbose = "true".equals(System.getProperty("HTU20D.verbose", "false"));

  private I2CBus bus;
  private I2CDevice htu20d;

  public HTU20D()
  {
    this(HTU20D_ADDRESS);
  }

  public HTU20D(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      htu20d = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }

  public boolean begin()
    throws Exception
  {
    try { reset(); } catch (Exception ex) { System.err.println("Reset:" + ex.toString()); }
    int r = 0;
    try
    {
      htu20d.write((byte) HTU20D_READREG);
      r = htu20d.read();
      if (verbose)
        System.out.println("DBG: Begin: 0x" + lpad(Integer.toHexString(r), "0", 2));
    }
    catch (Exception ex)
    { System.err.println("Begin:" + ex.toString()); }
    return (r == 0x02);
  }

  public void reset()
    throws Exception
  {
    //  HTU20D.write(HTU20D_ADDRESS, (byte)HTU20D_RESET);
    try
    {
      htu20d.write((byte) HTU20D_RESET);
      if (verbose)
        System.out.println("DBG: Reset OK");
    }
    finally
    {
      waitfor(15); // Wait 15ms
    }
  }

  public void close()
  {
    try { this.bus.close(); }
    catch (IOException ioe) { ioe.printStackTrace(); }    
  }
  
  public float readTemperature()
    throws Exception
  {
    // Reads the raw temperature from the sensor
    if (verbose)
      System.out.println("Read Temp: Written 0x" + lpad(Integer.toHexString((HTU20D_READTEMP & 0xff)), "0", 2));
    htu20d.write((byte) (HTU20D_READTEMP)); //  & 0xff));
    waitfor(50); // Wait 50ms
    byte[] buf = new byte[3];
    /*int rc  = */ htu20d.read(buf, 0, 3);
    int msb = buf[0] & 0xFF;
    int lsb = buf[1] & 0xFF;
    int crc = buf[2] & 0xFF;
    int raw = ((msb << 8) + lsb) & 0xFFFC;

    //  while (!Wire.available()) {}

    if (verbose)
    {
      System.out.println("Temp -> 0x" + lpad(Integer.toHexString(msb), "0", 2) + " " + "0x" +
                         lpad(Integer.toHexString(lsb), "0", 2) + " " + "0x" + lpad(Integer.toHexString(crc), "0", 2));
      System.out.println("DBG: Raw Temp: " + (raw & 0xFFFF) + ", " + raw);
    }

    float temp = raw; // t;
    temp *= 175.72;
    temp /= 65536;
    temp -= 46.85;

    if (verbose)
      System.out.println("DBG: Temp: " + temp);
    return temp;
  }

  public float readHumidity()
    throws Exception
  {
    // Reads the raw (uncompensated) humidity from the sensor
    htu20d.write((byte) HTU20D_READHUM);
    waitfor(50); // Wait 50ms
    byte[] buf = new byte[3];
    /* int rc  = */htu20d.read(buf, 0, 3);
    int msb = buf[0] & 0xFF;
    int lsb = buf[1] & 0xFF;
    int crc = buf[2] & 0xFF;
    int raw = ((msb << 8) + lsb) & 0xFFFC;

    //  while (!Wire.available()) {}

    if (verbose)
    {
      System.out.println("Hum -> 0x" + lpad(Integer.toHexString(msb), "0", 2) + " " + "0x" +
                         lpad(Integer.toHexString(lsb), "0", 2) + " " + "0x" + lpad(Integer.toHexString(crc), "0", 2));
      System.out.println("DBG: Raw Humidity: " + (raw & 0xFFFF) + ", " + raw);
    }

    float hum = raw;
    hum *= 125;
    hum /= 65536;
    hum -= 6;

    if (verbose)
      System.out.println("DBG: Humidity: " + hum);
    return hum;
  }

  protected static void waitfor(long howMuch)
  {
    try
    {
      Thread.sleep(howMuch);
    }
    catch (InterruptedException ie)
    {
      ie.printStackTrace();
    }
  }

  private static String lpad(String s, String with, int len)
  {
    String str = s;
    while (str.length() < len)
      str = with + str;
    return str;
  }

  public static void main(String[] args)
  {
    final NumberFormat NF = new DecimalFormat("##00.00");
    HTU20D sensor = new HTU20D();
    float hum = 0;
    float temp = 0;
   float tempF =0;
    try
    {
      if (!sensor.begin())
      {
        System.out.println("Sensor not found!");
        System.exit(1);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      System.exit(1);
    }


while(true){
    try
    {
      hum = sensor.readHumidity();
    }
    catch (Exception ex)
    {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }

    try
    {
      temp = sensor.readTemperature();

	tempF = 9/5 * temp  + 32 ;	


    }
    catch (Exception ex)
    {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }

    System.out.println("Temperature: " + NF.format(temp) + " C");
    
    System.out.println("Temperature: " + NF.format(tempF) + " F");
	System.out.println("Humidity   : " + NF.format(hum) + " %");
 

     try{
      Thread.sleep(1000);
      }
      catch (Exception ex)
      {
          System.err.println(ex.getMessage());
          ex.printStackTrace();
      }




 }
}}
