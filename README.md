# ACO - Arduino Code Optimizer
[![Build Status](https://travis-ci.com/sc2-mkr/arduino-code-optimizer.svg?branch=master)](https://travis-ci.com/sc2-mkr/arduino-code-optimizer)

Reduce the memory usage of your Arduino sketch up to 90% by using the [Arduino Port Manipulation](https://www.arduino.cc/en/Reference/PortManipulation).

All references to the ports used by the microcontroller can be found in its datasheet, e.g. [Atmega328P](http://ww1.microchip.com/downloads/en/DeviceDoc/Atmel-7810-Automotive-Microcontrollers-ATmega328P_Datasheet.pdf).

**Attention: the use of this tool on your sketch could make it to lose portability between the different Arduino microcontrollers**

## Getting started
### Prerequisites
For launching this software you need:
 - Java 11

### Usage
The program is designed for the simplest user experience.
![Blank Program](docs/start-program.png)

All you have to do is:
 - select your Arduino's code file
 - select the plugins you want to use
 - select the board
 - click "Optimize" and enjoy

**A more detailed guide can be found in /docs/manual directory (Ongoing)**

## Optimization Example
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
we have 1460 bytes used. 

But, replacing ```pinMode()``` and ```digitalWrite()``` functions with the relative port manipulation instruction:
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
we only have 854 bytes used in memory, 42% less memory used.

And, if we replace the ```setup()``` and ```loop()``` functions with the ```main()``` function:
```c++
int main() {
  DDRB |= (1<<DDB5);
  
  while(true) {
    PORTB |= 00100000;
    delay(1000);
    PORTB &= 11011111;
    delay(1000);
  }
}
```
we have only 618 bytes used, 58% less memory used.

## Built With
 - OpenJFX 11 - Graphic framework
 - Maven - Dependency Management

## Authors
Alessandro Tornesello - *Initial work* - [Iregon](github.com/iregon)

## License
This project is licensed under the MIT License - see the LICENSE.md file for details
