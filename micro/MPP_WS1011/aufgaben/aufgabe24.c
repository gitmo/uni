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

bool printValues = false;

#pragma vector = TIMERB0_VECTOR
__interrupt void TIMERB0_ISR (void)
{
	SET(ADC12CTL0, ADC12SC);
    
    printValues = true;
    
    // Clears the interrupt flag
    CLEAR(TBCCTL0, CCIFG);
}

void initADU() {
	
	//Zweitfunktion fuer Bit 0, 1 und 2 selektieren
	P6SEL = BIT0 + BIT1 + BIT2;
	
	//Modifizierbar
	ADC12CTL0 = 0;
	
	//Sample und Hold 256 Cycles
	//	ADC an
	//	Multiple sample and conversion
	//	Sample-and-hold time: 256 cycles
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

void initMMA() 
{
	//Auf Output schalten
	//P5DIR |= BIT4 + BIT5;
	
	//g-Select for 1.5g Range:
	//	P5.4 g-Select 2: LOW
	//	P5.5 g-select 1: LOW
	//deactivate sleep mode
	P5OUT &= ~(BIT4+BIT5);
	//	P5.6: HIGH
	P5OUT |= BIT6;
	
}

float convertAduValue(unsigned int value)
{
	// N_adc = 4095 * (Vin - Vr-) / (Vr+ - Vr-)
	// =>
	// Vr- = 0V, Vr+ = 3V
	// Vin = (N_adc * 3) / 4095
	
	// Result in milli volts
	return (value / 4095.0) * 3.0;
}

void printMMAValues()
{
	unsigned int ax, ay, az;
	float x, y, z;
	char buffer[32];
	
	while(ADC12CTL1 & ADC12BUSY);
	
	// P6.0 – ax
	ax = ADC12MEM0;
	x = convertAduValue(ax);
	
	// P6.1 – ay	
	ay = ADC12MEM1;
	y = convertAduValue(ay);
	
	// P6.2 – az 
	az = ADC12MEM2;
	z = convertAduValue(az);
	
	sprintf(buffer, "%f\t%f\t%f\r\n", x, y, z);
	uart1_put_str(buffer);
}

void initSecTimer() {
    // Divisor 8 for ACL Clock source
    SET(BCSCTL1, DIVA0);
    SET(BCSCTL1, DIVA1);
    
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
    TBCCR0 = 0x0028;
    
    // Starts timer ... Mode control 01b -> Up mode
    SET(TBCTL, MC0);
    CLEAR(TBCTL, MC1);
    
    // Enables interrupt
    SET(TBCCTL0, CCIE);
}

void Aufgabe24()
{
	initMMA();
	initADU();
	
	initSecTimer();

    // Interrupts global einschalten
    _bis_SR_register(GIE);
	
	while(true)
		if(printValues) {
			printValues = false;
			printMMAValues();
		}
}
