#ifndef PROJECT_H_
#define PROJECT_H_

#include <msp430x16x.h>      // Systemdefinitionen von TI für den MSP430F1612
#include "../init.h"        // Initialisierung des Mikrocontrollers
#include "../CC1100.h"      // CC1100 Funktransceiver
#include "../system.h"      // Systemfunktionen MSB430H
#include "../interrupts.h"  // ISR - Interrupt Service Routinen
#include <stdio.h>          // includes TI MSP430F1612
#include "../SHT11.h"
#include <stdint.h>
#include <math.h>

#include "lib/adu.h"
#include "lib/dac.h"
#include "lib/dma.h"
#include "lib/mma.h"
#include "lib/timer.h"
#include "lib/uart.h"

#endif /*PROJECT_H_*/
