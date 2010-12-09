#include "msp430x16x.h"
#include "CC1100.h"
#include "system.h"
#include "stdio.h"
#include "string.h"

//=============================================================================			  
// Rx Tx Buffer 
//=============================================================================
volatile  CC1100_Rx RxCC1100;
volatile  CC1100_Tx TxCC1100;
//=============================================================================
// Knoten ID und Funkkanal 
//=============================================================================
unsigned char ID = 1;
unsigned char channel = 8;

//=============================================================================			  
// PATABLE sollte überarbeitet werden
//=============================================================================
unsigned char paTableIndex = PATABLE;	// Current PATABLE Index
unsigned char paTable[] = {    0x00,		// -52 dBm	0
							   0x23, 		// -15 dBm	1
							   0x33, 		// -12 dBm	2
							   0x34, 		// -10 dBm	3
							   0x28, 		// -8 dBm	4
							   0x2B, 		// -6 dBm	5
							   0x57, 		// -4 dBm	6
							   0x54, 		// -2 dBm	7
							   0x3F, 		// 0 dBm	8
							   0x8C, 		// +2 dBm	9
							   0x8A, 		// +3 dBm	10
							   0x87, 		// +4 dBm	11
							   0x84, 		// +5 dBm	12
							   0xCE, 		// +6 dBm	13
							   0xCC, 		// +7 dBm	14
							   0xC9, 		// +8 dBm	15
							   0xC6, 		// +9 dBm	16
							   0xC3  		// +10 dBm	17
							};
//=============================================================================	
						
							
//=============================================================================			  
// Function switchchannel - 9 fest definierte Kanalfrequenzen
//=============================================================================
unsigned char FREQ2[]={0x21,0x21,0x21,0x21,0x21,0x21,0x21,0x21,0x21}; 
unsigned char FREQ1[]={0x71,0x6E,0x6B,0x68,0x65,0x41,0x3F,0x3C,0x39};
unsigned char FREQ0[]={0x7A,0x85,0x91,0x9D,0xA9,0xF8,0x03,0x0F,0x1B};
//Index		 frequency	 	Arbeitsplatz
//channel=0, f=869.525MHz  	Notrufkanal    
//channel=1, f=869.225MHz  	HWP1  
//channel=2, f=868.925MHz  	HWP2 
//channel=3, f=868.625MHz  	HWP3 
//channel=4, f=868.325MHz  	HWP4  
//channel=5, f=864.700MHz	HWP5 	 
//channel=6, f=864.400MHz   HWP6
//channel=7, f=864.100MHz  	HWP7
//channel=8, f=863.800MHz  	HWP8 
							
//=============================================================================			  
// 400 KBit/s, 869.525 MHz, MSK, Quartz: 26 MHz, Addresse=255
//=============================================================================
char conf[39];
char conf[] = {
  0x06, // IOCFG2	GDO2 Signal Konfigurierung Table 34 Packet,CRC,FIFO
  0x2E, // IOCFG1	GDO1 Signal Konfigurierung Table 34 Tristate
  0x0E, // IOCFG0	GDO0 Signal Konfigurierung Table 34 Carrier Sense	
  0x0F, // FIFOTHR	Bytes in FIFO
  0x9B, // SYNC1	Sync Word HighByte 0x9B
  0xAD, // SYNC0	Sync Word Low Byte 0xAD
  0x3F, // PKTLEN	Packetlänge = 63 bei variabler Packetlänge 
  0x06, // PKTCTRL1	2 Status Bytes anfügen; Adresscheck ON Broadcastadresse 0
  0x45, // PKTCTRL0	variable Packetlänge ON; whitening ON,  
  0xFF, // ADDR		Addresse für Packetfilterung (Knotenadresse)
  0x00, // CHANNR	
  0x0B, // FSCTRL1	
  0x00, // FSCTRL0
  0x21, // FREQ2	Frequenz Control Word High Byte 
  0x71, // FREQ1	Frequenz Control Word Middle Byte 
  0x7A, // FREQ0	Frequenz Control Word Low Byte
  0x2D, // MDMCFG4
  0xF8, // MDMCFG3
  0x73, // MDMCFG2	Modulationsformat MSK
  0x42, // MDMCFG1	8 Präambel Bytes, 
  0xF8, // MDMCFG0
  0x00, // DEVIATN
  0x07, // MCSM2 	(RX_TIME = until end of packet)
  0x03, // MCSM1 	(TX_OFFMODE = RX)
  0x18, // MCSM0	(autom. Frequenzkalibrierung bei IDLE->Tx und IDLE->Rx ON)
  0x1D, // FOCCFG
  0x1C, // BSCFG
  0xC0, // AGCCTRL2	AGC Control Gain 
  0x49, // AGCCTRL1	AGC Control Carrier Sense Level
  0xB2, // AGCCTRL0	AGC Control 
  0x87, // WOREVT1
  0x6B, // WOREVT0
  0xF8, // WORCTRL	WOR Control 
  0xB6, // FREND1	Front End Rx 
  0x10, // FREND0	Front End TX - PA Power settings
  0xEA, // FSCAL3
  0x2A, // FSCAL2
  0x00, // FSCAL1
  0x1F  // FSCAL0
};
//=============================================================================			  


//=============================================================================				  
// Funktion wartet bis die SPI des CC1100 verfügbar ist
//=============================================================================
void spiInitTrx(void)	// schaltet P3.2 auf Input und CS=0 und wartet bis
	{					// GDO1=0(SPI CC1100 bereit), dann P3.2 in den SPI Mode
	P3SEL &= ~0x04;			// P3.2 in Port Mode
	P3DIR &= ~0x04;			// P3.2 Input
	CC1100_CS_LOW;			// CS to low
	while (CC1100_GDO1);	// warten bist GDO1 0 
	P3SEL |= 0x04;			// P3.2 in SPI mode
	}
//=============================================================================
// Funktion sendet und empfängt ein Byte über die SPI des MSP430
//=============================================================================
unsigned char trx_spi(unsigned char data)
	{		
	IFG1 &= ~URXIFG0;			// zurücksetzen des URXIFG0 Flags
	while(!(IFG1 & UTXIFG0));	// warten bis TX Buffer leer ist
	TXBUF0 = data;				// ein Byte senden
	while(!(IFG1 & URXIFG0));	// warten auf empfangenes Byte
	return RXBUF0;				// Rückgabe des empfangenen Byte
	}
//=============================================================================
// Funktion zum Schreiben eines einzelnen CC1100 Registers
//=============================================================================
void spiWriteReg(unsigned char addr, unsigned char value)
	{	
	spiInitTrx();				// Init SPI CS = 0 warten bis bereit
	trx_spi(addr);				// Adresse schreiben
	trx_spi(value);				// Wert schreiben
	CC1100_CS_HIGH;				// CS = 1
	}
//=============================================================================
// Funktion zum Lesen eines einzelnen CC1100 Registers
//=============================================================================
unsigned char spiReadReg(unsigned char addr)
	{
	unsigned char x;			// Variable Rückgabewert
	spiInitTrx();				// Init SPI CS = 0 warten bis bereit
	trx_spi(addr | CC1100_READ_SINGLE); // Kommando schreiben
	x = trx_spi(NOBYTE);		// Wert lesen
	CC1100_CS_HIGH;				// CS = 1
	return x;
	}
//=============================================================================
// Funktion zum Schreiben des Strobe Kommandos 
//=============================================================================
void spiStrobe(unsigned char strobe)
	{
	spiInitTrx();				// Init SPI CS = 0 warten bis bereit
	trx_spi(strobe);
	CC1100_CS_HIGH;
	}
//=============================================================================
// Funktion zum Schreiben der Register im Burst
//=============================================================================
void spiWriteBurstReg(unsigned char addr, char *buffer, unsigned char count)
	{
	unsigned char i;					// Variable
	spiInitTrx();				// Init SPI CS = 0 warten bis bereit
	trx_spi(addr | CC1100_WRITE_BURST);
	for (i = 0; i < count; i++) {
		trx_spi(buffer[i]);
		}
	CC1100_CS_HIGH;
	}

//=============================================================================
// Funktion zum Lesen der Register des CC1100 im Burst
//=============================================================================
void spiReadBurstReg(unsigned char addr, char *buffer, unsigned char count)
	{
	unsigned char i;			// Variable
	spiInitTrx();				// Init SPI CS = 0 warten bis bereit
	trx_spi(addr | CC1100_READ_BURST);
	for (i = 0; i < count; i++) {
		buffer[i] = trx_spi(NOBYTE);
		}
	CC1100_CS_HIGH;
	}
//=============================================================================
// Funktionen zum Lesen der Status Register des CC1100
//=============================================================================
unsigned char spiReadStatus(unsigned char addr)
	{
	unsigned char x;	// Variable
	spiInitTrx();		// Init SPI CS = 0 warten bis bereit
	trx_spi(addr | CC1100_READ_BURST);
	x = trx_spi(NOBYTE);
	CC1100_CS_HIGH;
	return x;			// Chip Status Byte - Tabelle 17 im CC1100 Data Sheet
	}
unsigned char spiGetRxStatus(void)
	{
	unsigned char x;	// Variable
	spiInitTrx();		// Init SPI CS = 0 warten bis bereit
	// Strobe SNOP with the read byte set to get info on
	// current state and number of bits in the RX FIFO
	x = trx_spi(CC1100_SNOP | CC1100_READ_SINGLE);
	CC1100_CS_HIGH;		// CS = 1
	return x;			// Chip Status Byte - Tabelle 17 im CC1100 Data Sheet
	}
unsigned char spiGetTxStatus(void)
	{
	unsigned char x;	// Variable
	spiInitTrx();		// Init SPI CS = 0 warten bis bereit
	x = trx_spi(CC1100_SNOP);	// Strobe Kommando No Operation Zustand RXFIFO
	CC1100_CS_HIGH;		// CS = 1
	return x;			// Chip Status Byte - Tabelle 17 im CC1100 Data Sheet
	}
//=============================================================================
// Funktion RESET des CC1100  
//=============================================================================
void reset(void)
	{
	spiInitTrx();				// Init SPI CS = 0 warten bis bereit
	trx_spi(CC1100_SRES);		// Strobe Kommando Reset
	spiInitTrx();				// Init SPI CS = 0 warten bis bereit
	CC1100_CS_HIGH;				// CS = 1
	}
//=============================================================================
//	Funktion RESET des CC1100 nach power_on und warten bis bereit
//=============================================================================
void powerUpReset(void)
	{
	CC1100_CS_HIGH;
	NOPS;
	CC1100_CS_LOW;
	NOPS;
	CC1100_CS_HIGH;
	wait(4);
	reset();
	}
//=============================================================================
// Funktion setzt den Funkkanal (im Bereich 0...8)
//=============================================================================
void switchFreq(unsigned char  c)
	{
	spiStrobe(CC1100_SIDLE);
	spiWriteReg(CC1100_FREQ2,(unsigned char)FREQ2[c]);
	spiWriteReg(CC1100_FREQ1,(unsigned char)FREQ1[c]);
	spiWriteReg(CC1100_FREQ0,(unsigned char)FREQ0[c]);
	spiStrobe(CC1100_SRX);
	spiStrobe(CC1100_SCAL);
	}
//=============================================================================
// Funktion initalisiert CC1100 und setzt CC1100 in den RX Mode
//=============================================================================
void initCC1100(void)
	{
	// Power up Reset CC1100
	powerUpReset();
	// Konfigurationsregister schreiben
	spiWriteBurstReg(0x00, conf, sizeof(conf));
	// aktuelle Sendeleistung des CC1100 setzen
	spiWriteReg(CC1100_PATABLE, paTable[paTableIndex]);
	// Initialisieren der RSSI und CRC Werte im RxCC1100 Empfangspuffer
	RxCC1100.RSSI = 0x00;
	RxCC1100.CRC = false;
	// Enable interrupts on P2.0 for packet reception (CC1100 GDO2)
	_bic_SR_register(GIE);	// Disable all interrupts
	P2SEL = 0x00;			// must be <> 1 to use interrupts
	SET(P2IES, 0x01);		// Enables external interrupt on low edge (for GDO2)
	SET(P2IE, 0x01);		// Enable interrupt P2.0 
    CLEAR(P2IFG, 0x01);		// Clears the interrupt flag
	_bis_SR_register(GIE);	// Enable all Interrupts 
	spiStrobe(CC1100_SRX);	// Rx Mode
	wait(12);
	}
	
//=============================================================================
// Funktion initalisiert CC1100 und setzt CC1100 in den Idle Mode
//=============================================================================
void initCC1100_IDLE(void)
	{
	// Power up Reset CC1100
	powerUpReset();
	// Konfigurationsregister schreiben
	spiWriteBurstReg(0x00, conf, sizeof(conf));
	// aktuelle Sendeleistung des CC1100 setzen
	spiWriteReg(CC1100_PATABLE, paTable[paTableIndex]);
	// Initialisieren der RSSI und CRC Werte im RxCC1100 Empfangspuffer
	RxCC1100.RSSI = 0x00;
	RxCC1100.CRC = false;
	spiStrobe(CC1100_SIDLE);	// in den IDLE Mode setzen
	wait(12);
	}
//=============================================================================
// Funktion setzt CC1100 in PowerDown
//=============================================================================
void initCC1100_POWERDOWN(void)
	{
	spiStrobe(CC1100_SPWD);		// in den PowerDown Mode setzen
	wait(12);
	}	
		
//=============================================================================
// Funktion setzt die Adresse des CC1100 (ist im Bereich von 0...255 möglich)
//=============================================================================
void setUid(unsigned char id) 
	{
	if (id > MAX_UID || id < MIN_UID) return; // falsche Adresse abfangen
	spiStrobe(CC1100_SIDLE);				  // in den IDLE Mode setzen
	spiWriteReg(CC1100_ADDR, id);			  // Adressregister schreiben
	spiStrobe(CC1100_SRX);					  // in den RX Mode schalten	
	wait(12);								  // 12µSek warten
	}
//=============================================================================
// Funktion setzt die Sendeleistung des CC1100 siehe auch paTable 
//=============================================================================
char setOutputPower(unsigned char paIdx)
	{
	if (paIdx < 18)
		{
		spiStrobe(CC1100_SIDLE);	// CC1100 in den IDLE Mode setzen
		paTableIndex = paIdx;		// PA Index setzen
		spiWriteReg(CC1100_PATABLE, paTable[paTableIndex]); // PA Wert schreiben
		spiStrobe(CC1100_SRX);		// CC1100 in den RX Mode setzen
		wait(12);					// warten 
		return true;				// Rückgabe true 
		}
	return false;					// Rückgabe false da paIdx zu groß
	}

//=============================================================================
//  void sendPacket(unsigned char ziel,unsigned char quelle,char *data,unsigned char length)
//
//  Funktion:
//      Sendet Packete mit Längen bis zu 62Byte.
//      GDO2 muß so konfiguriert werden das es gesetzt wird wenn das sync word
//		gesendet ist und und rückgesetzt wird wenn das packet gesendet ist.
//		Dafür erforderlich ist spiWriteReg(CC1100_IOCFG0, 0x06);
//      Das GDO2 Bit wird in der Funktion gepollt.
//      
//  Parameter:
//		unsigned char ziel
//			Zieladresse des Knotens an die das Packet gesendet werden soll (0..255) 
//		unsigned char quelle
//			Zieladresse des Knotens an die das Packet gesendet werden soll (0..255) 
//     	char *data
//          Pointer auf einen Buffer der die Sendedaten enthält.
//      unsigned char length
//          Länge des Buffers mit den Sendedaten (length <= 60)
//=============================================================================

void sendPacket(unsigned char ziel,unsigned char quelle,  char *data, unsigned char length)
	{
	//	| 0 | 1 | 2 | 3  |....| 62 | data[3..62] = 59 
	//	  L   Z   Q   D1  ....  D59 => 62 Byte
	// 	L_Länge,Z_Ziel;Q_Quelle,D_Datenbyte
	unsigned char i;
	if (length > MAX_DATA_LENGTH-1 )	// max 59 Byte  
		{
		length = MAX_DATA_LENGTH-1 ;	// zu große Packete werden auf 59 Byte begrenzt 
		}
	// Packetlänge = 1 Byte (Zieladdresse) + data length
	TxCC1100.length = 2 + length;
	// Zieladresse eintragen
	TxCC1100.dest = ziel;
	TxCC1100.source = quelle;
	
	// Quelladresse eintragen
	//Sendepuffer füllen
	for (i = 0; i < length; i++)
		{
		TxCC1100.data[i] = data[i];
		}
	// Interrupt P2.0 verbieten
	CLEAR(P2IE, 0x01);
	// setzt CC1100 in den IDLE Mode
	spiStrobe(CC1100_SIDLE);
    // löscht den TX FIFO des CC1100
    spiStrobe(CC1100_SFTX);
	// Packet in TX FIFO schreiben +Länge+Ziel+Quelle
	spiWriteBurstReg(CC1100_TXFIFO, (char *) &TxCC1100, length+3);
	// setzt CC1100 in den Tx Mode
	spiStrobe(CC1100_STX);
	// warten GDO2=1 - sync transmitted
	while (!CC1100_GDO2);
	// warten GDO2=0 - Packet Ende
	while (CC1100_GDO2);
	// Interrupt P2.0 erlauben
	CLEAR(P2IFG, 0x01);
	SET(P2IE, 0x01);
	}


//=============================================================================
//  bool receivePacket()
//
//  Funktion:
//      Die Funktion empfängt ein Packet variabler Länge.
//		Das erste Byte im Packet ist die Länge des Packetes.
// 		Um diese Funktion zu nutzen muß der APPEND_STATUS im PKTCTRL1 Register
//		gesetzt werden. RSSI und CRC des Packetes werden beim Lesen angehangen.
//      Die Funktion wird durch die ISR für P2.0 aufgerufen.
//		Ist getriggert durch das GDO2 Signal des CC1100 (an P2.0)
//		für den Empfang eines Packetes. Siehe auch Register IOCFG2. 
//      Das RXBYTES Register zeigt ob Bytes im FIFO stehen.
// 		Ist notwendig da das GDO Signal auch signalisier wenn:
//			***ein synch 
//			***Rx FIFO is flushed due to address filtering
//			***CRC	filtering
//			***packet length filtering.
//
//		Packet wird in RxCC1100 Buffer geschrieben
//   
//  Parameter:
//		keine
//          
//  Rückgabewert:
//      bool
//          true:   CRC OK
//          false:  CRC NOT OK or
//					storage buffer too short or
//					no packet was put in the RX FIFO due to filtering
//=============================================================================
char receivePacket()
	{
	// für RSSI und CRC Status vorbereiten
	char status[2];
	// mit 0 initialisieren um aktuelle Länge zu speichern
	unsigned char packetLength = 0;
	// Wenn Bytes im RX FIFO vorhanden sind dann...
	if ((spiReadStatus(CC1100_RXBYTES) & BYTES_IN_RXFIFO))
		{
		// Längenbyte des aktuellen Packetes aus dem RX FIFO lesen (erstes Byte)
        packetLength = spiReadReg(CC1100_RXFIFO); //Das erste Byte ist LängenByte
		// Wenn Packetlänge OK dann...
        if (packetLength <= PACKET_LENGTH)
			{
			// Längenbyte in den RxCC1100 Puffer schreiben
			RxCC1100.length = packetLength;// packetLength;
			// Den Rest des Packetes in RxCC1100 mit aktueller Länge schreiben
            spiReadBurstReg(CC1100_RXFIFO,(char *)RxCC1100.data, packetLength);
            // Lesen der zwei Status Bytes (status[0] = RSSI, status[1] = LQI)
            spiReadBurstReg(CC1100_RXFIFO, status, 2);
			// RSSI Werte in den RxCC1100 Puffer schreiben
			RxCC1100.RSSI = status[I_RSSI];
			// CRC Wert in den RxCC1100 Puffer schreiben 
			RxCC1100.CRC = (status[I_LQI] & CRC_OK) >> 7;
			// Zieladresse in den RxCC1100 Puffer schreiben
			RxCC1100.dest = RxCC1100.data[0]; 
			// Quelladresse in den RxCC1100 Puffer schreiben
			RxCC1100.source = RxCC1100.data[1];
			// Rückgabewert CRC true or false
			return RxCC1100.CRC;
        	}
    	//...sonst...
		else
			{
			// ...CC1100 in den IDLE Mode setzen um... 
            spiStrobe(CC1100_SIDLE);
            // ...den RX FIFO zu löschen...
            spiStrobe(CC1100_SFRX);
			// ...und Wert für Rückgabe ist false
            return false;
        	}
		}
	// ...sonst da keine Bytes im RX FIFO	
	else
		{
		// ...Rückgabe false 
		return false;
		}
	}
//=============================================================================
//  printPacket()
//
//  Funktion:
//      Die Funktion gibt empfangenes Datenpaket auf der seriellen 
//		Schnittstelle aus.      
// 
//=============================================================================	
void printPacket(void)
	{
	char i;
	char text[20];
	char z[] = "\n\r";
	sprintf(text,"\n\rPaketlänge = %u", RxCC1100.length);
	writestr(text);	
	sprintf(text,"\n\rZieladresse = %u", RxCC1100.dest);
	writestr(text);
	sprintf(text,"\n\rQuelladresse= %u", RxCC1100.source);
	writestr(text);
	sprintf(text,"\n\rRSSI = %u", RxCC1100.RSSI);
	writestr(text);
	writestr(z);	
	writestr("\n\rDaten = ");
	for (i=2;i<((RxCC1100.length));i++)
		{
		writechar(RxCC1100.data[i]);
		}
	writestr(z);
	}

