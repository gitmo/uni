#include <msp430x16x.h>      // Systemdefinitionen von TI für den MSP430F1612
#include "../../init.h"        // Initialisierung des Mikrocontrollers
#include "../../CC1100.h"      // CC1100 Funktransceiver
#include "../../system.h"      // Systemfunktionen MSB430H
#include "../../interrupts.h"  // ISR - Interrupt Service Routinen
#include <stdio.h>          // includes TI MSP430F1612
#include "../../SHT11.h"

void initADU() {

    //Zweitfunktion fuer Bit 0, 1 und 2 selektieren
    P6SEL = BIT0 + BIT1 + BIT2;

    //Modifizierbar
    ADC12CTL0 = 0;

    //Sample und Hold 256 Cycles
    //  ADC an
    //  Multiple sample and conversion
    //  Sample-and-hold time: 256 cycles
    ADC12CTL0 = MSC + SHT0_10 + SHT1_10 + ADC12ON;

    //Taktquelle auf ADC12OSC (5V)
    //Interner Sample and Hold Pulse-Modi
    //Conversion sequence mode: Sequence-of-channels
    //Conversion start address: ADC12MEM0
    ADC12CTL1 = SHP + CONSEQ_1;

    //End of Squence
    //A0 (ax), A1(ay), A2(az) initialisieren
    //VR+ = AVCC
    //VR- =AVSS
    ADC12MCTL0 = INCH_0;
    ADC12MCTL1 = INCH_1;
    ADC12MCTL2 = INCH_2 + EOS;

    //ADU anschalten
    SET(ADC12CTL0, ENC);
}