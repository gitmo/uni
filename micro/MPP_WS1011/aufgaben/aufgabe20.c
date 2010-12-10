#include "msp430x16x.h"      // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"
#include "project.h"
#include "aufgabe20.h"

void uart1_put_char(char c)  {
    while((U1TCTL & TXEPT) == 0);
    
    U1TXBUF = c;
}

void Aufgabe20() {
    
    while(1) {
        uart1_put_char('?');
    }
}
