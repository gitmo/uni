#include "msp430x16x.h"        // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"            // Initialisierung des Mikrocontrollers
#include "../CC1100.h"            // CC1100 Funktransceiver
#include "../system.h"            // Systemfunktionen MSB430H
#include "../interrupts.h"        // ISR - Interrupt Service Routinen
#include "stdio.h"            // includes TI MSP430F1612 
#include "../SHT11.h"  

#define LED_ROT 0
#define LED_GELB 1
#define LED_GRUEN 2

#define BUTTON_R 0
#define BUTTON_L 1

     // 00, 01,    10,   11 
enum { OFF, RIGHT, LEFT, BOTH } but_state;

int counter = 0;

void displayBits(int c) {
    int bit0 = c & 0x01;
    int bit1 = (c & 0x02) >> 1;
    int bit2 = (c & 0x04) >> 2;
    P4OUT = ~( (bit0 << LED_GRUEN) | (bit1 << LED_GELB) | (bit2 << LED_ROT) );
}

void Aufgabe4() {

    //Alle LED-Bits auf Output stellen
    P4DIR = (1 << LED_ROT) | (1 << LED_GELB) | (1 << LED_GRUEN);

    while( true ) {
        but_state = P1IN & 0x03;
        
        switch (but_state) {
            case OFF:   // do nada
                break;
            case RIGHT: // decrement
                if (--counter < 0)
                    counter = 7;
                break;
            case LEFT:  // increment
                counter = (++counter % 8);
                break;
            case BOTH:  // reset
                counter = 0;
                break;
        }
        displayBits(counter);
        wait(10000);    // Chill a little
    }
}
