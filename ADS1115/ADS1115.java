

import java.io.IOException;


import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class ADS1115
{
    
    private static int ADS1115_CONVERSIONDELAY = 8;
    private static int ADS1115_ADDRESS = 0x48;
    /*
     * =========================================================================
     * POINTER REGISTER
     * -----------------------------------------------------------------------
     */
    private static int ADS1015_REG_POINTER_CONVERT = 0;
    private static int ADS1015_REG_POINTER_CONFIG = 1;
    private static int ADS1015_REG_POINTER_LOWTHRESH = 2;
    private static int ADS1015_REG_POINTER_HITHRESH = 3;
    
    /*
     * =========================================================================
     * CONFIG REGISTER
     * -----------------------------------------------------------------------
     */
    private static int ADS1015_REG_CONFIG_OS_MASK = 0x8000;
    private static int ADS1015_REG_CONFIG_OS_SINGLE = 0x8000; // Write: Set to
    // start a
    // single-conversion
    private static int ADS1015_REG_CONFIG_OS_BUSY = 0x0000; // Read: Bit = 0
    // when conversion
    // is in progress
    private static int ADS1015_REG_CONFIG_OS_NOTBUSY = 0x8000; // Read: Bit = 1
    // when device
    // is not
    // performing a
    // conversion
    
    private static int ADS1015_REG_CONFIG_MUX_MASK = 0x7000;
    private static int ADS1015_REG_CONFIG_MUX_DIFF_0_1 = 0x0000; // Differential
    // P = AIN0,
    // N = AIN1
    // (default)
    private static int ADS1015_REG_CONFIG_MUX_DIFF_0_3 = 0x1000; // Differential
    // P = AIN0,
    // N = AIN3
    private static int ADS1015_REG_CONFIG_MUX_DIFF_1_3 = 0x2000; // Differential
    // P = AIN1,
    // N = AIN3
    private static int ADS1015_REG_CONFIG_MUX_DIFF_2_3 = 0x3000; // Differential
    // P = AIN2,
    // N = AIN3
    private static int ADS1015_REG_CONFIG_MUX_SINGLE_0 = 0x4000; // Single-ended
    // AIN0
    private static int ADS1015_REG_CONFIG_MUX_SINGLE_1 = 0x5000; // Single-ended
    // AIN1
    private static int ADS1015_REG_CONFIG_MUX_SINGLE_2 = 0x6000; // Single-ended
    // AIN2
    private static int ADS1015_REG_CONFIG_MUX_SINGLE_3 = 0x7000; // Single-ended
    // AIN3
    
    private static int ADS1015_REG_CONFIG_PGA_MASK = 0x0E00;
    private static int ADS1015_REG_CONFIG_PGA_6_144V = 0x0000; // +/-6.144V
    // range
    private static int ADS1015_REG_CONFIG_PGA_4_096V = 0x0200; // +/-4.096V
    // range
    private static int ADS1015_REG_CONFIG_PGA_2_048V = 0x0400; // +/-2.048V
    // range
    // (default)
    private static int ADS1015_REG_CONFIG_PGA_1_024V = 0x0600; // +/-1.024V
    // range
    private static int ADS1015_REG_CONFIG_PGA_0_512V = 0x0800; // +/-0.512V
    // range
    private static int ADS1015_REG_CONFIG_PGA_0_256V = 0x0A00; // +/-0.256V
    // range
    
    private static int ADS1015_REG_CONFIG_MODE_MASK = 0x0100;
    private static int ADS1015_REG_CONFIG_MODE_CONTIN = 0x0000; // Continuous
    // conversion
    // mode
    private static int ADS1015_REG_CONFIG_MODE_SINGLE = 0x0100; // Power-down
    // single-shot
    // mode
    // (default)
    
    private static int ADS1015_REG_CONFIG_DR_MASK = 0x00E0;
    private static int ADS1015_REG_CONFIG_DR_128SPS = 0x0000; // 128 samples per
    // second
    private static int ADS1015_REG_CONFIG_DR_250SPS = 0x0020; // 250 samples per
    // second
    private static int ADS1015_REG_CONFIG_DR_490SPS = 0x0040; // 490 samples per
    // second
    private static int ADS1015_REG_CONFIG_DR_920SPS = 0x0060; // 920 samples per
    // second
    private static int ADS1015_REG_CONFIG_DR_1600SPS = 0x0080; // 1600 samples
    // per second
    // (default)
    private static int ADS1015_REG_CONFIG_DR_2400SPS = 0x00A0; // 2400 samples
    // per second
    private static int ADS1015_REG_CONFIG_DR_3300SPS = 0x00C0; // 3300 samples
    // per second
    
    private static int ADS1015_REG_CONFIG_CMODE_MASK = 0x0010;
    private static int ADS1015_REG_CONFIG_CMODE_TRAD = 0x0000; // Traditional
    // comparator
    // with
    // hysteresis
    // (default)
    private static int ADS1015_REG_CONFIG_CMODE_WINDOW = 0x0010; // Window
    // comparator
    private static int ADS1015_REG_CONFIG_CPOL_MASK = 0x0008;
    private static int ADS1015_REG_CONFIG_CPOL_ACTVLOW = 0x0000; // ALERT/RDY
    // pin is
    // low when
    // active
    // (default)
    private static int ADS1015_REG_CONFIG_CPOL_ACTVHI = 0x0008;; // ALERT/RDY
    // pin is
    // high when
    // active
    
    private static int ADS1015_REG_CONFIG_CLAT_MASK = 0x0004; // Determines if
    // ALERT/RDY pin
    // latches once
    // asserted
    private static int ADS1015_REG_CONFIG_CLAT_NONLAT = 0x0000; // Non-latching
    // comparator
    // (default)
    private static int ADS1015_REG_CONFIG_CLAT_LATCH = 0x0004; // Latching
    // comparator
    
    private static int ADS1015_REG_CONFIG_CQUE_MASK = 0x0003;
    private static int ADS1015_REG_CONFIG_CQUE_1CONV = 0x0000; // Assert
    // ALERT/RDY
    // after one
    // conversions
    private static int ADS1015_REG_CONFIG_CQUE_2CONV = 0x0001; // Assert
    // ALERT/RDY
    // after two
    // conversions
    private static int ADS1015_REG_CONFIG_CQUE_4CONV = 0x0002; // Assert
    // ALERT/RDY
    // after four
    // conversions
    private static int ADS1015_REG_CONFIG_CQUE_NONE = 0x0003; // Disable the
    // comparator
    // and put
    // ALERT/RDY in
    // high state
    // (default)
    /* ========================================================================= */
    
    private static boolean verbose = true;//.equals(System.getProperty("htu21df.verbose", "false"));
    
    private I2CBus bus;
    private I2CDevice ads1115;
    
    public ADS1115()
    {
        this(ADS1115_ADDRESS);
    }
    
    public ADS1115(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            ads1115 = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    public void setConfigDefaults() throws IOException
    {
        // Start with default values
        /*int config = ADS1015_REG_CONFIG_CQUE_NONE | // Disable the
        // comparator
        // (default
        // val)
        ADS1015_REG_CONFIG_CLAT_NONLAT | // Non-latching
        // (default
        // val)
        ADS1015_REG_CONFIG_CPOL_ACTVLOW | // Alert/Rdy active
        // low
        // (default val)
        ADS1015_REG_CONFIG_CMODE_TRAD | // Traditional
        // comparator
        // (default val)
        ADS1015_REG_CONFIG_DR_920SPS | // 1600 samples per
        // second
        // (default)
        ADS1015_REG_CONFIG_MODE_SINGLE; // Single-shot mode
        // (default)
        
        // Set PGA/voltage range
        config |= ADS1015_REG_CONFIG_PGA_6_144V; // +/- 6.144V range
        // (limited to
        // VDD +0.3V max!)*/
        
        
        byte[] data = new byte[3];
        
        data[0] = (byte)0x01;
        data[1] = (byte)0x85;
        data[2] = (byte)0x83;
        
        
        ads1115.write(data,0,3);
        
        //ads1115.write((byte)config);
        
        //return config;
    }
    
   public int read16Bits() throws IOException
    {
        
        ads1115.write((byte)0x00);
        
        byte[] buffer = new byte[2];
        
        try{
        Thread.sleep(100);
        }catch( Exception e  ){ }
        
        ads1115.read(buffer, 0, buffer.length);
        
        int msb = buffer[0] & 0xff;
        int lsb = buffer[1] & 0xff;
       
        /*if (lsb < 0)
            lsb = 256 + lsb;
        */
        return ((msb << 8) | lsb);
        
    }
    
    void write16Bits(int address, int value) throws IOException
    {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) (value << 8);// msb
        buffer[1] = (byte) (value & 0xFF); // lsb
        ads1115.write(address, buffer, 0, buffer.length);
        
    }
    
    public static void main(String[] args) throws IOException
    {
        
        ADS1115 sensor = new ADS1115();
        //sensor.setConfigDefaults();
        
        while(true)
        {
            sensor.setConfigDefaults();
            
            System.out.println(" output :  " + sensor.read16Bits());
            
        }
        
        
    }
    
    
}

