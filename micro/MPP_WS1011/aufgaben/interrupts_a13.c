extern char trafficSignalsAnimation;

#pragma vector = WDT_VECTOR
__interrupt void WDT_ISR (void)
{
    if (led1) {
        CLEAR(P4OUT, BIT1);
    } else {
        //Watchdog-Timer ausschalten
        WDTCTL = WDTPW + WDTHOLD;
        //Ampelschaltung
        trafficSignalsAnimation = 1;
    }

    CLEAR(IFG1, WDTIFG);
}

#pragma vector = PORT1_VECTOR
__interrupt void PORT1 (void)
{
    // Reset watchdog counter while preserving other status bits
    WDTCTL = WDTPW + (WDTCTL & 0x00FF) + WDTCNTCL;
    P4OUT ^= BIT1;

    //Interrupt Flag ausschalten
    CLEAR(P1IFG, 0xFF);
}
