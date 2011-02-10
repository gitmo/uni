//=========================================================================
//=== Startprojekt zum Mikroprozessorpraktikum ============================
//=========================================================================
#include "msp430x16x.h"     // Systemdefinitionen von TI für den MSP430F1612
#include "init.h"           // Initialisierung des Mikrocontrollers
#include "CC1100.h"         // CC1100 Funktransceiver
#include "system.h"         // Systemfunktionen MSB430H
#include "interrupts.h"     // ISR - Interrupt Service Routinen
#include "stdio.h"          // includes TI MSP430F1612
#include "SHT11.h"          // SHT11 Temperatur- und Feuchtesensor
#include "project/project.h"        // Header Datei für aufgabeX.c
                            // möglichst eigene xxx.h und xxx.c Dateien
                            // erstellen und verwenden.

main(void);                 //Haupprogramm

//====Hauptprogramm=======================================================

main(void)
    {

//===Hier sollten Variablen deklariert werden ============================
    // unsigned char i = 0;

//===Hier die notwendigen Initialisierungs Funktionen=====================
    //====================================================================
    initPort();             // Initialisierung der Port Register
    //====================================================================
    //== XT2() oder Dco() als Taktquelle einstellen
    //== durch Ein- oder Auskommentieren
    //== DCO ist bei LPM Einsatz bevorzugt muß zyklisch kalibriert werden
    //== XT2 ist quarzstabil muß nicht zyklisch kalibriert werden
    //
    //XT2 ();               // XT2 Taktquelle aktivieren mit 7.3728MHz
    DCO ();                 // Dco Taktquelle aktivieren mit 7.3728MHz
    //                         beachte DELTA
    //
    //====================================================================
    //flash_read(MEM_ID, &ID, 1);   // MSB430H ID aus dem INFOMEM lesen
    //====================================================================
    //init_timer();         // Init Timer für Sekundeninterrupt
    initUART1();            // UART-RS232 mit 115.2kBit/s initialisieren
    //====================================================================
    //== Initialisierung des CC1100 Transceivers - bitte nicht verändern!
    initUART0_SPI();        // CC1100 SPI UART initalisieren
    //initCC1100();         // CC1100 init und in RX Mode setzen
    //initCC1100_IDLE();  // CC1100 init und in den IDLE Mode setzen
    //initCC1100_POWERDOWN(); // CC1100 in den PowerDown Mode setzen
    //setUid(ID);         // CC1100 Adresse setzen, ID aus CC1100.c
    //====================================================================

//===Hier die Endlosschleife quasi das Betriebssystem=====================
    while(1)
        {

        // Hier den Code für die Lösung der Aufgabenstellung einfügen
        // am besten in der Form:
        //
        projectMain(); // Aufgabe1.h nicht vergessen!
        //
        // Aufgabex() sollte eigene Initialisierungsroutine enthalten und
        // eine eigene Endlosschleife

        }   // Ende der Endlosschleife

    }       // Ende Main
//====Ende des Hauptprogramms ============================================
