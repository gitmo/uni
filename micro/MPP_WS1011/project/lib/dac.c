#include <msp430x16x.h>      // Systemdefinitionen von TI für den MSP430F1612
#include "../../init.h"        // Initialisierung des Mikrocontrollers
#include "../../CC1100.h"      // CC1100 Funktransceiver
#include "../../system.h"      // Systemfunktionen MSB430H
#include "../../interrupts.h"  // ISR - Interrupt Service Routinen
#include <stdio.h>          // includes TI MSP430F1612
#include "../../SHT11.h"

/**
 * for use write your data in DAC12_0DAT
 * 
 */
void initDAC0() {
    /* Zur Steuerung des DA-Wandlers werden folgende Register genutzt:
     * DAC12_0CTL Controlregister DA Wandler 0
     * DAC12_0DAT Datenregister DA Wandler 0
     * DAC12_1CTL Controlregister DA Wandler 1
     * DAC12_1DAT Datenregister DA Wandler 1
     */

    // Für alle Aufgaben sollte der DA-Wandler mit 3V (VEREF+)
    // Referenzspannung und 12 Bit Auflösung (DAC12RES) genutzt werden.
    // DAC12SREFx   11 := VEREF+
    // DAC12RES      0 := 12-bit resolution
    // DAC12LSELx    0 := Direct latch
    // DAC12CALON    1 := Initiate calibration sequence
    // DAC12IR       1 := DAC12 full-scale output = 1 x reference voltage
    // DAC12AMPx   111 := High speed/current
    // DAC12DF       0 := Data-format: Straight binary
    // DAC12IE       0 := interrupt disable
    // DAC12IFG      0 := no interrupt pending
    // DAC12ENC      1 := enable conversion if DAC12LSELx > 0

    P6SEL = BIT6;
    DAC12_0CTL = 0;
    DAC12_0CTL = DAC12SREF_2 + DAC12AMP_7 + DAC12CALON + DAC12IR;
}

void initDAC1() {
    P6SEL = BIT7;
    DAC12_1CTL = 0;
    DAC12_1CTL = DAC12SREF_2 + DAC12AMP_7 + DAC12CALON + DAC12IR;
}
    
