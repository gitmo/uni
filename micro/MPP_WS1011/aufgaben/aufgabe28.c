#include "msp430x16x.h"     // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "../SHT11.h"
#include <string.h>
#include "project.h"
#include "aufgabe19.h"
#include "mma.h"

#define BUFFER_SIZE 255

#define LAB_ID 7        // Number on the desk
#define CHAN LAB_ID     // Channel
#define TXID LAB_ID +10 // Sender ID 
#define RXID LAB_ID     // Receiver ID

void sendSensors()
{
    char buffer[BUFFER_SIZE];
    char mmaBuffer[32];
    int taster;
    
    SHT11_Read_Sensor();

    printMMAValues("%f\t%f\t%f", mmaBuffer);
    taster = TASTEN_BITS & (TASTE0+TASTE1);

    sprintf(buffer, "%s \t %s \t %s \t %d\r\n", temp_char, humi_char, mmaBuffer, taster);
    
	// Toggle red LED on port 5
    LED_TOGGLE(LED_ROT);
	
	// Actual send: dest, source        
    sendPacket(RXID, TXID, buffer, strlen(buffer)+1);
    //str = "lame0";
//    sendPacket(RXID, TXID, str, strlen(str)+1);

    // Toggle red LED on port 5
    // TODO: Toggle on ack from receiver instead
    LED_TOGGLE(LED_ROT);
}

void parsePacket() {
    int i, tabcount = 0;
    for (i=2;i<((RxCC1100.length));i++)
        {
            if (RxCC1100.data[i] == '\t')
                tabcount++;
            if (tabcount == 5) {
                // Taster-Status
                switch (RxCC1100.data[i+1] - '0') {
                    case 0:
                        SET(LED_BITS, LED_GRUEN);
                        break;
                    case 1:
                        SET(LED_BITS, LED_GRUEN);
                        break;
                    case 2: 
                        CLEAR(LED_BITS, LED_GRUEN);
                        break;
                    case 3: 
                        CLEAR(LED_BITS, LED_GRUEN);
                        break;
                }
                    
            }
        }
}


//==============================================================
//===INT:01====ADR:FFE2====PORT2================================
//==============================================================
#pragma vector = PORT2_VECTOR
__interrupt void PORT2 (void) {
    char res ;          // CRC Check
    if (P2IFG & 0x01)   // Check P2IFG Bit P2.0 - CC1100 Rx Packet
        {
        CLEAR(P2IFG, 0x01);
        //LEDTOGGLE;
        LED_TOGGLE(LED_GELB);
        res = receivePacket();  // CRC Rückgabe
        if (res)                // wenn Packet OK ...
            {
            printPacket();      // Packet auf Terminal ausgeben
            parsePacket();
            sendPacket(TXID, RXID, "OK", 3);
            //LED_TOGGLE(LED_ROT);
            }
        else
            {
            spiStrobe(CC1100_SIDLE);    // Switch to IDLE
            spiStrobe(CC1100_SFRX);     // Flush the RX FIFO
            }
        }
    else
        {
        CLEAR(P2IFG, 0xFF);             // Clear all flags
        }
    spiStrobe(CC1100_SRX);              // Switch to RX Mode
    }
//==============================================================

//==============================================================
#pragma vector = TIMERB0_VECTOR
__interrupt void TIMERB0_ISR (void)
{
    sendSensors();
    // Clears the interrupt flag
    CLEAR(TBCCTL0, CCIFG);
}
//==============================================================

  
void initSender() {
    setUid(TXID);
    initSecTimer(1);
}

void initReceiver() {
    setUid(RXID);
}

void Aufgabe28() {
    initCC1100();         // CC1100 init und in RX Mode setzen
    initSender();
    //initReceiver();

    switchFreq(CHAN);
    // Interrupts global einschalten
    _bis_SR_register(GIE);

    while(1);
}
