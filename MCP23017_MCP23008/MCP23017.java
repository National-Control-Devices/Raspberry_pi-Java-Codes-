import java.io.IOException;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;


public class MCP23017{

 public static void main(String args[]) throws InterruptedException, IOException {
        
     int i=0;
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom MCP23017 GPIO provider
       final MCP23017GpioProvider gpioProvider20 = new MCP23017GpioProvider(I2CBus.BUS_1, 0x20 );
       
        final MCP23017GpioProvider gpioProvider21 = new MCP23017GpioProvider(I2CBus.BUS_1, 0x21);
       
         System.out.println("<--Pi4J--> MCP23017  ... started.");
       
      /*   GpioPinDigitalInput myInputs20[] = {
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A0, "MyInput-A0", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A1, "MyInput-A1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A2, "MyInput-A2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A3, "MyInput-A3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A4, "MyInput-A4", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A5, "MyInput-A5", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A6, "MyInput-A6", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider20, MCP23017Pin.GPIO_A7, "MyInput-A7", PinPullResistance.PULL_UP)





  };     


        GpioPinDigitalInput myInputs21[] = {

                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A0, "MyInput-A0", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A1, "MyInput-A1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A2, "MyInput-A2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A3, "MyInput-A3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A4, "MyInput-A4", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A5, "MyInput-A5", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A6, "MyInput-A6", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider21, MCP23017Pin.GPIO_A7, "MyInput-A7", PinPullResistance.PULL_UP)

            };


       gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                        + event.getState());
            }
        }, myInputs20);

       */
         GpioPinDigitalOutput myOutputs20[] = { 
            
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A0, "MyInput-A0", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A1, "MyInput-A1", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A2, "MyInput-A2", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A3, "MyInput-A3", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A4, "MyInput-A4", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A5, "MyInput-A5", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A6, "MyInput-A6", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_A7, "MyInput-A7", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B0, "MyOutput-B0", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B1, "MyOutput-B1", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B2, "MyOutput-B2", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B3, "MyOutput-B3", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B4, "MyOutput-B4", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B5, "MyOutput-B5", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B6, "MyOutput-B6", PinState.LOW),
             gpio.provisionDigitalOutputPin(gpioProvider20, MCP23017Pin.GPIO_B7, "MyOutput-B7", PinState.LOW)



        };
         
        GpioPinDigitalOutput myOutputs21[] = { 
    
            
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A0, "MyInput-A0", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A1, "MyInput-A1", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A2, "MyInput-A2", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A3, "MyInput-A3", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A4, "MyInput-A4", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A5, "MyInput-A5", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A6, "MyInput-A6", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_A7, "MyInput-A7", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B0, "MyOutput-B0", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B1, "MyOutput-B1", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B2, "MyOutput-B2", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B3, "MyOutput-B3", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B4, "MyOutput-B4", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B5, "MyOutput-B5", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B6, "MyOutput-B6", PinState.LOW),
	        gpio.provisionDigitalOutputPin(gpioProvider21, MCP23017Pin.GPIO_B7, "MyOutput-B7", PinState.LOW)
};

     while(true)
     {
         
     
   System.out.println(" - ------ GPIO toggling I2cbus_20 ---------  " );

    for( i=0;i<16;i++)
        
 	{
        
        System.out.println(" - ------ GPIO " +i + " ON ---------  " );

            gpio.setState(true, myOutputs20[i]);
            Thread.sleep(800);
        
        System.out.println(" - ------ GPIO " +i + " OFF ---------  " );

        gpio.setState(false, myOutputs20[i]);
            Thread.sleep(800);
        
        
        
	}

     
     System.out.println(" - ------ GPIO toggling I2cbus_21 ---------  " );

     
    for ( i = 0; i < 16; i++) {

        
        System.out.println(" - ------ GPIO " +i + " ON ---------  " );

            gpio.setState(true, myOutputs21[i]);
            Thread.sleep(800);
        
        System.out.println(" - ------ GPIO " +i + " OFF ---------  " );

        gpio.setState(false, myOutputs21[i]);
            Thread.sleep(800);
        }


     }

      // gpio.shutdown();



}


}



