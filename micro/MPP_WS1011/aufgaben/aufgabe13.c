#include "msp430x16x.h"		// Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"

void Aufgabe13()
{
    // Indicate we're started
    CLEAR(P4OUT, BIT2);            // Green LED on
    
    /*
     * Watchdog auf Basis des ACLK-Takt mit dem Vorteiler 8
     * Zählerstand 32768 für RESET
     */

    WDTCTL = WDTPW + WDTHOLD;                // Stop WDT
    BCSCTL1 |= DIVA_3;                        // ACLK Divider 3: /8    (DIVA0 + DIVA1)
    WDTCTL = WDTPW + WDTCNTCL + WDTSSEL;     // Start WDT with ACLK /32768 (TIS0 + TIS1 = 0)
    
    /*
     * Falls der Watchdog feuert ensteht die skurille Situation das Programm
     * mit einer langsameren Taktfrequenz neu gestartet wird, da das DCOCTL
     * Register zurückgesetzt wird. Dieses Register wird normalerweise bei
     * Programmstart in DCO() in main.c initialisiert. Die Programmausführung
     * setzt beim RESET bei vorigen Program Counter fort (hier fast immer in
     * wait_ms().
     */
        
    while(true) {
        wait_ms(500);
        SET(P4OUT, BIT0);    // Red LED off
		while (P1IN & 0x03){	// While buttons A or B are pressed
			wait_ms(10);
	        // Reset watchdog counter, preserve lower 8 bits
			WDTCTL = WDTPW + WDTCNTCL + (WDTCTL & 0x0F);
		}
        // Reset watchdog counter, preserve lower 8 bits
		WDTCTL = WDTPW + WDTCNTCL + (WDTCTL & 0x0F);
        wait_ms(500);
        CLEAR(P4OUT, BIT0);        // Red LED on
    }
}
