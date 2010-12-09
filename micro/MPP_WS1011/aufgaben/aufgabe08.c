#include "msp430x16x.h"        // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"            // Initialisierung des Mikrocontrollers
#include "../CC1100.h"            // CC1100 Funktransceiver
#include "../system.h"            // Systemfunktionen MSB430H
#include "../interrupts.h"        // ISR - Interrupt Service Routinen
#include "stdio.h"            // includes TI MSP430F1612 
#include "../SHT11.h"   
#include "project.h"

void Aufgabe8() {
    //mit bekanntem Grundzustand initialisieren
    P5OUT = 0x00;
    
    // Wechseln des Zustands und Messen des Flankenwechsel
    while(1) {
        P5OUT ^= 0x10; //XOR.B   #0x0010,&Port_5_6_P5OUT
        
        P5OUT ^= 0x10; //XOR.B   #0x0010,&Port_5_6_P5OUT
    } //JMP     (C$L1)
}
