#include <Servo.h>

Servo servo;

void setup() {
  // put your setup code here, to run once:
  servo.attach(10);

  
}

void loop() {
  // put your main code here, to run repeatedly:

  servo.write(180);
  delay(2000);
  servo.write(0);
  delay(2000);
  
}
