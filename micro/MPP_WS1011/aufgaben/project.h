#ifndef CONFIG_H_
#define CONFIG_H_
	
#define LED_DIR P4DIR
#define LED_BITS P4OUT

#define LED_ROT BIT0
#define LED_GELB BIT1
#define LED_GRUEN BIT2

#define LED_OFF 		(SET(LED_BITS, (LED_ROT | LED_GELB | LED_GRUEN)))
#define LED_ON 			(CLEAR(LED_BITS, (LED_ROT | LED_GELB | LED_GRUEN)))
#define LED_TOGGLE(x)	(LED_BITS ^=  (x))
#define LED_SET(x)  	(CLEAR(LED_BITS, (x)))

#define LED_DIR_OUT (LED_ROT | LED_GELB | LED_GRUEN)

#define TASTEN_BITS P1IN
#define TASTE0 BIT0
#define TASTE1 BIT1
	
#define RSEL_RESET 		(BCSCTL1 &= ~(RSEL0 | RSEL1 | RSEL2))
#define DCO_RESET		(DCOCTL &= ~(DCO0 | DCO1 | DCO2))
#define MOD_RESET		(DCOCTL  &= ~(MOD0 | MOD1 | MOD2 | MOD3 | MOD4))
#define DIVM_RESET		(BCSCTL2 &= ~(DIVM0 | DIVM1))
#endif /*CONFIG_H_*/
