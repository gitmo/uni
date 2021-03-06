#include "msp430x16x.h"     // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include "aufgabe13.h"

#define TASTE0    BIT0

enum { GELB, ROT, ROT_GELB, GRUEN } nextTrafficSignal = GELB;

void playTrafficSignals()  {
    switch(nextTrafficSignal) {
            case GELB:
                P4OUT = ~(1 << LED_GELB);
                waitMilli(a_sec);
                nextTrafficSignal = ROT;
            break;

            case ROT:
                P4OUT = ~(1 << LED_ROT);
                waitMilli(5*a_sec);
                nextTrafficSignal = ROT_GELB;
            break;

            case ROT_GELB:
                P4OUT = ~((1 << LED_GELB) | (1<< LED_ROT));
                waitMilli(a_sec);
                nextTrafficSignal = GRUEN;
            break;

            case GRUEN:
                P4OUT = ~(1 << LED_GRUEN);
                waitMilli(10*a_sec);
                nextTrafficSignal = GELB;
            break;
        }
}

void Aufgabe13()
{
    // Taster 0 als Interruptquelle
    CLEAR(P1IFG, TASTE0);
    SET(P1IE, TASTE0);

    // Alle LEDs aus
    SET(P4OUT, (BIT0 | BIT1 | BIT2));

    /*
     * Watchdog auf Basis des ACLK-Takt mit dem Vorteiler 8
     * Zählerstand 32768 für RESET
     */

    WDTCTL = WDTPW + WDTHOLD;                // Stop WDT
    SET(BCSCTL1, DIVA1);                     // ACLK Divider 10b: /4    (DIVA1)
    CLEAR(BCSCTL1, DIVA0);
    WDTCTL = WDT_ADLY_1000;        // Start WDT with ACLK /32768 (TIS0 + TIS1 = 0)
    SET(IE1, WDTIE);                        //

    CLEAR(P4OUT, BIT1);

    // Interrupts global einschalten
       _bis_SR_register(GIE);

    while(true) {

        if(trafficSignalsAnimation) {
            playTrafficSignals();
            continue;
        }

        wait_ms(500/4);
        P4OUT ^= BIT0;
     }
}
