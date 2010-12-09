#include "msp430x16x.h"     // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"

void enableInterrupt() {
    CLEAR(P1IFG, BIT0 | BIT1);
    SET(P1IE, BIT0 | BIT1);
}

void disableInterrupt() {
    CLEAR(P1IE, BIT0 | BIT1);
}

void Aufgabe12()
{
    SET(P4DIR, BIT0|BIT1|BIT2);

    enableInterrupt();

    //Schaltet Interrupt fuer Button1 an
    _bis_SR_register(GIE);


    while(1) {
        P4OUT ^=  BIT2;

        if(P4OUT & BIT2)
            disableInterrupt();
        else
            enableInterrupt();

        wait_ms(5000);
    }
}
