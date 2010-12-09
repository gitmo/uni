#include "msp430x16x.h"        // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"
#include "project.h"
#include "aufgabe15.h"

enum POWER_ENUM powerState;

void Aufgabe16()
{
    // Alle LEDs aus
    SET(P4OUT, (BIT0 | BIT1 | BIT2));
      
    powerState = LPM_4;
    
    // Taster 0 interruptfähig schalten
    CLEAR(P1IFG, TASTE0);
    // Flanke auf 0 -> Interrupt beim drücken (statt loslassen)
    // fürdie invertierten Taster entspricht dies einer HF
    CLEAR(P1IES, TASTE0);
    SET(P1IE, TASTE0);
         
    // Interrupts global einschalten
    _bis_SR_register(GIE);
    
    while(true) {
        if( powerState == ACTIVE ) {
            // Ampel
            LED_SET(LED_GELB);
        } else if ( powerState == LPM_4 ) {
            LED_OFF;
            LPM4;
        }
    }
}
