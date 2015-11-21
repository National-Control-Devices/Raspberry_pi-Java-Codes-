import java.io.IOException;

import com.pi4j.gpio.extension.mcp.MCP23008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23008Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.gpio.RaspiPin;

public class MCP23008{

 public static void main(String args[]) throws InterruptedException, IOException {
        
      
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // create custom MCP23017 GPIO provider
       final MCP23008GpioProvider gpioProvider = new MCP23008GpioProvider(I2CBus.BUS_1, 0x20);
     
         System.out.println("<--Pi4J--> MCP23008  ... started.");
       
       /*  GpioPinDigitalInput myInputs[] = {
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_00, "MyInput-A0", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_01, "MyInput-A1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_02, "MyInput-A2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_03, "MyInput-A3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_04, "MyInput-A4", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_05, "MyInput-A5", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_06, "MyInput-A6", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(gpioProvider, RaspiPin.GPIO_07, "MyInput-A7", PinPullResistance.PULL_UP)

  };     


       gpio.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                        + event.getState());
            }
        }, myInputs);
     
   */
         GpioPinDigitalOutput myOutputs[] = {
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_00, "MyOutput-B0", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_01, "MyOutput-B1", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_02, "MyOutput-B2", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_03, "MyOutput-B3", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_04, "MyOutput-B4", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_05, "MyOutput-B5", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_06, "MyOutput-B6", PinState.LOW),
            gpio.provisionDigitalOutputPin(gpioProvider, MCP23008Pin.GPIO_07, "MyOutput-B7", PinState.LOW)


        };
        
    for (int i = 0; i < 8; i++) {
        
        
        gpio.setState(true, myOutputs[i]);
        
        System.out.println(" --> GPIO PIN STATE : " + myOutputs[i].getPin() + " = "
                           + myOutputs[i].getState());
        
        Thread.sleep(800);
       
        
        gpio.setState(false, myOutputs[i]);
        
        System.out.println(" --> GPIO PIN STATE : " + myOutputs[i].getPin() + " = "
                           + myOutputs[i].getState());
        
        
        
    }
     
     gpio.shutdown();


}


}



