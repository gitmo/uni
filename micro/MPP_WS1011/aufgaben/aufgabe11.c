#include "msp430x16x.h"		// Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"

#define MCU_RESET (WDTCTL = 0)

void Aufgabe11()
{
	CLEAR(P4OUT, BIT1);
	wait_ms(1000);
	SET(P4OUT, BIT1);
	wait_ms(500);
	
	MCU_RESET;
	
	CLEAR(P4OUT, BIT0);
}
