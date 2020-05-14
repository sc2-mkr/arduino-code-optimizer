# ACO - Arduino Code Optimizer
[![Build Status](https://travis-ci.com/sc2-mkr/arduino-code-optimizer.svg?branch=master)](https://travis-ci.com/sc2-mkr/arduino-code-optimizer)

Reduce the memory usage of your Arduino sketch up to 90% by using the [Arduino Port Manipulation](https://www.arduino.cc/en/Reference/PortManipulation).

All references to the ports used by the microcontroller can be found in its datasheet, e.g. [Atmega328P](http://ww1.microchip.com/downloads/en/DeviceDoc/Atmel-7810-Automotive-Microcontrollers-ATmega328P_Datasheet.pdf).

## Example
With the classic blink code:
```c++
void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  digitalWrite(LED_BUILTIN, HIGH);   
  delay(1000);                       
  digitalWrite(LED_BUILTIN, LOW);    
  delay(1000);                       
}
```
we have 1460 bytes used. But, with only three lines change:
```c++
void setup() {
  DDRB |= (1<<DDB5);
}

void loop() {
  PORTB |= 00100000;   
  delay(1000);                       
  PORTB &= 11011111;
  delay(1000);                       
}
```
we have 854 bytes used in memory, 58% less memory used.

**Attention: the use of this tool on your sketch could make it to lose portability between the different Arduino microcontrollers**
