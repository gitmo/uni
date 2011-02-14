#include <msp430x16x.h>      // Systemdefinitionen von TI für den MSP430F1612
#include "../../init.h"        // Initialisierung des Mikrocontrollers
#include "../../CC1100.h"      // CC1100 Funktransceiver
#include "../../system.h"      // Systemfunktionen MSB430H
#include "../../interrupts.h"  // ISR - Interrupt Service Routinen
#include <stdio.h>          // includes TI MSP430F1612
#include "../../SHT11.h"
#include "dma.h"

void initDMA(DMA_TRANSFER_SELECT transferSelect0, DMA_TRANSFER_SELECT transferSelect1) {

    //DMACTL0 DMA Control Register 0
    //  trigger select
    DMACTL0 = transferSelect0 * 0x0001u + transferSelect1 * 0x0010u;

    //DMACTL1 DMA Control Register 1
    //  DMA transfer immediately: DMAONFETCH = 0
    //  Disable NMI interrupt: ENNMI = 0
    DMACTL1 = 0;

}

void initDMA_0(uint16_t* pData, uint16_t dataLength)
{
    CLEAR(DMA0CTL, DMAEN);
    
    //DMAxCTL DMA Channel x Control Register
    //  block repeat DMA Transfer mode: DMADTx = 000
    //  Destination address is unchanged: DSTINCRx = 00
    //  Source address is incremented: SRCINCRx = 11
    //  DMA enable: DMAEN = 1
    DMA0CTL = DMADT_4 + DMASRCINCR_3 + DMALEVEL + DMAIE;

    //DMA Source Address Register
    DMA0SA = (uint16_t) pData;

    //DMA Destination Address
    DMA0DA = (uint16_t) &DAC12_0DAT;

    //DMA Size Address Register
    //  one byte ist transferrred
    DMA0SZ = dataLength;

    //Anschalten
    SET(DMA0CTL, DMAEN);
}

void initDMA_1(uint16_t* pData, uint16_t dataLength)
{
    CLEAR(DMA1CTL, DMAEN);
    
    //DMAxCTL DMA Channel x Control Register
    //  block repeat DMA Transfer mode: DMADTx = 000
    //  Destination address is unchanged: DSTINCRx = 00
    //  Source address is incremented: SRCINCRx = 11
    //  DMA enable: DMAEN = 1
    DMA1CTL = DMADT_4 + DMASRCINCR_3 + DMALEVEL + DMAIE;

    //DMA Source Address Register
    DMA1SA = (uint16_t) pData;

    //DMA Destination Address
    DMA1DA = (uint16_t) &DAC12_1DAT;

    //DMA Size Address Register
    //  one byte ist transferrred
    DMA1SZ = dataLength;

    //Anschalten
    SET(DMA1CTL, DMAEN);
}

