#include "msp430x16x.h"        // Systemdefinitionen von TI fÃ¼r den MSP430F1612
#include "../init.h"            // Initialisierung des Mikrocontrollers
#include "../CC1100.h"            // CC1100 Funktransceiver
#include "../system.h"            // Systemfunktionen MSB430H
#include "../interrupts.h"        // ISR - Interrupt Service Routinen
#include "stdio.h"            // includes TI MSP430F1612
#include "../SHT11.h"            // SHT11 Temperatur- und Feuchtesensor

#define TasterA (0x01)
#define ROT 0
#define GELB 1
#define GRUEN 2

int Aufgabe2(void) {
    /*
    unsigned char a = 0;    // Lokale Variable vom Typ Char (8-Bit), 0-255
    P1DIR = 0xF8;           // 11111000b (P1DIR.[0..2] werden auf Input gesetzt
    a = 10;                 // 00001010b
    // Q: Was passiert wenn jetzt der Taster an der Portleitung P1.0 gedrÃ¼ckt wird?
    // A: P1IN.0 = HIGH
    P1OUT = a;              // P1OUT.0 und P1OUT.2 auf LOW
                            // (kein Effekt, da schon als Input gesetzt)
    a = P1IN & 0x00;        // Taste an P1.0 gedrÃ¼ckt => a = 0x00
    a = P1IN & 0x03;        // Taste an P1.0 gedrÃ¼ckt => a = 0x01
    a = P1IN & 0x02;        // Taste an P1.0 gedrÃ¼ckt => a = 0x00
    a = P1IN & 0x01;        // Taste an P1.1 gedrÃ¼ckt => a = 0x01
    a = P1IN & 0x03;        // Tasten an P1.0 und P1.1 gedrÃ¼ckt a = 0x03
    P4OUT = P1IN & TasterA; // Taster an P1.0 nicht gedrÃ¼ckt a = 0x00
    P4OUT = P1IN & TasterA; // Taster an P1.0 gedrÃ¼ckt a = 0x01
    */

    /*
     *  P4.0 LED          rot
     *  P4.1 LED          gelb
     *  P4.2 LED          grÃ¼n
     * Schreiben Sie ein Programm mit folgender FunktionalitÃ¤t:
     *  Keine Taste gedrÃ¼ckt gelbe LED (P4.1) an
     *  Rechte Taste gedrÃ¼ckt grÃ¼ne LED (P4.2) an
     *  Linke Taste gedrÃ¼ckt rote LED (P4.0) an
     *  Beide Tasten gedrÃ¼ckt gelbe LED (P4.1) an
     */

    // Alle LED-Bits auf Output stellen
    P4DIR = (1 << ROT) | (1 << GELB) | (1 << GRUEN);

    while(true) {
        switch(P1IN) {
            //linke Taste gedrÃ¼ckt
            case 0x01:
                P4OUT = ~(1 << GRUEN);
            break;

            //rechte Taste gedrÃ¼ckt
            case 0x02:
                P4OUT = ~(1 << ROT);
            break;

            //beide Tasten gedrÃ¼ckt
            case 0x03:
                P4OUT = ~(1 << GELB);
            break;

            //keine Taste gedrÃ¼ckt 0x00
            default:
                P4OUT = ~(1 << GELB);
            break;
        }

        wait(100);
    }

    return 0;
}

