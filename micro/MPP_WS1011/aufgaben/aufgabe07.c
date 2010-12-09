#include "msp430x16x.h"         // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"            // Initialisierung des Mikrocontrollers
#include "../CC1100.h"          // CC1100 Funktransceiver
#include "../system.h"          // Systemfunktionen MSB430H
#include "../interrupts.h"      // ISR - Interrupt Service Routinen
#include "stdio.h"              // includes TI MSP430F1612
#include "../SHT11.h"
#include "project.h"

void Aufgabe7() {
    // Taster 0 interruptfähig schalten
    CLEAR(P1IFG, TASTE0);
    SET(P1IE, TASTE0);

    // Interrupts global einschalten
    _bis_SR_register(GIE);

    P5SEL |= BIT4;  // P5 MCLK

    while(true);
}
