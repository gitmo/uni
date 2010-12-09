#include "msp430x16x.h"
#include "system.h"
#include "interrupts.h"
#include "stdio.h"
#include "string.h"


//=============================================================================
void flash_read(unsigned int add, unsigned char* ptr, unsigned int len)
	{ 
	unsigned int l = 0;
  	while(l<len)					// Blocklänge für..
  		{							// ...lesen eines Flashwertes
    	ptr[l] = *(unsigned int*)(add + l);	
    	l++;
		}    
	}

//=============================================================================
void flash_write(unsigned int add, unsigned char* ptr, unsigned int len)
	{	
  	unsigned int i = 0; 
  	for(i=0;i<len;i+=2)				// Blocklänge für..
  		{							// ...schreiben eines Flashwertes	
    	flash_prog((char*)(add + i), (ptr[i]));
		}
	}

//=============================================================================
void flash_erase( int *Data_ptr )	// Adresse im gewünschten Segment
	{ 
	// DCO(SMCLK) mit 7.372800MHz / 17 ~ 434000 Hz
	// WDTCTL = WDTPW | WDTHOLD;	// Stop Watchdog
	_bic_SR_register(GIE);			// alle Interrupte sperren
	FCTL2 = FWKEY | FSSEL_3 | FN4;	// SMCLK Taktquelle, Teiler 16+1
	FCTL3 = FWKEY;               	// Lock = 0   
	//while(FCTL3 & 0x0001) nop();  // Warten auf BUSY = 0
	FCTL1 = FWKEY+ERASE;            // ERASE = 1 
	*Data_ptr=0;                  	// löschen Flash Segment
	FCTL1 = FWKEY;               	// ERASE = 0 
	FCTL3 = FWKEY+LOCK;            	// Lock = 1 
	_bis_SR_register(GIE);			// alle Interrupte erlauben
	// WDT_ARST_1000;				// Start Watchdog
	}

//=============================================================================
void flash_prog( char *Data_ptr, char word )
	{ 
	// DCO(SMCLK) is 7.372800MHz / 17 ~ 434000 Hz
	// WDTCTL = WDTPW | WDTHOLD;	// Stop Watchdog
	_bic_SR_register(GIE);			// alle Interrupte sperren
	FCTL2 = FWKEY | FSSEL_3 | FN4;	// SMCLK Taktquelle, Teiler 16+1
    FCTL3 = FWKEY;          	    // Lock = 0 
	//while(FCTL3 & 0x0001) nop(); 	// Warten auf BUSY = 0
    FCTL1 = FWKEY+WRT;              // WRT = 1 Schreibmode ein
    *Data_ptr=word;              	// Flash schreiben 
    FCTL1 = FWKEY;              	// WRT = 0 Schreibmode aus
    FCTL3 = FWKEY+LOCK;             // Lock = 1 
	_bis_SR_register(GIE);			// alle Interrupte erlauben
	// WDT_ARST_1000;				// Start Watchdog
	}

//=============================================================================
void writestr(char* str)				// Zeichenkettenausgabe RS232
	{				
	unsigned int j =0;					
	for (j=0;j < strlen(str);j++)		// Zeichenkettenlänge
		{		
		while((U1TCTL & TXEPT)== 0x00);	// warten bis Sendepuffer leer
		U1TXBUF = str[j];				// schreiben Sendepuffer
		}
	}

//=============================================================================
void writechar(char c)					// Zeichenausgabe RS232 
	{				
	while((U1TCTL & TXEPT)== 0x00); 	// warten bis Sendepuffer leer
	U1TXBUF = c;						// schreiben Sendepuffer
	}

//=============================================================================
void wait(unsigned int c)				// Wartet c * 10 mikrosekunde
	{				
	while (c > 0)
		{
		NOPS; NOPS; NOPS; NOPS; NOPS; NOPS; NOPS; _NOP(); c--;
		}
	}

//=============================================================================
unsigned int irand(unsigned int max)	// Random number between 0 and max-1
	{	
	return (int) abs(rand() % max);
	}
//=============================================================================
