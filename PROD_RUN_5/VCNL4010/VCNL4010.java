
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.system.SystemInfo;

import java.io.IOException;

/*
 * Proximity sensor
 */
public class VCNL4010
{
  public final static int LITTLE_ENDIAN = 0;
  public final static int BIG_ENDIAN    = 1;
  private final static int VCNL4010_ENDIANNESS = BIG_ENDIAN;
  /*
  Prompt> sudo i2cdetect -y 1
       0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f
  00:          -- -- -- -- -- -- -- -- -- -- -- -- --
  10: -- -- -- 13 -- -- -- -- -- -- -- -- -- -- -- --
  20: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
  30: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
  40: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
  50: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
  60: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
  70: -- -- -- -- -- -- -- --
   */
  // This next addresses is returned by "sudo i2cdetect -y 1", see above.
  public final static int VCNL4010_ADDRESS = 0x13; 
  // Commands
  public final static int VCNL4010_COMMAND          = 0x80;
  public final static int VCNL4010_PRODUCTID        = 0x81;
  public final static int VCNL4010_IRLED            = 0x83;
  public final static int VCNL4010_AMBIENTPARAMETER = 0x84;
  public final static int VCNL4010_AMBIENTDATA      = 0x85;
  public final static int VCNL4010_PROXIMITYDATA    = 0x87;
  public final static int VCNL4010_SIGNALFREQ       = 0x89;
  public final static int VCNL4010_PROXINITYADJUST  = 0x8A;

  public final static int VCNL4010_3M125            = 0x00;
  public final static int VCNL4010_1M5625           = 0x01;
  public final static int VCNL4010_781K25           = 0x02;
  public final static int VCNL4010_390K625          = 0x03;

  public final static int VCNL4010_MEASUREAMBIENT   = 0x10;
  public final static int VCNL4010_MEASUREPROXIMITY = 0x08;
  public final static int VCNL4010_AMBIENTREADY     = 0x40;
  public final static int VCNL4010_PROXIMITYREADY   = 0x20;

  private static boolean verbose = false;
  
  private I2CBus bus;
  private I2CDevice vcnl4010;
  
  public VCNL4010()
  {
    this(VCNL4010_ADDRESS);
  }
  
  public VCNL4010(int address)
  {
    try
    {
      // Get i2c bus
      bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
      if (verbose)
        System.out.println("Connected to bus. OK.");

      // Get device itself
      vcnl4010 = bus.getDevice(address);
      if (verbose)
        System.out.println("Connected to device. OK.");
      
      vcnl4010.write(VCNL4010_IRLED, (byte)20); // 20 * 10mA = 200mA. Range [10-200], by step of 10.
      try
      {
        int irLed = readU8(VCNL4010_IRLED);
        System.out.println("IR LED Current = " + (irLed * 10) + " mA");
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      
      try
      {        
//      vcnl4010.write(VCNL4010_SIGNALFREQ, (byte)VCNL4010_390K625);
        int freq = readU8(VCNL4010_SIGNALFREQ);
        switch (freq)
        {
          case VCNL4010_3M125:
            System.out.println("Proximity measurement frequency = 3.125 MHz");
            break;
          case VCNL4010_1M5625:
            System.out.println("Proximity measurement frequency = 1.5625 MHz");
            break;
          case VCNL4010_781K25:
            System.out.println("Proximity measurement frequency = 781.25 KHz");
            break;
          case VCNL4010_390K625:
            System.out.println("Proximity measurement frequency = 390.625 KHz");
            break;
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      
      vcnl4010.write(VCNL4010_PROXINITYADJUST, (byte)0x81); 
      try 
      {
        int reg = readU8(VCNL4010_PROXINITYADJUST);
        System.out.println("Proximity adjustment register = " + toHex(reg));
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    catch (IOException e)
    {
      System.err.println(e.getMessage());
    }
  }
  
  private int readU8(int reg) throws Exception
  {
    int result = 0;
    try
    {
      result = this.vcnl4010.read(reg);
      try { Thread.sleep(0, 170100); } catch (Exception ex) { ex.printStackTrace(); } // 170 microseconds
      if (verbose)
        System.out.println("(U8) I2C: Device " + toHex(VCNL4010_ADDRESS) + " returned " + toHex(result) + " from reg " + toHex(reg));
    }
    catch (Exception ex)
    { ex.printStackTrace(); }
    return result;
  }

  private int readU16(int register) throws Exception
  {
    int hi = this.readU8(register);
    int lo = this.readU8(register + 1);
    int result = (VCNL4010_ENDIANNESS == BIG_ENDIAN)? (hi << 8) + lo : (lo << 8) + hi; // Little endian for VCNL4010
    if (verbose)
      System.out.println("(U16) I2C: Device " + toHex(VCNL4010_ADDRESS) + " returned " + toHex(result) + " from reg " + toHex(register));
    return result;
  }

  public int readProximity() throws Exception
  {
    int prox = 0;
    vcnl4010.write(VCNL4010_COMMAND, (byte)VCNL4010_MEASUREPROXIMITY);
    boolean keepTrying = true;
    while (keepTrying)
    {
      int cmd = this.readU8(VCNL4010_COMMAND);
      if (verbose)
        System.out.println("DBG: Proximity: " + (cmd & 0xFFFF) + ", " + cmd + " (" + VCNL4010_PROXIMITYREADY + ")");
      if (((cmd & 0xff) & VCNL4010_PROXIMITYREADY) != 0)
      {
        keepTrying = false;
        prox = this.readU16(VCNL4010_PROXIMITYDATA);
      }
      else
        waitfor(10);  // Wait 10 ms
    }
    return prox;
  }
      
  public int readAmbient() throws Exception
  {
    int ambient = 0;
    vcnl4010.write(VCNL4010_COMMAND, (byte)VCNL4010_MEASUREAMBIENT);
    boolean keepTrying = true;
    while (keepTrying)
    {
      int cmd = this.readU8(VCNL4010_COMMAND);
      if (verbose)
        System.out.println("DBG: Ambient: " + (cmd & 0xFFFF) + ", " + cmd + " (" + VCNL4010_AMBIENTREADY + ")");
      if (((cmd & 0xff) & VCNL4010_AMBIENTREADY) != 0)
      {
        keepTrying = false;
        ambient = this.readU16(VCNL4010_AMBIENTDATA);
      }
      else
        waitfor(10);  // Wait 10 ms
    }
    return ambient;
  }
      
  public final static int AMBIENT_INDEX   = 0;
  public final static int PROXIMITY_INDEX = 1;
  public int[] readAmbientProximity() throws Exception
  {
    int prox = 0;
    int ambient = 0;
    vcnl4010.write(VCNL4010_COMMAND, (byte)(VCNL4010_MEASUREPROXIMITY | VCNL4010_MEASUREAMBIENT));
    boolean keepTrying = true;
    while (keepTrying)
    {
      int cmd = this.readU8(VCNL4010_COMMAND);
      if (verbose)
        System.out.println("DBG: Proximity: " + (cmd & 0xFFFF) + ", " + cmd + " (" + VCNL4010_PROXIMITYREADY + ")");
      if (((cmd & 0xff) & VCNL4010_PROXIMITYREADY) != 0 && ((cmd & 0xff) & VCNL4010_AMBIENTREADY) != 0)
      {
        keepTrying = false;
        ambient = this.readU16(VCNL4010_AMBIENTDATA);
        prox    = this.readU16(VCNL4010_PROXIMITYDATA);
      }
      else
        waitfor(10);  // Wait 10 ms
    }
    return new int[] { ambient, prox };
  }
      
  private static String toHex(int i)
  {
    String s = Integer.toString(i, 16).toUpperCase();
    while (s.length() % 2 != 0)
      s = "0" + s;
    return "0x" + s;
  }
  
  private static void waitfor(long howMuch)
  {
    try { Thread.sleep(howMuch); } catch (InterruptedException ie) { ie.printStackTrace(); }
  }
  
  private static boolean go = true;
  
  private static int minProx    = Integer.MAX_VALUE;
  private static int minAmbient = Integer.MAX_VALUE;
  private static int maxProx    = Integer.MIN_VALUE;
  private static int maxAmbient = Integer.MIN_VALUE;

  public static void main(String[] args)
  {
    VCNL4010 sensor = new VCNL4010();
    int prox    = 0;
    int ambient = 0;
    
    
    Runtime.getRuntime().addShutdownHook(new Thread()
                                         {
                                           public void run()
                                           {
                                             go = false;
                                             System.out.println("\nBye");
                                             System.out.println("Proximity between " + minProx + " and " + maxProx);
                                             System.out.println("Ambient between " + minAmbient + " and " + maxAmbient);
                                           }
                                         });
    System.out.println("-- Ready --");
    int i = 0;
    while (go) //  && i++ < 5)
    {
      try 
      { 
        if (false)
          prox = sensor.readProximity(); 
        else if (false)
          ambient = sensor.readAmbient();
        else if (true)
        {
          int[] data = sensor.readAmbientProximity();
          prox    = data[PROXIMITY_INDEX];
          ambient = data[AMBIENT_INDEX];
        }
        maxProx = Math.max(prox, maxProx);
        maxAmbient = Math.max(ambient, maxAmbient);
        minProx = Math.min(prox, minProx);
        minAmbient = Math.min(ambient, minAmbient);
      } 
      catch (Exception ex) 
      { 
        System.err.println(ex.getMessage()); 
        ex.printStackTrace();
      }
      System.out.println("Ambient:" + ambient + ", Proximity: " + prox); 
      try { Thread.sleep(100L); } catch (InterruptedException ex) { System.err.println(ex.toString()); }
    }
  }
}