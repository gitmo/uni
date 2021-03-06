#include "msp430x16x.h"      // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include "project.h"
#include "aufgabe20.h"
#include "aufgabe21.h"

void initADU() {
    //Modifizierbar
    ADC12CTL0 = 0;

    //Sample und Hold 256 Cycles
    //ADC an
    ADC12CTL0 = SHT0_8 + ADC12ON;

    //Taktquelle auf ADC12OSC (5V)
    //Interner Sample and Hold Pulse-Modi
    ADC12CTL1 = SHP;

    //End of Squence
    //A4 selektiert
    //VR+ = AVCC
    //VR- =AVSS
    ADC12MCTL0 = EOS + INCH_4;

    //Portleitung selektieren
    P6SEL = BIT4;
    //Auf Input schalten
    CLEAR(P6DIR,BIT4);

    //ADU anschalten
    SET(ADC12CTL0, ENC);
}

unsigned int readADU() {
    while(ADC12CTL1 & ADC12BUSY);

    // N_adc = 4095 * (Vin - Vr-) / (Vr+ - Vr-)
    // =>
    // Vr- = 0V, Vr+ = 3V
    // Vin = (N_adc * 3) / 4095

    // Result in milli volts
    return ((ADC12MEM0 / 4095.0) * 3.0) * 1000;
}

#pragma vector = TIMERB0_VECTOR
__interrupt void TIMERB0_ISR (void)
{
    char buffer[32];
    float mV;

    // Software-controlled sample-and-conversion start.
    SET(ADC12CTL0, ADC12SC);
    mV = readADU() / 1000.0;
    sprintf(buffer, "ADU: %.2f V\r\n", mV);
    uart1_put_str(buffer);

    LED_OFF;
    if(mV < 1.0)
        LED_SET(LED_GELB);
    else if (mV < 2.0)
        LED_SET(LED_GRUEN);
    else
        LED_SET(LED_ROT);
    // Clears the interrupt flag
    CLEAR(TBCCTL0, CCIFG);
}

void Aufgabe23()
{
    initADU();

    initSecTimer(1);

    _bis_SR_register(GIE);

    while(1);
}
