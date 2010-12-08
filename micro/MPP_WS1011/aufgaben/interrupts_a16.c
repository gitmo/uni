#include "msp430x16x.h"
#include "interrupts.h"
#include "system.h"
#include "CC1100.h"
#include "stdio.h"
#include "string.h"

#include "project.h"
#include "aufgaben/aufgabe15.h"
	
//Interrupte folgendermaßen im Code einbinden:
//
// 		#pragma vector = TIMERA0_VECTOR
//		__interrupt void TimerA0 (void)
// 		{
//    	...code
// 		}

//
//	Tabelle für die möglichen Interruptquellen zum Einsetzen in 
//	#pragma vector = ...

//=======================================================================
//  #pragma vector =...				 INTNR  |  ADR   | Funktion			|
//=======================================================================
//RESET_VECTOR						//int31 | 0xFFFE | Reset 			|
//NMI_VECTOR						//int30 | 0xFFFC | Non-maskable		|
//=======================================================================
//TIMERB0_VECTOR          			//int29 | 0xFFFA | Timer B CC0		|
//TIMERB1_VECTOR          			//int28 | 0xFFF8 | Timer B CC1-6, TB|
//COMPARATORA_VECTOR				//int27 | 0xFFF6 | Comparator A 	|
//WDT_VECTOR              			//int26 | 0xFFF4 | Watchdog Timer 	|
//TIMERA0_VECTOR					//int25 | 0xFFF2 | Timer A CC0		|
//TIMERA1_VECTOR		            //int24 | 0xFFF0 | Timer A CC1-2	|
//USCIAB0RX_VECTOR					//int23 | 0xFFEE | USART0 Receive	|
//USCIAB0TX_VECTOR					//int22 | 0xFFEC | USART0 Transmit	|
//ADC12_VECTOR			           	//int21 | 0xFFEA | ADC				|
//RESERVED20_VECTOR				    //int20 | 0xFFE8 | ???		 		|
//PORT2_VECTOR 				        //int19 | 0xFFE6 | Port2			|
//PORT1_VECTOR 			            //int18 | 0xFFE4 | Port1 			|
//USCIAB1RX_VECTOR					//int17 | 0xFFE2 | USART1 Receive	|
//USCIAB1TX_VECTOR					//int16 | 0xFFE0 | USART1 Transmit 	|
//DMA_VECTOR		             	//int15 | 0xFFDE | DMA				|
//DAC12_VECTOR                    	//int14 | 0xFFDC | DAC12			|
//=======================================================================
//reservierter Bereich			//int13-0	| 0xFFDA...0xFFC0			|
//=======================================================================


//==============================================================
// Implementierung der ISR Routinen 
// Achtung! Code in der ISR nicht zu lang machen,
// da sonst Timing oder Stack Probleme drohen.
// Für die benötigten ISR die Auskommentierungen entfernen
// und eigenen Code für die jeweilige ISR einfügen
//==============================================================


#pragma vector = PORT1_VECTOR 
__interrupt void PORT1 (void)
{
    if( powerState == LPM_4 ) {
        // Flanke auf 1 -> Interrupt beim loslassen
        // für die invertierten Taster entspricht dies einer LF
        SET(P1IES, TASTE0);
        powerState = ACTIVE;
        LPM4_EXIT;
    } else if ( powerState == ACTIVE ) {
        // Flanke auf 0 -> Interrupt beim drücken (statt loslassen)
        // für die invertierten Taster entspricht dies einer HF
        CLEAR(P1IES, TASTE0);
        powerState = LPM_4;
    }
    
    //Interrupt Flag ausschalten
    CLEAR(P1IFG, 0xFF);
}

		
//==============================================================
//===INT:01====ADR:FFE2====PORT2================================
//==============================================================
#pragma vector = PORT2_VECTOR
__interrupt void PORT2 (void) {
	char res ;			// CRC Check 
	if (P2IFG & 0x01)	// Check P2IFG Bit P2.0 - CC1100 Rx Packet
		{ 
		CLEAR(P2IFG, 0x01);
		LEDTOGGLE;
		res = receivePacket(); 	// CRC Rückgabe 
		if (res)				// wenn Packet OK ...
			{
			printPacket();		// Packet auf Terminal ausgeben
			}
		else
			{	
			spiStrobe(CC1100_SIDLE); 	// Switch to IDLE
			spiStrobe(CC1100_SFRX);	 	// Flush the RX FIFO
			}
		}	
	else
		{
		CLEAR(P2IFG, 0xFF);			 	// Clear all flags
		}
	spiStrobe(CC1100_SRX);			 	// Switch to RX Mode
	}
//==============================================================
