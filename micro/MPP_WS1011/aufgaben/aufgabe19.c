#include "msp430x16x.h"      // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include "project.h"
#include "aufgabe19.h"

day_time_t dayTime = { 0, 0, 0 };
day_time_t t1 = { 0, 0, 1 };
day_time_t t2 = { 0, 0, 2 };

bool laterThan( day_time_t x1, day_time_t x2 ){
    int epoch1 = x1.hh * 60 + x1.mm * 60 + x1.ss;
    int epoch2 = x2.hh * 60 + x2.mm * 60 + x2.ss;
    return epoch1 > epoch2;
}

void Aufgabe19()
{
    initSecTimer(1);

    // Interrupts global einschalten
    _bis_SR_register(GIE);

    while(1) {
        if(laterThan(t1, dayTime))
            LED_SET(LED_ROT);
        if(laterThan(t2, dayTime))
            LED_OFF;
    }
}
