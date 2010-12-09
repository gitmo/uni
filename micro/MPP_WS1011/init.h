#ifndef INIT_H_
#define INIT_H_

//=========================================================================
void initPort (void);       // Grundinitialisierung der Portleitungen
//=========================================================================
void DCO (void);            // Dco Taktquelle aktivieren mit 7.3728MHz
//=========================================================================
void XT2 (void);            // XT2 Taktquelle aktivieren mit 7.3728MHz
//=========================================================================
void initUART0_SPI(void);   // Init der UART-SPI für CC1100
//=========================================================================
void initUART1(void);       // Init der UART-RS232 
//=========================================================================
void init_timer(void);      // Init des Timers A
//=========================================================================

#endif /*INIT_H_*/
