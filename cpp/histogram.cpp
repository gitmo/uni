//
//  histogram.cpp
//  C++ Course Exercises
//
//  Created by Sebastian Me on 14.10.11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#include <iostream>
#include <cstdio>
using namespace std;

const char * fname = "english.txt";

#define NUMASCII 256

char * bar (int n)
{
    char * bar = new char[n+1];
    int i;
    for (i = 0; i < n; ++i)
        bar[i] = '|';
    bar[i] = '\0';
    return bar;
}

int main (int argc, const char * argv[])
{
    FILE * fd;
    int histogram[NUMASCII];
    
    for (int i = 0; i < NUMASCII; ++i) {
        histogram[i] = 0;
    }
    
    // Open file for reading
    if (!(fd = fopen(fname, "r"))) {
        perror(fname);
        exit(1);
    }
    
    char c;
    while ((c = fgetc(fd)) != EOF) {
        histogram[c]++;
    }

    fclose(fd);
    
    cout << "Histogram:" << endl;
    for (int i = 0; i < NUMASCII; ++i) {
        if (histogram[i] && isalpha(i))
            cout << (char) i << ": " << bar(histogram[i]) << endl;
    }
                
    return 0;
}