#include "msp430x16x.h"
#include "init.h"

//=========================================================================
void initPort () {
//====PORT1================================================================
WDTCTL = WDTPW | WDTHOLD;   // Watchdog ausschalten
P1SEL = 0x00;       // Port1 Zweitfunktion
P1OUT = 0x00;       // Port1 Ausgangsregister: 00000000 = 0x00 
P1DIR = 0xFC;       // Port1 Direction: 11111100 = 0xFC
                    //   0 - P1.0 [EINGANG] - JP1_9 Taster gb
                    //   0 - P1.1 [EINGANG] - JP1_8 Taster sw
                    //   1 - P1.2 [AUSGANG] - JP1_7
                    //   1 - P1.3 [AUSGANG] - JP1_6
                    //   1 - P1.4 [AUSGANG] - JP1_5
                    //   1 - P1.5 [AUSGANG] - JP1_4
                    //   1 - P1.6 [AUSGANG] - JP1_3
                    //   1 - P1.7 [AUSGANG] - JP1_2
//====PORT2================================================================
P2SEL = 0x00;       // Port2 Zweitfunktion
P2OUT = 0x00;       // Port2 Ausgangsregister: 00000000 = 0x00
P2DIR = 0x1C;       // Port2 Direction: 00011100 = 0x1C
                    //   0 - P2.0 [EINGANG] - CC1100_GDO2
                    //   0 - P2.1 [EINGANG] - CC1100_GDO0
                    //   1 - P2.2 [AUSGANG] - ungenutzt
                    //   1 - P2.3 [AUSGANG] - ungenutzt
                    //   1 - P2.4 [AUSGANG] - ungenutzt
                    //   0 - P2.5 [EINGANG] - ROSC 
                    //   0 - P2.6 [EINGANG] - SD-KARTE Protect
                    //   0 - P2.7 [EINGANG] - SD-KARTE Detect
//====PORT3================================================================
P3SEL = 0xC0;       // Port3 Zweitfunktion
P3OUT = 0x09;       // Port3 Ausgangsregister: 00001001 = 0x09
P3DIR = 0x6B;       // Port3 Direction: 00101011 = 0x2B
                    //   1 - P3.0 [AUSGANG] - CC1100_CSN
                    //   1 - P3.1 [AUSGANG] - CC1100_SI
                    //   0 - P3.2 [EINGANG] - CC1100_SO
                    //   1 - P3.3 [AUSGANG] - CC1100_SCLK
                    //   0 - P3.4 [EINGANG] - SHT 11 DATA (IN/OUT)
                    //   1 - P3.5 [AUSGANG] - SHT 11 CLK
                    //   1 - P3.6 [2-Funktion] - RS232_RxD JP2_8
                    //   0 - P3.7 [2-Funktion] - RS232_TxD JP2_7
//====PORT4================================================================
P4SEL = 0x00;       // Port4 Zweitfunktion
P4OUT = 0xFF;       // Port4 Ausgangsregister: 00000000 = 0x00
P4DIR = 0xFF;       // Port4 Direction: 11111111 = 0xFF
                    //   1 - P4.0 [AUSGANG] - JP2_15 LED rt
                    //   1 - P4.1 [AUSGANG] - JP1_16 LED gb
                    //   1 - P4.2 [AUSGANG] - JP1_15 LED gr
                    //   1 - P4.3 [AUSGANG] - JP1_14 
                    //   1 - P4.4 [AUSGANG] - JP1_13 
                    //   1 - P4.5 [AUSGANG] - JP1_12 
                    //   1 - P4.6 [AUSGANG] - JP1_11
                    //   1 - P4.7 [AUSGANG] - JP1_10
//===PORT5=================================================================
P5SEL = 0x00;       // Port5 Zweitfunktion: 00000000 = 0x00
P5OUT = 0x89;       // Port5 Ausgangsregister: 10001001 = 0x89
P5DIR = 0xFB;       // Port5 Direction: 11111011 = 0xFB
                    //   1 - P5.0 [AUSGANG] - SD-KARTE /CS
                    //   1 - P5.1 [AUSGANG] - SD-KARTE DI
                    //   0 - P5.2 [EINGANG] - SD-KARTE DO
                    //   1 - P5.3 [AUSGANG] - SD-KARTE DCLK
                    //   1 - P5.4 [AUSGANG] - MMA GS1 JP2_8
                    //   1 - P5.5 [AUSGANG] - MMA GS2 JP2_9
                    //   1 - P5.6 [AUSGANG] - MMA /SLEEP
                    //   1 - P5.7 [AUSGANG] - LED_ROT 0-an, 1-aus
//====PORT6================================================================
P6SEL = 0x00;       // Port6 Zweitfunktion = 0x07
P6OUT = 0x00;       // Port6 Ausgangsregister: 00000000 = 0x00
P6DIR = 0xC8;       // Port6 Direction: 11111111 = 0xFF
                    //   0 - P6.0 [AD-EINGANG] - MMA X-Achse
                    //   0 - P6.1 [AD-EINGANG] - MMA Y-Achse
                    //   0 - P6.2 [AD-EINGANG] - MMA Z-Achse
                    //   1 - P6.3 [AUSGANG] - ungenutzt
                    //   1 - P6.4 [EINGANG] - ADC4 JP2_5
                    //   1 - P6.5 [EINGANG] - ADC5 JP2_4
                    //   1 - P6.6 [AUSGANG] - DAC6 JP2_3
                    //   1 - P6.7 [AUSGANG] - DAC7 JP2_2 
}


//=========================================================================
void initUART0_SPI(void)
    {
    while(!(UTCTL0 & TXEPT));   // Sendebuffer leer?
    // Interrupt Enable Register
    IE1 &= ~(URXIE0 + UTXIE0);  // Tx und Rx Interrupt verbieten
    // Module Enable Register
    ME1 &= ~(UTXE0 + URXE0);    // Tx und Rx freigeben
    // Zuordnung der SPI Zweitfunktion auf die Portleitungen
    P3SEL |= 0x0E;              // Zweitfunktion SPI freigeben
                                // P3.1 [AUSGANG] - CC1100_SI
                                // P3.2 [EINGANG] - CC1100_SO
                                // P3.3 [AUSGANG] - CC1100_SCLK
    // USART Control Register
    UCTL0  =    SWRST;          // Software Reset = 1
    UCTL0 |=    CHAR            // 8 Daten Bit
            +   SYNC            // SPI Mode
            +   SWRST           // Software Reset = 1
            +   MM;             // USART ist Master
    // USART Transmit Control Register
    UTCTL0 =    CKPL            // Datenübernahme mit LH Flanke
            +   SSEL1           // Taktquelle SMCLK
            +   SSEL0           // Taktquelle SMCLK
            +   STC;            // 3 Leitungsmode der SPI
    // USART Receive Control Register
    URCTL0 = 0x00;              //      
    // USART Baudrate Register 0
    UBR00 = 0x02;               // Taktquelle=SMCLK/2, SMCLK = 7,3728 MHz
    // USART Baudrate Register 1
    UBR10 = 0x00;               
    // USART Modulation Control Register
    UMCTL0 = 0x00;
    // USART Modul Enable Register
    ME1 |= USPIE0;         // Enable USART0 TXD/RXD
    // USART Control Register
    UCTL0  &= ~SWRST;      // Reset Bit löschen 
    }
    
    
//=========================================================================
void initUART1(void)
    {
//  P3SEL .....;            // USART RX und TX dem Modul zuweisen
//  U1CTL .....;            // Reset
//  U1CTL .....;            // Format 8N1
//  U1TCTL.....;            // Taktquelle SMCLK
//  U1BR0 .....;            // Teiler Low-Teil, da 7372800/64 ca. 115200
//  U1BR1 .....;            // Teiler High-Teil
//  U1MCTL ....;            // Modulationskontrolle
//  ME2 .......;            // Enable USART1 TXD/RXD
//  U1CTL .....;            // Reset
//  IE2 .......;            // Enable Interrupt
    }
    
    
//=========================================================================
void DCO (void)             // f(DCO) = DELTA*(32768Hz) = 7372800Hz
    {                       // ROSC P2SEL = 0x20 beachten
    #define DELTA (225)     // DELTA * 32768Hz = 7372800Hz
    unsigned int Compare, Oldcapture = 0;   
    WDTCTL = WDTPW + WDTHOLD;   // Stop WDT
    BCSCTL2 |= DCOR;        // ROSC (extern) einschalten
    BCSCTL2 &= ~SELM1;      // DCO als Taktquelle für MCLK     
    BCSCTL2 &= ~SELS;       // DCO als Taktquelle für SMCLK    
    BCSCTL1 |= XT2OFF;      // XT2 ausschalten 
    CCTL2 = CM_1 + CCIS_1 + CAP;        // CAP, ACLK
    TACTL = TASSEL_2 + MC_2 + TACLR;    // SMCLK, cont-mode, clear
    while (1)
        {
        while (!(CCIFG & CCTL2));       // Wait until capture occured
        CCTL2 &= ~CCIFG;                // Capture occured, clear flag
        Compare = CCR2;                 // Get current captured SMCLK
        Compare = Compare - Oldcapture; // SMCLK difference
        Oldcapture = CCR2;              // Save current captured SMCLK
        if (DELTA == Compare) break;    // If equal, leave "while(1)"
        else if (DELTA < Compare)       // DCO zu schnell 
            {         
            DCOCTL--;                   // verlangsamen
            if (DCOCTL == 0xFF)         //  
                {                       // Did DCO roll under?, Sel lower RSEL
                if (!(BCSCTL1 == (XT2OFF))) BCSCTL1--; //schaltet XT2 aus
                }
            }
        else 
            {
            DCOCTL++;
            if (DCOCTL == 0x00)
                {                       // Did DCO roll over? Sel higher RSEL
                if (!(BCSCTL1 == (XT2OFF + 0x07))) BCSCTL1++;              
                }
            }
        }
    CCTL2 = 0;                          // Stop CCR2
    TACTL = 0;                          // Stop Timer_A
    TBCCTL0 &= ~CCIFG;                  // Clears compare IFG
    TBR=0;
    }   
    
    
//============================================================================
void XT2 (void)                     // Funktion schaltet XT2 ein
    {                               // und setzt XT2 als Taktquelle
    volatile unsigned int i;
    WDTCTL = WDTPW + WDTHOLD;       // Stop WDT
    BCSCTL1 &= ~XT2OFF;             // XT2 = HF XTAL einschalten
    do {
        IFG1 &= ~OFIFG;             // Clear OSCFault flag
        for (i = 0xFF; i > 0; i--); // Time for flag to set
    }
    while ((IFG1 & OFIFG) != 0);    // OSCFault flag still set?                
    BCSCTL2 |= SELM1 + SELS;        // MCLK und SMCLK = XT2 (7372800Hz)
    }


//============================================================================
void init_timer (void) {
//  TACCR0 ...;                 // jede Sekunde einen Interrupt 
//  TACTL ....;                 // Taktquelle Uhrenquarz count up to
//  TACCTL0 ..;                 // Interrupt freigeben
    }


//============================================================================  
