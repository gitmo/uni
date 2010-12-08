#ifndef AUFGABE15_H_
#define AUFGABE15_H_

enum POWER_ENUM { ACTIVE, LPM_1, LPM_2, LPM_3, LPM_4, OFF };
extern enum POWER_ENUM powerState;

extern int watchdogSecCounter;

#endif /*AUFGABE15_H_*/
