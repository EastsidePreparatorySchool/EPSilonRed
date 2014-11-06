#ifndef EPS_DECLARATIONS
#define EPS_DECLARATIONS
#define button1   0x01
#define button2   0x02
#define button3   0x04
#define button4   0x08
#define button5   0x10
#define button6   0x20
#define button7   0x40
#define button8   0x80
#define button9  0x100
#define button10 0x200
#define SCOOP_UP 128
#define SCOOP_DOWN 0
#define CHECK_FOR_STOP() {getJoystickSettings(joystick); if (joystick.StopPgm) return;}
#endif