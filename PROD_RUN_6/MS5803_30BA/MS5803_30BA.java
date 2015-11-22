import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MS5803_30BA
{
    
    public final static int MS580X_ADDRESS = 0x77;
    
    double temperature = 0.0;
    double pressure = 0.0;
    
    public final static int CMD_RESET	=	0x1E;	// ADC reset command
    public final static int CMD_ADC_READ =	0x00;	// ADC read command
    public final static int CMD_ADC_CONV =	0x40;	// ADC conversion command
    public final static int CMD_ADC_D1	 =	0x00;	// ADC D1 conversion
    public final static int CMD_ADC_D2	 =	0x10;	// ADC D2 conversion
    public final static int CMD_ADC_256	 =	0x00;	// ADC resolution=256
    public final static int CMD_ADC_512	 =	0x02;	// ADC resolution=512
    public final static int CMD_ADC_1024 =	0x04;	// ADC resolution=1024
    public final static int CMD_ADC_2048 =	0x06;	// ADC resolution=2048
    public final static int CMD_ADC_4096 =	0x08;	// ADC resolution=4096
    
    static int _Resolution =	256;	// set resolution
    
    // Create array to hold the 8 sensor calibration coefficients
    int[] sensorCoeffs = new int[8];     // unsigned 16-bit integer (0-65535)
    // D1 and D2 need to be unsigned 32-bit integers (long 0-4294967295)
    static long     D1 = 0;    // Store uncompensated pressure value
    static long     D2 = 0;    // Store uncompensated temperature value
    // These three variables are used for the conversion steps
    // They should be signed 32-bit integer initially
    // i.e. signed long from -2147483648 to 2147483647
    static long	dT = 0;
    static long 	TEMP = 0;
    // These values need to be signed 64 bit integers
    // (long long = int64_t)
    static long	Offset = 0;
    static long	Sensitivity  = 0;
    static long	T2 = 0;
    static long	OFF2 = 0;
    static long	Sens2 = 0;
    // bytes to hold the results from I2C communications with the sensor
    static int HighByte;
    static int MidByte;
    static int LowByte;
    
    float mbar; // Store pressure in mbar.
    float tempC; // Store temperature in degrees Celsius
    //    float tempF; // Store temperature in degrees Fahrenheit
    //    float psiAbs; // Store pressure in pounds per square inch, absolute
    //    float psiGauge; // Store gauge pressure in pounds per square inch (psi)
    //    float inHgPress;	// Store pressure in inches of mercury
    //    float mmHgPress;	// Store pressure in mm of mercury
    
    long mbarInt; // pressure in mbar, initially as a signed long integer
    
    private static boolean verbose = true;
    
    private I2CBus bus;
    private I2CDevice ms580x;
    
    //public final static byte config = MS580X_BOTH_TEMP_HUMI | MS580X_TEMP_HUMI_14BIT | MS580X_HEAT_ON ;
    
    
    public MS5803_30BA()
    {
        this(MS580X_ADDRESS);
    }
    
    public MS5803_30BA(int address)
    {
        try
        {
            // Get i2c bus
            bus = I2CFactory.getInstance(I2CBus.BUS_1); // Depends onthe RasPI version
            if (verbose)
                System.out.println("Connected to bus. OK.");
            
            // Get device itself
            ms580x = bus.getDevice(address);
            if (verbose)
                System.out.println("Connected to device. OK.");
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    public void setDefault(){
        
        try
        {
            // MS580X.write( LPS331_CTRL_REG1 , (byte)0xE0);
            
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    void Read_coefficients() throws IOException
    {
        byte[] b = new byte[2];
        
        
        for (int i = 0; i < 8; i++ ){
            
            // The PROM starts at address 0xA0
            
            ms580x.read(0xA0 + (i * 2),b,0,2);
            
            HighByte = b[0] & 0xff;
            LowByte = b[1] & 0xff;
            
            sensorCoeffs[i] = ((HighByte & 0xff ) << 8) | LowByte;
            
            /*if (Verbose){
             // Print out coefficients
             Serial.print("C");
             Serial.print(i);
             Serial.print(" = ");
             Serial.println(sensorCoeffs[i]);
             delay(10);
             }*/
        }
    }
    
    void readSensor(int _Resolution) throws IOException{
        // Choose from CMD_ADC_256, 512, 1024, 2048, 4096 for mbar resolutions
        // of 1, 0.6, 0.4, 0.3, 0.2 respectively. Higher resolutions take longer
        // to read.
        if (_Resolution == 256){
            D1 = MS_5803_ADC((byte)(CMD_ADC_D1 + CMD_ADC_256)); // read raw pressure
            D2 = MS_5803_ADC((byte)(CMD_ADC_D2 + CMD_ADC_256)); // read raw temperature
        } else if (_Resolution == 512) {
            D1 = MS_5803_ADC((byte)(CMD_ADC_D1 + CMD_ADC_512)); // read raw pressure
            D2 = MS_5803_ADC((byte)(CMD_ADC_D2 + CMD_ADC_512)); // read raw temperature
        } else if (_Resolution == 1024) {
            D1 = MS_5803_ADC((byte)(CMD_ADC_D1 + CMD_ADC_1024)); // read raw pressure
            D2 = MS_5803_ADC((byte)(CMD_ADC_D2 + CMD_ADC_1024)); // read raw temperature
        } else if (_Resolution == 2048) {
            D1 = MS_5803_ADC((byte)(CMD_ADC_D1 + CMD_ADC_2048)); // read raw pressure
            D2 = MS_5803_ADC((byte)(CMD_ADC_D2 + CMD_ADC_2048)); // read raw temperature
        } else if (_Resolution == 4096) {
            D1 = MS_5803_ADC((byte)(CMD_ADC_D1 + CMD_ADC_4096)); // read raw pressure
            D2 = MS_5803_ADC((byte)(CMD_ADC_D2 + CMD_ADC_4096)); // read raw temperature
        }
        // Calculate 1st order temperature, dT is a long signed integer
        // D2 is originally cast as an uint32_t, but can fit in a int32_t, so we'll
        // cast both parts of the equation below as signed values so that we can
        // get a negative answer if needed
        dT = (int)D2 - ( (int)sensorCoeffs[5] * 256 );
        // Use integer division to calculate TEMP. It is necessary to cast
        // one of the operands as a signed 64-bit integer (int64_t) so there's no
        // rollover issues in the numerator.
        TEMP = 2000 + ((long)dT * sensorCoeffs[6]) / 8388608L;
        // Recast TEMP as a signed 32-bit integer
        TEMP = (int)TEMP;
        
        
        // All operations from here down are done as integer math until we make
        // the final calculation of pressure in mbar.
        
        
        // Do 2nd order temperature compensation (see pg 9 of MS5803 data sheet)
        // I have tried to insert the fixed values wherever possible
        // (i.e. 2^31 is hard coded as 2147483648).
        if (TEMP < 2000) {
            // For 1 bar model
            T2 = ((long)dT * dT) / 2147483648L ; // 2^31 = 2147483648
            T2 = (int)T2; // recast as signed 32bit integer
            OFF2 = 3 * ((TEMP-2000) * (TEMP-2000));
            Sens2 = 7 * ((TEMP-2000)*(TEMP-2000)) / 8 ;
        } else { // if TEMP is > 2000 (20.0C)
            // For 1 bar model
            T2 = 0;
            OFF2 = 0;
            Sens2 = 0;
            if (TEMP > 4500) {
                // Extra adjustment for high temps, only needed for 1 bar model
                Sens2 = Sens2 - ((TEMP-4500)*(TEMP-4500)) / 8;
            }
        }
        
        // Additional compensation for very low temperatures (< -15C)
        if (TEMP < -1500) {
            // For 1 bar model
            // Leave OFF2 alone in this case
            Sens2 = Sens2 + 2 * ((TEMP+1500)*(TEMP+1500));
        }
        
        // Calculate initial Offset and Sensitivity
        // Notice lots of casts to int64_t to ensure that the
        // multiplication operations don't overflow the original 16 bit and 32 bit
        // integers
        
        // For 1 bar sensor
        Offset = (long)sensorCoeffs[2] * 65536 + (sensorCoeffs[4] * (long)dT) / 128;
        Sensitivity = (long)sensorCoeffs[1] * 32768L + (sensorCoeffs[3] * (long)dT) / 256;
        
        // Adjust TEMP, Offset, Sensitivity values based on the 2nd order
        // temperature correction above.
        TEMP = TEMP - T2; // both should be int32_t
        Offset = Offset - OFF2; // both should be int64_t
        Sensitivity = Sensitivity - Sens2; // both should be int64_t
        
        // Final compensated pressure calculation. We first calculate the pressure
        // as a signed 32-bit integer (mbarInt), then convert that value to a
        // float (mbar).
        
        // For 1 bar sensor
        mbarInt = ((D1 * Sensitivity) / 2097152 - Offset) / 8192;
        mbar = (float)mbarInt / 100;
        
        // Calculate the human-readable temperature in Celsius
        tempC  = (float)TEMP / 100;
        
        // Start other temperature conversions by converting mbar to psi absolute
        //    psiAbs = mbar * 0.0145038;
        //    // Convert psi absolute to inches of mercury
        //    inHgPress = psiAbs * 2.03625;
        //    // Convert psi absolute to gauge pressure
        //    psiGauge = psiAbs - 14.7;
        //    // Convert mbar to mm Hg
        //    mmHgPress = mbar * 0.7500617;
        //    // Convert temperature to Fahrenheit
        //    tempF = (tempC * 1.8) + 32;
        
        
        System.out.println("TEMPERATURE :  "  +  tempC + " C ");
        System.out.println("PRESSURE :  "  +  mbar + " mbar");
        
    }
    
    
    long MS_5803_ADC(byte commandADC) throws IOException {
        
        byte[] b =  new  byte[3];
        // D1 and D2 will come back as 24-bit values, and so they must be stored in
        // a long integer on 8-bit Arduinos.
        long result = 0;
        // Send the command to do the ADC conversion on the chip
        ms580x.write((byte)(CMD_ADC_CONV + commandADC));
        
        // Wait a specified period of time for the ADC conversion to happen
        
        try{
            Thread.sleep(20);
        }
        catch(Exception e){}
        
        // Now send the read command to the MS5803
        
        ms580x.write((byte)CMD_ADC_READ);
        
        // Then request the results. This should be a 24-bit result (3 bytes)
        
        ms580x.read(b,0,3);
        
        HighByte= b[0];
        MidByte = b[1];
        LowByte = b[2];
        
        // Combine the bytes into one integer
        
        result = ((long)(HighByte & 0xff ) << 16) + ((long)MidByte << 8) + (long)LowByte;
        
        return result;
        
    }
    
    public static void main(String args[]) throws IOException
    {
        
        MS5803_30BA sensor =  new MS5803_30BA();
        sensor.Read_coefficients();
        
        while(true){
            
            sensor.readSensor(_Resolution);
            
        }
        
        
    }
    
    
}

