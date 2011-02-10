#include "project.h"

#define DATA_SIZE 512
#define PI 3.1415926535897932384626433832795f

uint16_t data_0[2*DATA_SIZE];
uint16_t data_1[2*DATA_SIZE];

void calcSinus(uint16_t* data)
{
    int i;
    double amplitude, dc_offset, step_deg;

    // Die Sinusfunktion soll eine Amplitude von 1V und einen Gleichspannungsanteil von 1,5V besitzen
    amplitude = 4095 / 3; //Outputrange liegt zwischen 0 und 3V aufgeteilt auf 12Bit(0...4965)
    dc_offset = 4095 / 2;
    step_deg = (2*PI) / DATA_SIZE;

    for(i=0; i<2*DATA_SIZE; i++)
    {
        data[i] = (uint16_t) (dc_offset + amplitude * sin( i * step_deg ));
    }
}

void projectMain() {
    calcSinus(data_0);
    calcSinus(data_1);

    initTimerA(1.0/1000);
    initTimerB(1.0/3000);

    initDAC0();
    initDAC1();
    
    initDMA(TRANSFER_SELECT_TBCCR0, TRANSFER_SELECT_TACCR0);
    
    initDMA_0(data_0 + DATA_SIZE/4, DATA_SIZE );
    initDMA_1(data_1, DATA_SIZE);
    
    //_bis_SR_register(GIE);
    
    while(1);
        
}
