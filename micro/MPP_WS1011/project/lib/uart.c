#include <msp430x16x.h>      // Systemdefinitionen von TI für den MSP430F1612
#include "../../init.h"        // Initialisierung des Mikrocontrollers
#include "../../CC1100.h"      // CC1100 Funktransceiver
#include "../../system.h"      // Systemfunktionen MSB430H
#include "../../interrupts.h"  // ISR - Interrupt Service Routinen
#include <stdio.h>          // includes TI MSP430F1612
#include "../../SHT11.h"

void uart1_put_char(char c)  {
    while((U1TCTL & TXEPT) == 0);
    
    U1TXBUF = c;
}

void uart1_init() {
    P3SEL |= 0xC0;          // USART RX und TX dem Modul zuweisen
    U1CTL = SWRST;          // Reset
    U1CTL |= CHAR;          // Format 8N1
    U1TCTL |= SSEL1;        // Taktquelle SMCLK
    U1BR0 = 0x40;           // Teiler Low-Teil, da 7372800/64 ca. 115200
    U1BR1 = 0x00;           // Teiler High-Teil
    U1MCTL = 0x00;          // Modulationskontrolle
    ME2 |= URXE1 + UTXE1;   // Enable USART1 TXD/RXD
    U1CTL &= ~SWRST;        // Reset
    IE2 |= URXIE1;          // Enable Interrupt
}

void uart1_put_str(char* str) {
    int i = 0;
    while(str[i] != '\0')
        uart1_put_char(str[i++]);
}
