#ifndef SHT11_H_
#define SHT11_H_


//=========================================================================
// SHT11 Variablen
//=========================================================================
#define SHT11_SCK_LOW   (P3OUT &= ~0x20)        // serial clock line low
#define SHT11_SCK_HIGH  (P3OUT |= 0x20)         // serial clock line high

#define SHT11_DATA      ((P3IN & 0x10)>>4)      // read serial I/O
#define SHT11_DATA_LOW  (P3OUT &= ~0x10)        // serial I/O line low
#define SHT11_DATA_HIGH (P3OUT |= 0x10)         // serial I/O line high
#define SHT11_DATA_IN   (P3DIR &= ~0x10)        // serial I/O as input
#define SHT11_DATA_OUT  (P3DIR |= 0x10)         // serial I/O as output

#define SHT11_NO_ACK    (0)
#define SHT11_ACK       (1)
                                    //adr command  r/w
#define SHT11_STATUS_REG_W  (0x06)  //000  0011    0
#define SHT11_STATUS_REG_R  (0x07)  //000  0011    1
#define SHT11_MEASURE_TEMP  (0x03)  //000  0001    1
#define SHT11_MEASURE_HUMI  (0x05)  //000  0010    1
#define SHT11_RESET         (0x1E)  //000  1111    0


extern char humi_char[12];          // relative Feuchtigkeit als Zeichenkette
extern char temp_char[15];          // Temperatur als Zeichenkette


//=========================================================================
// SHT11 Sensor auslesen
void SHT11_Read_Sensor(void);

//=========================================================================
// SHT11 Wandlung starten
char SHT11_Measure(unsigned char *p_value, unsigned char *p_checksum, unsigned char mode);

//=========================================================================
// Byte schreiben und ACK testen
char SHT11_Write_Byte(unsigned char value);

//=========================================================================
// Byte lesen und ACK=1 oder ACK=0 am Ende setzen 
char SHT11_Read_Byte(unsigned char ack);

//=========================================================================
// Schreiben Statusregister mit 8Bit Checksumme
char SHT11_Write_StatusReg(unsigned char *p_value);


//=========================================================================
// Lesen Statusregister mit 8Bit Checksumme
char SHT11_Read_StatusReg(unsigned char *p_value, unsigned char *p_checksum);

//=========================================================================
// Software Reset 
char SHT11_SoftReset(void);

//=========================================================================
// Verbindungs Reset 
void SHT11_ConnectionReset(void);

//=========================================================================
// Transmision Start 
void SHT11_TransStart(void);


#endif /*SHT11_H_*/
