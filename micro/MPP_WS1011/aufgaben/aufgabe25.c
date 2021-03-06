#include "msp430x16x.h"     // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include "project.h"
#include "aufgabe20.h"
#include "aufgabe21.h"
#include "math.h"

#define SAMPLES 100
#define PI 3.1415926535897932384626433832795f

unsigned short sinus[SAMPLES];
unsigned short index = 0;

void calcSinus()
{
    unsigned short i;
    double amplitude, dc_offset, step_deg;

    // Output-Range liegt zwischen 0 und 3V, entspricht bei 12Bit 0 bis 4095:
    // Die Sinusfunktion soll eine ...
    amplitude = 4095 / 3;         // Amplitude von 1V und einen ...
    dc_offset = 4095 / 2;         // Gleichspannungsanteil von 1,5V besitzen
    step_deg = (2*PI) / SAMPLES;  // Größe der Schritte einer vollen Periode.

    for(i=0; i<SAMPLES; i++) {
        sinus[i] = (unsigned short) (dc_offset + amplitude * sin( i * step_deg ));
    }
}

void initDAC() {
    // Zur Steuerung des DA-Wandlers werden folgende Register genutzt:
    // DAC12_0CTL Controlregister DA Wandler 0
    // DAC12_0DAT Datenregister DA Wandler 0
    // DAC12_1CTL Controlregister DA Wandler 1
    // DAC12_1DAT Datenregister DA Wandler 1

    // Für alle Aufgaben sollte der DA-Wandler mit 3V (VeREF+)
    // Referenzspannung und 12 Bit Auflösung (DAC12RES) genutzt werden.
    // DAC12SREFx   10 := VeREF+
    // DAC12RES      0 := 12-bit resolution
    // DAC12LSELx    0 := Direct latch
    // DAC12CALON    1 := Initiate calibration sequence
    // DAC12IR       1 := DAC12 full-scale output = 1 x reference voltage
    // DAC12AMPx   111 := High speed/current
    // DAC12DF       0 := Data-format: Straight binary
    // DAC12IE       0 := interrupt disable
    // DAC12IFG      0 := no interrupt pending
    // DAC12ENC      0 := enable conversion if DAC12LSELx > 0

    P6SEL = BIT6;
    DAC12_0CTL = 0;
    DAC12_0CTL = DAC12SREF_2 + DAC12AMP_7 + DAC12CALON + DAC12IR;
}

void initTimer() {
    // Divisor 1 for ACL Clock source
    CLEAR(BCSCTL1, DIVA0);
    CLEAR(BCSCTL1, DIVA1);

    // Halts timer
    CLEAR(TBCTL, MC0);
    CLEAR(TBCTL, MC1);

    // Clear the timer
    SET(TBCTL, TBCLR);

    // Sets TBSSEL 01b => Clock Source ACLK
    SET(TBCTL, TBSSEL0);
    CLEAR(TBCTL, TBSSEL1);

    // Sets counter length to 16-Bit
    CLEAR(TBCTL, CNTL0);
    CLEAR(TBCTL, CNTL1);

    // Sets the timer to 2048 cycles, i.e. 500 ms on ACLK with divisor 8
    TBCCR0 = 2; // TODO: 1000 cycles?

    // Starts timer ... Mode control 01b -> Up mode
    SET(TBCTL, MC0);
    CLEAR(TBCTL, MC1);

    // Enables interrupt
    SET(TBCCTL0, CCIE);
}

#pragma vector = TIMERB0_VECTOR
__interrupt void TIMERB0_ISR (void)
{
    DAC12_0DAT = sinus[index] & 4095;
    index = (index+1) % SAMPLES;
    // Clears the interrupt flag
    CLEAR(TBCCTL0, CCIFG);
}

void Aufgabe25()
{
    calcSinus();
    initDAC();
    initTimer();
    _bis_SR_register(GIE);
    while(1);
}
