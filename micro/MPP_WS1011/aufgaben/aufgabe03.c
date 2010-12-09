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

#define a_sec 100

enum { OFF, GELB, ROT, ROT_GELB, GRUEN } nextSignal;

void waitMilli(unsigned int i) {
    for(;i>0;--i)
        wait(1000);
}

void Aufgabe3() {
    
    int buttonAlreadyPressed = 0;
    int cooldown = 0;
    nextSignal = OFF;
    
    //Alle LED-Bits auf Output stellen
    
    P4DIR = (1 << LED_ROT) | (1 << LED_GELB) | (1 << LED_GRUEN);
    while(true) {
        // Pausiert die Ampelschaltung nach einem durchlaufenden Zyklus
        if(cooldown) {
            waitMilli(10*a_sec);
            cooldown = 0;
        }
        
        // setzt den naechsten Teilzyklus
        switch(nextSignal) {
            case OFF:
                P4OUT = 0xFF;
                if(buttonAlreadyPressed)
                    nextSignal = GELB;
            break;
            
            case GELB:
                P4OUT = ~(1 << LED_GELB);
                waitMilli(a_sec);
                nextSignal = ROT;
            break;
            
            case ROT:
                P4OUT = ~(1 << LED_ROT);
                waitMilli(5*a_sec);
                nextSignal = ROT_GELB;
            break;
            
            case ROT_GELB:
                P4OUT = ~((1 << LED_GELB) | (1<< LED_ROT));
                waitMilli(a_sec);
                nextSignal = GRUEN;
            break;
            
            case GRUEN:
                P4OUT = ~(1 << LED_GRUEN);
                waitMilli(10*a_sec);
                nextSignal = OFF;
                buttonAlreadyPressed = 0;
                cooldown = 1;
            break;
        }
        
        // Wird der Button gedrueckt, sow wird ein neuer Zyklus gestartet
        if(!buttonAlreadyPressed) {
            if(P1IN == 0x01) {
                buttonAlreadyPressed = 1;
            }
        }
    }
}
