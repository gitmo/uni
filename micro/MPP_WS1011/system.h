#ifndef SYSTEM_H_
#define SYSTEM_H_

//=========================================================================
// Systemdefinitionen
//=========================================================================
#define LED         (0x80)                
#define LEDOFF      (P5OUT |= LED)    
#define LEDON       (P5OUT &= ~LED)   
#define LEDTOGGLE       (P5OUT ^=  LED)    

#define MCU_RESET       (WDTCTL = 0x0000)
#define SET(x, y)       ((x) |= (y))
#define CLEAR(x, y)     ((x) &= (~(y)))
#define NOPS do{_NOP();_NOP();_NOP();_NOP();_NOP();_NOP();_NOP();_NOP();_NOP();_NOP();}while(0)
#define TIMERB_CCR0     (65535)
#define INFOMEM_A       (0x1080)
#define INFOMEM_B       (0x1000)
#define MEM_ID          (0x1000)            // Adresse ID im INFOMEM 
    

//extern int flashMEM ;  // Variable RAM
//extern int *MEMptr ; // MEMptr ist Zeiger 
//=========================================================================
// Typdeklarationen
//=========================================================================
typedef char bool;
typedef unsigned char UINT8;
typedef unsigned int  UINT16;
typedef unsigned long UINT32;
typedef int            INT16;
typedef unsigned char uint8_t;
typedef unsigned int  uint16_t;
typedef unsigned long uint32_t;
enum {
  true = 1,
  false = 0
};
//=========================================================================


//=========================================================================
// Funktionen zum Zugriff auf den Flashspeicher
//=========================================================================
void flash_read(unsigned int add, unsigned char* ptr, unsigned int len);
// liest Flash
//=========================================================================
void flash_write(unsigned int add, unsigned char* ptr, unsigned int len);
// schreibt Flash
//=========================================================================
void flash_erase( int *Data_ptr );      
// löscht adressiertes Segment 
//=========================================================================
void flash_prog( char *Data_ptr, char word );
// schreibt ein Byte
//=========================================================================



//=========================================================================
// Funktionen zum Zugriff auf die Serielle RS232 Schittstelle
//=========================================================================
extern void writestr(char*);            // Zeichenkettenausgabe RS232
//=========================================================================
extern void writechar(char c);          // Zeichenausgabe RS232


//=========================================================================
// Funktionen für Verzögerungen
//=========================================================================
// Funktion die c * 10 us wartet (bei 7.
void wait(unsigned int c);              
//=========================================================================
// Funktion die c * 120 us wartet, c maximal 5461
#define wait_120us(c)   (wait((unsigned int)c*12))
//=========================================================================
// Funktion liefert Zufallszahl
unsigned int irand(unsigned int max);   // Funktion für Rand 0 <= x <= max
//=========================================================================




#endif /*SYSTEM_H_*/
