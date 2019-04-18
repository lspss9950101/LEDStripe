# LED Stripe #
## Introduction ##
It's a easy application of WS2812B LED stripe, which consists of attiny85 micro controllor communicating with a atmega328p chip by I2C protocol and controlled by an android application using bluetooth serial port.
## Infrastucture ##
Attiny85  \<\-\> WS2812B (using FastLED library)  
Atmega328p \<\-\> Attiny (using I2C, Atmega328p as master)  
HC-06 \<\-\> Atmega328p (using bluetooth serial)  
Android device \<\-\> HC-06 (using bluetooth protocol)  
## BOM ##
|Name		|Amount	|
|:--------------|------:|
|Attiny85	|1	|
|Atmega328p	|1	|
|HC\-06		|1	|
|WS2812B	|60	|
|Android device	|1	|
## Schematic ##
![Alt text](/res/schematic.png)
