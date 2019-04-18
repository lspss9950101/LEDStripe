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
![schematic](/res/schematic.png)
## Detail ##
### Bluetooth patern ###
The bluetooth patern contains 9 bytes.  
The first byte indicates the displaying mode.  
The following three bytes indicate primary color.  
Other following three bytes indicate secondary color.  
The next byte indicates excessive config data.  
The last byte indicates the delay time in milliseconds.  

|XX	|XX XX XX	|XX XX XX	|XX	|XX	|
|:------|:--------------|:--------------|:------|:------|
|Mode	|Primary color	|Secondary color|config	|delay	|

Mode : refer to section mode  
Color : express as RGB with three bytes  
config : optional data for some modes  
delay : in millisecond  
