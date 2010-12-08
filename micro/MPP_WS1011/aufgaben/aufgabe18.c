#include "msp430x16x.h"		// Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"
#include "project.h"
#include "common.h"
#include "aufgabe18.h"


void initSecTimer(int sec) {
	// Divisor 8 for ACL Clock source
	SET(BCSCTL1, DIVA0);
	SET(BCSCTL1, DIVA1);
	
	// Halts timer
	CLEAR(TBCTL, MC0);
	CLEAR(TBCTL, MC1);

	// Clear the timer
	SET(TBCTL, TBCLR);
	
	// Sets TBSSEL 01b => Clock Source ACLK
	SET(TBCTL, TBSSEL0);
	CLEAR(TBCTL, TBSSEL1);
	
	// Sets counter length to 16-Bit
	CLEAR(TBCTL, CNTL0);
	CLEAR(TBCTL, CNTL1);

	// Sets the timer to 4096 cycles, i.e. 1000 ms on ACLK with divisor 8
	TBCCR0 = sec * 0x0FFF;
	
	// Starts timer ... Mode control 01b -> Up mode
	SET(TBCTL, MC0);
	CLEAR(TBCTL, MC1);
	
	// Enables interrupt
	SET(TBCCTL0, CCIE);
}

//==============================================================
void Aufgabe18()
{
	
	initSecTimer(TIMER_SEC);

    // Interrupts global einschalten
    _bis_SR_register(GIE);

	// Infinite loop
	while(1) {
		LPM3;
	}
}
