# LED Stripe #
## Introduction ##
It's a easy application of WS2812B LED stripe, which consists of attiny85 micro controllor communicating with a atmega328p chip by I2C protocol and controlled by an android application using bluetooth serial port. Also, to prevent resetting everytime rebooting the computer, the setting will be saved in the EEPROM of Atmega328p and loaded when rebooting.
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
Config : optional data for some modes  
Delay : in millisecond  
### Mode ###
Mode is a byte used to indicate how the WS2812B animates.  

|Value	|Mode		|Config		|
|:------|:--------------|:--------------|
|0x00	|Blank		|NULL		|
|0x01	|Rainbow	|Period		|
|0x02	|Gradient	|Period		|
|0x03	|Breathing	|Period		|
|0x04	|Solid		|NULL		|
|0x05	|SolidGradient	|Period		|

### Instance ###
#### Circuit ####
![Circuit](/res/circuit_1.jpg)
![Circuit](/res/circuit_2.jpg)
#### Gradient ####
[![Gradient Example](http://img.youtube.com/vi/ES8kneXbvdg/0.jpg)](http://www.youtube.com/watch?v=ES8kneXbvdg "Gradient Example")
#### Rainbow ####
[![Rainbow Example](http://img.youtube.com/vi/ES8kneXbvdg/0.jpg)](https://youtu.be/ryr7hZnHJKw "Rainbow Example")
#### Breathing ####
[![Breathing Example](http://img.youtube.com/vi/rJafmvNVk_8/0.jpg)](http://www.youtube.com/watch?v=rJafmvNVk_8 "Breathing Example")
