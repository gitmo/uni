#include "msp430x16x.h"      // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612 
#include "../SHT11.h"
#include "project.h"
#include "aufgabe19.h"
#include "aufgabe20.h"
#include "aufgabe21.h"

extern char humi_char[12];
extern char temp_char[15];

void uart1_put_str(char* str) {
    int i = 0;
    while(str[i] != '\0')
        uart1_put_char(str[i++]);
}
void Aufgabe21() {
    initSecTimer(1);
    
    _bis_SR_register(GIE);
    
    while(1) {
    	char buffer[32];
    	sprintf(buffer, "%d:%d:%d\t", dayTime.hh, dayTime.mm, dayTime.ss);
        uart1_put_str(buffer);
        
        SHT11_Read_Sensor();
        
        uart1_put_str(humi_char);
        uart1_put_str(temp_char);
        uart1_put_str("\r\n");
    }
}
