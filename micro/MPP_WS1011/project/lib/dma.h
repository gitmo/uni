#ifndef DMA_H_
#define DMA_H_

typedef enum DMA_TRANSFER_SELECT {
    TRANSFER_SELECT_DMA_SW,
    TRANSFER_SELECT_TACCR2,                 /* Timer_A (TACCR2.IFG) */
    TRANSFER_SELECT_TBCCR2,                 /* Timer_B (TBCCR2.IFG) */
    TRANSFER_SELECT_UART0_Recieve,          /* UART0/I2C receive */
    TRANSFER_SELECT_UART0_Transmit,         /* UART0/I2C transmit */
    TRANSFER_SELECT_DAC12_0CTL,             /* DAC12_0CTL.DAC12IFG */
    TRANSFER_SELECT_ADC12,                  /* ADC12 (ADC12IFG) */
    TRANSFER_SELECT_TACCR0,                 /* Timer_A (TACCR0.IFG) */
    TRANSFER_SELECT_TBCCR0,                 /* Timer_B (TBCCR0.IFG) */
    TRANSFER_SELECT_UART1_Recieve,          /* UART1 receive */
    TRANSFER_SELECT_UART1_Transmit,         /* UART1 transmit */
    TRANSFER_SELECT_Multiplier_Ready,       /* Multiplier ready */
    TRANSFER_SELECT_previous_Dma_Channel,   /* previous DMA channel DMA2IFG */
    TRANSFER_SELECT_ext_Trigger             /* ext. Trigger (DMAE0) */
} DMA_TRANSFER_SELECT;

void initDMA(DMA_TRANSFER_SELECT transferSelect0, DMA_TRANSFER_SELECT transferSelect1);
void initDMA_0(unsigned int *pData, unsigned int dataLength);
void initDMA_1(unsigned int *pData, unsigned int dataLength);

#endif /*DMA_H_*/
