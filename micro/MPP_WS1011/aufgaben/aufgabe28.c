#include "msp430x16x.h"     // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include "project.h"
#include "aufgabe19.h"
#include <string.h>
#include "mma.h"

#define BUFFER_SIZE 255

voidReadSensors() {
}

void sendSensors()
{
    char buffer[BUFFER_SIZE];
    char mmaBuffer[32];

    SHT11_Read_Sensor();

    printMMAValues("%f\t%f\t%f", buffer);

    sprintf(buffer, "%s\t%s\t%s\t...\r\n", temp_char, humi_char, mmaBuffer);
    sendPacket(7, 17, buffer, BUFFER_SIZE);
}

#pragma vector = TIMERB0_VECTOR
__interrupt void TIMERB0_ISR (void)
{
    sendSensors();

    // Clears the interrupt flag
    CLEAR(TBCCTL0, CCIFG);
}

void initTransceiver() {
    setUid(17);

    initSecTimer(5);
}

void initReciever() {
    setUid(7);
}

void Aufgabe28() {
    switchFreq(0);

    initTransceiver();
    //initReciever();

    while(1);
}
