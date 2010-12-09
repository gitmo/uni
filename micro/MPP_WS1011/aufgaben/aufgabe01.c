#include "msp430x16x.h"        // Systemdefinitionen von TI fÃ¼r den MSP430F1612
#include "init.h"            // Initialisierung des Mikrocontrollers
#include "CC1100.h"            // CC1100 Funktransceiver
#include "system.h"            // Systemfunktionen MSB430H
#include "interrupts.h"        // ISR - Interrupt Service Routinen
#include "stdio.h"            // includes TI MSP430F1612
#include "SHT11.h"            // SHT11 Temperatur- und Feuchtesensor

/*
 DIR bei 0 ist der PIN auf Input gestellt,
 bei 1 ist der Pin auf Output gestellt.

 Board-Belegung:
    P4.0 LED          rot
    P4.1 LED          gelb
    P4.2 LED          grÃ¼n
    P4.3 Beeper
*/
#define ROT 0
#define GELB 1
#define GRUEN 2
#define BEEPER 3
#define A_SEC 1000*1000


int Aufgabe1 (void) {
    unsigned char a = 0;
    /*
    P4DIR = 0x00;       // Alle Bits werden auf Input gestellt.
    a = 10;             // Globale Variable a bekommt Wert 10.
    P4OUT = a;          // 10 = 00001010b => P4.1 und P4.4 auf HIGH, Rest ist LOW
    P4OUT = 0x01;       // P4.0 auf HIGH, Rest auf LOW
    P4DIR = 0x07;       // 0x07 = 00000111b => Bits 0 bis 2 auf OUTPUT, Rest auf Input.
                        // Effekt: gruene und gelbe LED leuchten
    a = P4IN & 0x07;    // 0x01 & 0x07 = 0x01
    P4OUT &= 0x00;      // Alle Bits werden auf LOW gesetzt (LEDs leuchten)
    P4OUT |= 0x01;      // P4.0 wird auf HIGH gesetzt (rote LED aus)
    P4OUT |= LEDRT;     // Es Ã¤ndert sich nichts, da 0x01 | 0x01 = 0x01
    P4OUT &= ~LEDRT;    // ~0x01 = 0xFE -> 0x01 & 0xFE = 0x00
                        // Effekt: alle LEDs auf LOW und leuchten somit
    P4OUT ^= LEDRT;     // 0x00 ^ 0x01 = 0x01 (gelbe und gruene LED leuchten)
    */

    P4DIR = (1 << ROT) | (1 << GELB) | (1 << GRUEN);

    while(1) {

        /*
         * Schreiben Sie ein kleines Programm, welches den Durchlauf einer
         * Ampelsignalsequenz mit den Phasen (rt,rt-gb,gr,gb,rt) simuliert.
         * Nutzen Sie dazu die bereitgestellte Funktion fÃ¼r eine Zeitschleife.
        */

        //rot
        P4OUT = ~(1 << ROT);
        wait(5*A_SEC);

        //rot gelb
        P4OUT = ~((1 << GELB) | (1 << ROT));
        wait(A_SEC);

        //gelb
        P4OUT |= (1 << ROT);
        wait(A_SEC);

        //gruen
        P4OUT = ~(1 << GRUEN);
        wait(5*A_SEC);

    }

   return 0;
}

