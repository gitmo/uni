#include "msp430x16x.h"      // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include "project.h"
#include "aufgabe20.h"
#include "aufgabe21.h"
#include "aufgabe22.h"

void Aufgabe22()
{
    char tmpBuffer[32];

		// Interrupts global einschalten
    _bis_SR_register(GIE);

    uart1_put_str("Initialized!");

		while(1) {
			if(gotNewLine) {
				sprintf(tmpBuffer,"String Länge: %d", buffer_counter);
				gotNewLine = false;
				uart1_put_str(tmpBuffer);
			}
		}
}
