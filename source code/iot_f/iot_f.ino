#include <DallasTemperature.h>
#include <OneWire.h>
#define ONE_WIRE_BUS 14
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
//------------------------------------
#include <ESP8266WiFi.h>
#include <SoftwareSerial.h>

// sensor
 
int Val = 0; //初始 感測值
int x=0;
float y=0;
//connect
char* SID = "dd-wrt";
char* PWD = "";
char* IP = "192.168.1.106";
uint16_t port =8081;
String file = "temp.php";

char* str = ""; //http 回覆狀態
String message = "";
 
char buffer_tem[500];
char buffer[500];
int commaPosition;
int i=0;
int c=0;
WiFiClient client;
char inString[32]; // string for incoming serial data
int stringPos = 0; // string index counter
boolean startRead = false; // is reading?
 String resp="",ans="";


void init_wifi(){
  WiFi.begin(SID, PWD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
}
void uploadData(float temp)
{
   if (client.connect(IP,port)) {
      String getStr = "GET /"+file+"?temp="+temp;
      getStr +=" HTTP/1.1\r\nHost:"+(String)IP;
      getStr += "\r\n\r\n";
      Serial.println("---------------------------------------------");
      Serial.println(getStr);
      Serial.println("---------------------------------------------");
      client.print(getStr);
            while(client.available())
      {    
        // The esp has data so display its output to the serial window 
        resp = client.readString(); // read the next character.
      } 
    ans = catch_word(resp);
    if(ans == "on"){
      x=1;
      Serial.println("switch on");
     // digitalWrite(8,HIGH);
    }
    else {
      x=0;
      Serial.println("switch off");
      //digitalWrite(8,LOW);
    }
     // delay(2000);  
  }
}
String catch_word(String pageread){
  stringPos = 0;
  memset( &inString, 0, 32 ); //clear inString memory
  int i ;
  for(i = 0;i < pageread.length() ; i++){
      char c = pageread.c_str()[i];
      if (c == '^' ) {
        startRead = true;
      }
      else if(startRead){
        if(c != '!'){ 
          inString[stringPos] = c;
          stringPos ++;
        }
       else{
          startRead = false;
          return inString;
        }
      }
    }  
} 

//------------------------------------

const int INA = 12;
const int INB = 13;
byte speed = 80;
void setup()
{
pinMode(INA,OUTPUT);
pinMode(INB,OUTPUT);
 Serial.begin(115200); // for debug
 Serial.println("Temperature Sensor");
  // 初始化 
 init_wifi();  //設定ESP8266,改變模式,連線wifi
  Serial.println("--------------");
sensors.begin();
}
void loop()
{
 
 sensors.requestTemperatures();
 y=sensors.getTempCByIndex(0);
 Serial.println(y);
 
  if(x==1){
digitalWrite(INA,LOW);
digitalWrite(INB,HIGH);} 
else{
digitalWrite(INA,LOW);
digitalWrite(INB,LOW); 
}
Serial.println("in....");
   uploadData(y);
   delay(500);
 
}
