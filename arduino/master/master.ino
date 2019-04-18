#include<Wire.h>
#include<EEPROM.h>
#include<SoftwareSerial.h>
#define ADDRESS_SLAVE 0x0F
#define SLAVE_RST_PIN 8
#define DATA_LEN 9

// mode, r1, g1, b1, r2, g2, b2, conf, delay_time;
uint8_t data[DATA_LEN];
SoftwareSerial BT(7, 6);

void setup(){
	Wire.begin();
  BT.begin(115200);

  pinMode(SLAVE_RST_PIN, OUTPUT);
  resetSlave();
  delay(100);
  ReadEEPROM();

	//Serial.begin(9600);
}

void loop(){
	if(BT.available()){
	  SendRemoteData();
	}
}

void resetSlave(){
  digitalWrite(SLAVE_RST_PIN, LOW);
  digitalWrite(SLAVE_RST_PIN, HIGH);
}

void ReadEEPROM(){
  byte valid = EEPROM.read(0);
  if(valid == 0xFF){
    for(int i = 1; i <= DATA_LEN; i++)data[i-1] = EEPROM.read(i);
    Wire.beginTransmission(ADDRESS_SLAVE);
    Wire.write(data, DATA_LEN);
    delay(1);
    Wire.endTransmission();
  }
}

void WriteEEPROM(){
  EEPROM.update(0, 0xFF);
  for(int i = 1; i <= DATA_LEN; i++)EEPROM.update(i, data[i-1]);
}

void SendRemoteData(){
  uint8_t count = 0;
  while(count < DATA_LEN){
    /*if(Serial.available()){
      data[count++] = Serial.read();
      /*String t = String(count) + " : " + String(data[count-1]);
      Serial.write(t.c_str());
    }*/
    if(BT.available()){
      data[count++] = BT.read();
      BT.write(count);
    }
  }
  /*String t;
  t = "\nMode : " + String(data[0]) + "\n";
	Serial.write(t.c_str());
  t = "Conf1 : (" + String(data[1]) + ", " + String(data[2]) +", " + String(data[3]) + ")\n";
  Serial.write(t.c_str());
  t = "Conf2 : (" + String(data[4]) + ", " + String(data[5]) +", " + String(data[6]) + ")\n";
  Serial.write(t.c_str());
  t = "Resolution : " + String(data[7]) + "\n";
  Serial.write(t.c_str());
  t = "Shooting_star_len : " + String(data[8]) + "\n";
  Serial.write(t.c_str());
  t = "Rainbow_len : " + String(data[9]) + "\n";
  Serial.write(t.c_str());
  t = "Delay_time : " + String(data[10]) + "\n";
  Serial.write(t.c_str());*/

  if(data[0] == 0xFF){
    resetSlave();
    return;
  }
	Wire.beginTransmission(ADDRESS_SLAVE);
	Wire.write(data, DATA_LEN);
  delay(1);
	Wire.endTransmission();
  WriteEEPROM();
}
