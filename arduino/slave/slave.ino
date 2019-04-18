#include<config.h>
#include<TinyWireS.h>
#include<FastLED.h>
#define ADDRESS_SLAVE 0x0F
#define LED_PIN 1
#define DATA_LEN 9

short iterator = 0;
CRGB leds[NUM_LEDS];

uint8_t data[DATA_LEN];


void setup(){
	TinyWireS.begin(ADDRESS_SLAVE);
	TinyWireS.onReceive(ReceiveEvent);

  data[0] = 0; // mode
  data[1] = 255; data[2] = 0; data[3] = 255; // color1
  data[4] = 0; data[5] = 255; data[6] = 255; // color2
  data[7] = 64; // conf
  data[8] = 20; // delay_time

  FastLED.addLeds<WS2812B, LED_PIN, GRB>(leds, NUM_LEDS);

  pinMode(3, OUTPUT);
}
int k = 0;
void loop(){
	switch(data[0]){
    case CLEAN_LED:
      for(int i = 0; i < NUM_LEDS; i++)leds[i] = CRGB(0,0,0);
      break;
		case RAINBOW_FLOW:
			ModeRainbow();
			break;
		case SHOOTING_STAR:
			ModeSolidShootingStar();
			break;
		case BREATHING:
			ModeBreath();
			break;
    case SINGLE_SOLID:
      for(int i = 0; i < NUM_LEDS; i++)leds[i] = CRGB(data[1], data[2], data[3]);
      break;
		case DUE_GRADUAL:
			ModeGradual();
			break;
	}
	FastLED.show();
  tws_delay(data[8]);
  TinyWireS_stop_check();
}

void ModeRainbow(){
  for(uint8_t j = 0; j < NUM_LEDS; j++){
    uint8_t hue = (j + iterator) * 255.f / data[7];
    leds[j] = CHSV(hue, 255, 255);
  }
  iterator = (iterator + 1) % data[7];
} 
/*
void ModeShootingStar(){
	for(uint8_t i = 0; i < data[8]; i++){
		double ratio = (i * 1.f / data[8]) * (i * 1.f / data[8]);
		if(i + iterator < NUM_LEDS && i + iterator >= 0)leds[i + iterator] = CRGB((data[1] * ratio), (data[2] * ratio), (data[3] * ratio));
	}
  iterator++;
  if(iterator > NUM_LEDS)iterator = -int(data[8]);
}
*/
void ModeSolidShootingStar(){
	for(uint8_t i = 0; i < NUM_LEDS; i++){
    uint8_t p = (i + iterator) % (data[7] * 2);
    p = (p > data[7] ? data[7] * 2 - p : p);
    CRGB color = CRGB((data[1] * p + data[4] * (data[7] - p)) / data[7],
                      (data[2] * p + data[5] * (data[7] - p)) / data[7],
                      (data[3] * p + data[6] * (data[7] - p)) / data[7]);
		leds[i] = color;
	}
	iterator = (iterator + 1) % (data[7] * 2);
}

void ModeBreath(){
	static bool color = 0;
  if(color)for(uint8_t i = 0; i < NUM_LEDS; i++)leds[i] = CRGB(data[1], data[2], data[3]);
  else for(uint8_t i = 0; i < NUM_LEDS; i++)leds[i] = CRGB(data[4], data[5], data[6]);
	uint8_t brightness = 255 - 255.f / (data[7]*data[7]) * (iterator - data[7]) * (iterator - data[7]);
	FastLED.setBrightness(brightness);
	iterator++;
	if(iterator > data[7]*2){
		iterator = 0;
		color = !color;
	}
}

void ModeGradual(){
  CHSV c1, c2;
  c1 = rgb2hsv_approximate(CRGB(data[1], data[2], data[3]));
  c2 = rgb2hsv_approximate(CRGB(data[4], data[5], data[6]));
	int tmp = (iterator > data[7] ? 2*data[7] - iterator : iterator);
	CRGB color = CHSV(uint8_t((c1.hue * tmp + c2.hue * (data[7] +0.f - tmp)) / data[7]),
			              uint8_t((c1.sat * tmp + c2.sat * (data[7] +0.f - tmp)) / data[7]),
			              uint8_t((c1.val * tmp + c2.val * (data[7] +0.f - tmp)) / data[7]));
  for(int i = 0; i < NUM_LEDS; i++)leds[i] = color;
	iterator = (iterator + 1) % (data[7]*2);
}

void ReceiveEvent(uint8_t len){
  for(uint8_t i = 0; i < len; i++)data[i] = TinyWireS.receive();
  iterator = 0;
  FastLED.setBrightness(BRIGHTNESS);
}
