#include "msp430x16x.h"      // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"
#include "project.h"
#include "aufgabe15.h"

int watchdogSecCounter;

void Aufgabe15()
{
    // Alle LEDs aus
    SET(P4OUT, (BIT0 | BIT1 | BIT2));
    
    /*
     * Watchdog auf Basis des ACLK-Takt mit dem Vorteiler 8
     * Zählerstand 32768 für RESET
     */

    WDTCTL = WDTPW + WDTHOLD;   // Stop WDT
    SET(BCSCTL1, DIVA1);                     // ACLK Divider 10b: /4    (DIVA1)
    CLEAR(BCSCTL1, DIVA0);
    WDTCTL = WDT_ADLY_1000;     // Start WDT with ACLK /32768 (TIS0 + TIS1 = 0)
    SET(IE1, WDTIE);
     
    powerState = LPM_4;
    watchdogSecCounter = 0;
    
    // Taster 0 interruptfähig schalten
    CLEAR(P1IFG, TASTE0);
    SET(P1IE, TASTE0);
         
    // Interrupts global einschalten
    _bis_SR_register(GIE);
    
    while(true) {
        if( powerState == ACTIVE )
            LED_SET(LED_GELB);
        else if ( powerState == LPM_4 ) {
            LED_OFF;
            LPM4;
        } 
        while( TASTEN_BITS & TASTE0 ) {
            if( watchdogSecCounter > 2 ) {
                powerState = LPM_4;
                break;
            }
            wait_ms(10);
        }           
    }
}
