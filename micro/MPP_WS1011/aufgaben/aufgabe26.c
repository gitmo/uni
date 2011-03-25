#include "msp430x16x.h"     // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include <math.h>
#include "project.h"
#include "aufgabe20.h"
#include "aufgabe21.h"

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

#pragma vector = DACDMA_VECTOR
__interrupt void DMA_ISR (void)
{
    SET(DMA0CTL, DMAEN);

    // Clears the interrupt flag
    CLEAR(DMA0CTL, DMAIFG);
}

void initDAC() {
    P6SEL = BIT6;
    DAC12_0CTL = 0;
    DAC12_0CTL = DAC12SREF_2 + DAC12AMP_7 + DAC12CALON + DAC12IR;
}

void initDMA()
{
    //DMACTL0 DMA Control Register 0
    //  trigger select: Timer_B (TBCCR0.IFG)
    DMACTL0 = DMA0TSEL_8;
    //DMACTL0 = DMA0TSEL_0;

    //DMACTL1 DMA Control Register 1
    //  DMA transfer immediately: DMAONFETCH = 0
    //  Disable NMI interrupt: ENNMI = 0
    DMACTL1 = 0;

    //DMAxCTL DMA Channel x Control Register
    //  Single DMA Transfer mode: DMADTx = 000
    //  Destination address is unchanged: DSTINCRx = 00
    //  Source address is incremented: SRCINCRx = 11
    //  DMA enable: DMAEN = 1
    DMA0CTL = DMADT_0 + DMASRCINCR_3 + DMALEVEL + DMAIE;

    //DMA Source Address Register
    DMA0SA = (unsigned int) &sinus;

    //DMA Destination Address
    DMA0DA = (unsigned int) &DAC12_0DAT;

    //DMA Size Address Register
    //  one byte ist transferrred
    DMA0SZ = SAMPLES;

    //Anschalten
    SET(DMA0CTL, DMAEN);
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
    // SET(TBCCTL0, CCIE);
}

void Aufgabe26()
{
    calcSinus();
    initTimer()
    initDAC();
    initDMA();
    _bis_SR_register(GIE);

    while(1);
}
