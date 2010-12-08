#include "msp430x16x.h"
#include "SHT11.h"
#include "system.h"
#include "stdio.h"
#include "string.h"
#include "math.h"

//=============================================================================
// Variablen
//=============================================================================
enum {TEMP, HUMI};

unsigned int humi_int;
unsigned int temp_int;
float humi_float;
float temp_float;
char humi_char[12];
char temp_char[15];
char p[] = "%";


//========================================================================= 
char SHT11_Write_Byte(unsigned char value)
//========================================================================= 
	{ 
  	unsigned char i,ack=0;  
  	for(i=0; i<8; i++)
  		{
    	if(value & BIT7) 	SHT11_DATA_HIGH;	// Datenbit 1 setzen
    	else            	SHT11_DATA_LOW;		// Datenbit 0 setzen
    	SHT11_SCK_HIGH;	SHT11_SCK_LOW;			// Taktsignal
    	value = value << 1;						// Value Wert links schieben
 		}
  	SHT11_DATA_IN;								// Datenleitung IN
  	SHT11_SCK_HIGH;	SHT11_SCK_LOW;				// Taktsignal
	ack = SHT11_DATA;                 	// Daten einlesen - ack Bit 
	return ack;		                    // Rückgabe ack=1 if no acknowledge
	}


//========================================================================= 
char SHT11_Read_Byte(unsigned char ack) 
//========================================================================= 
	{ 
  	unsigned char i,value=0;
  	SHT11_DATA_IN;
  	for(i=0; i<8; i++)					// 8 Datenbits lesen
  		{
    	value = value << 1;             // value links schieben 
    	SHT11_SCK_HIGH;			
    	if (SHT11_DATA) value++;        // wenn DATA=1 value um ein erhöhen
    	SHT11_SCK_LOW;
    	}
    if (ack == 0) SHT11_DATA_LOW;		// ack=0
    else SHT11_DATA_HIGH;				// ack=1
   	SHT11_DATA_OUT;                     // ack auf DATA
  	SHT11_SCK_HIGH; SHT11_SCK_LOW;		// Taktsignal						    
  	return value;						// Rückgabe gelesener Wert
	}


//========================================================================= 
void SHT11_TransStart(void)
//========================================================================= 
//       _____         ________
// DATA:      |_______|
//           ___     ___
// SCK : ___|   |___|   |______
	{
	SHT11_DATA_HIGH;
	SHT11_DATA_OUT;
	SHT11_SCK_LOW;                   
   	SHT11_SCK_HIGH;
   	SHT11_DATA_LOW;
   	SHT11_SCK_LOW; 
   	SHT11_SCK_HIGH;
  	SHT11_DATA_HIGH;		   
   	SHT11_SCK_LOW;		   
	}


//========================================================================= 
void SHT11_ConnectionReset(void)
//========================================================================= 
//       _____________________________________________________         ____
// DATA:                                                      |_______|
//          _    _    _    _    _    _    _    _    _        ___     ___
// SCK : __| |__| |__| |__| |__| |__| |__| |__| |__| |______|   |___|   |__
	{  
  	unsigned char i; 
  	SHT11_DATA_HIGH; SHT11_SCK_LOW;		// Anfangsbelegung   
  	  	for(i=0;i<9;i++)                  
  		{
  		SHT11_SCK_HIGH; SHT11_SCK_LOW;	// 9 Takte ausgeben
  		}
  	SHT11_TransStart();                 // es folgt Transmission start  
	}

//========================================================================= 
char SHT11_SoftReset(void)
//========================================================================= 
	{ 
  	unsigned char error=0;  
  	SHT11_ConnectionReset();            //reset communication
  	error |= SHT11_Write_Byte(SHT11_RESET);	//sendet RESET zum SHT11
  	return error;                     	//error=1 wenn SHT11 nicht reagiert
	}


//========================================================================= 
char SHT11_Read_StatusReg(unsigned char *p_value, unsigned char *p_checksum)
//========================================================================= 
	{ 
  	unsigned char error=0;
  	SHT11_TransStart();                     		// Transmission start
  	error |= SHT11_Write_Byte(SHT11_STATUS_REG_R); 	// Kommando senden
  	*p_value = SHT11_Read_Byte(SHT11_ACK);        	// Status Byte lesen
  	*p_checksum = SHT11_Read_Byte(SHT11_NO_ACK);   	// CRC lesen  
  	return error;                     				//error=1 bei Fehler
	}

//========================================================================= 
char SHT11_Write_StatusReg(unsigned char *p_value)
//========================================================================= 
	{ 
  	unsigned char error=0;	// schreibt Status Registerund CRC				
  	SHT11_TransStart();    		            //transmission start
  	error +=SHT11_Write_Byte(SHT11_STATUS_REG_W);	//send command to sensor
  	error +=SHT11_Write_Byte(*p_value);    	//send value of status register
  	return error;                     		//error>=1 in case of no response form the sensor
	}
			    							   
//========================================================================= 
char SHT11_Measure(unsigned char *p_value, unsigned char *p_checksum, unsigned char mode)
//========================================================================= 
	{ 
  	unsigned char error=0;
  	unsigned char ack;
  	unsigned int i;
	SHT11_TransStart();                   	// Transmission start
  	switch(mode)							// TEMP oder HUMI messen
  		{									// Kommando an SHT11 senden
    	case TEMP	: error+=SHT11_Write_Byte(SHT11_MEASURE_TEMP); break;
    	case HUMI	: error+=SHT11_Write_Byte(SHT11_MEASURE_HUMI); break;
    	default     : break;	 
  		}
  	for (i=0;i<65535;i++)
  		{
  		wait(100);			// 1msek warten - Zeit hängt von der f ab !!!!!
  		ack = SHT11_DATA;	// Wandlung kann zwischen 50..200mSek dauern
  		if(!ack) break;
		} 
 	if(ack) error+=1; 
	*(p_value+1) = SHT11_Read_Byte(SHT11_NO_ACK); 	// lesen MSB
	*(p_value)   = SHT11_Read_Byte(SHT11_ACK);    	// lesen LSB
  	return error;
	}

//========================================================================= 
void SHT11_Read_Sensor(void) { 

	// Variablen und Konstanten
	unsigned char error,checksum;
	
	// T = D1 + D2 * SO(T) Berechnungsgleichung für die Temperatur
	// SO(T) ist Wert aus temp_float
	const float D1=-39.6;			// für 14 Bit Werte
	const float	D2= 0.01;			// 
	float T;						// Temperatur
	
	// HL = C1 + C2 * SORH + C3 * SO(RH) * SO(RH)  <= relative Feuchte linear	 
	// SO(RH) ist Wert aus humi_float 
	const float C1=-4.0;            // für 12 Bit Werte
  	const float C2=+0.0405;         // 
  	const float C3=-0.0000028;      // 
  	float HL;						// relative Feuchtigkeit linear
  	
  	// HT = (T-25) * ( T1 + T2 * SO(RH) ) + HL	<= relative Feuchte kompensiert
  	const float T1=+0.01;           // for 12 Bit @ 3V
  	const float T2=+0.00008;     	// for 12 Bit @ 3V	
	float HT;						// relative Feuchtigkeit kompensiert
	
  	SHT11_ConnectionReset();
  	
  	error=0;
	
	//===== Feuchtigkeit
  	error+=SHT11_Measure((unsigned char*) &humi_int,&checksum,HUMI);  
  	
  	//==== Temperatur
  	error+=SHT11_Measure((unsigned char*) &temp_int,&checksum,TEMP);  

  	if(error!=0)
  		{
  		SHT11_ConnectionReset();
  		return;											// Messung fehlerhaft	
  		}                 								// Abbruch
  	else
  		{ 
	    humi_float=(float)humi_int;                   	// integer zu float
    	temp_float=(float)temp_int;                   	// integer zu float
		}
	
	//=====Berechnung der Temperatur  	
  	T = D1 + D2 * temp_float;							// Gleichung
  	sprintf(temp_char,"T=%-6.2f °C", T);				// float zu string
    //writestr(temp_char);
    
    //===== Berechnung der relativen Feuchtigkeit 
    HL = C1 + C2*humi_float + C3*humi_float*humi_float;	// Gleichung
    sprintf(humi_char,"HL=%5.2f %% ", HL);				// float zu string
									// % eintragen
    //writestr(humi_char);
    
    //===== Berechnung der relativen Feuchtigkeit temperaturkompensiert
    HT = (T-25) * (T1+T2*humi_float) + HL;				// Gleichung
    sprintf(humi_char,"HT=%5.2f %%", HT);				// float zu string
					// % eintragen
    //writestr(humi_char);
	} 


