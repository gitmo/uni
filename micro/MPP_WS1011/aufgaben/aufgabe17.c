#include "msp430x16x.h"		// Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"
#include "aufgabe18.h"
#include "project.h"

//==============================================================
void Aufgabe17()
{
	// Inits the timer
	initSecTimer(1);

    // Interrupts global einschalten
    _bis_SR_register(GIE);

	// Infinite loop
	while(1);
}
