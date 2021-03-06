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


public class MCP23017 {
    
    public static void main(String args[]) throws InterruptedException, IOException {
        
        System.out.println("<--Pi4J--> MCP23017 GPIO Example ... started.");
        
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom MCP23017 GPIO provider
        final MCP23017GpioProvider gpioProvider = new MCP23017GpioProvider(I2CBus.BUS_1, 0x20);
        
        // provision gpio input pins from MCP23017
        GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A0, "MyInput-A0", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A1, "MyInput-A1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A2, "MyInput-A2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A3, "MyInput-A3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A4, "MyInput-A4", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A5, "MyInput-A5", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A6, "MyInput-A6", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A7, "MyInput-A7", PinPullResistance.PULL_UP),
            };
        
        // create and register gpio pin listener
        gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                        + event.getState());
            }
        }, myInputs);
        
        
        for (int i = 0; i < 8; i++) {
            System.out.println(" --> GPIO PIN STATE CHANGE: " + myInputs[i].getPin() + " = "
                               + myInputs[i].getState());
            Thread.sleep(100);
        }
       
  
        
        
        // provision gpio output pins and make sure they are all LOW at startup
        GpioPinDigitalOutput myOutputs[] = { 
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B0, "MyOutput-B0", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B1, "MyOutput-B1", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B2, "MyOutput-B2", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B3, "MyOutput-B3", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B4, "MyOutput-B4", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B5, "MyOutput-B5", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B6, "MyOutput-B6", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B7, "MyOutput-B7", PinState.LOW)
          };
        
        
         for (int i = 0; i < 8; i++) {
             
             System.out.println(" --> GPIO PIN STATE CHANGE: " + myOutputs[i].getPin() + " = "
                                + myOutputs[i].getState());
             Thread.sleep(100);
         }

       
        /*
        
        // keep program running for 20 seconds
        for (int count = 0; count < 10; count++) {
           gpio.setState(true, myOutputs);
            Thread.sleep(1000);
            gpio.setState(false, myOutputs);
            Thread.sleep(1000);
        }
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();                 
    */
         }
}
