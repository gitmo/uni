//
//  problem5.cpp
//  C++ Course Exercises
//
//  Created by gitmo on 11.10.11.
//  Copyright 2011 Team Awesome. All rights reserved.
//

#include <iostream>
#include <cstdio>
#include <unistd.h>
#include <errno.h>
#include "problem5.h"

#define LINELEN 255
#define NUM 20

void swap (char& a, char& b) {
    char t = a;
    a = b;
    b = t;
}

void qsort(int left, int right, char data[]) {
    if (left < right) {
        // Choose right-most element as pivot
        char pivot = data[right];
        // Partition
        int store = left;
        for (int i = left; i < right; i++) {
            if (data[i] < pivot)
                swap(data[i], data[store++]);
        }
        swap(data[store], data[right]); // move pivot
        // Sort recursively
        qsort(left, store - 1, data);
        qsort(store + 1, right, data);
    }
}

char * sort(char data[]) {
    qsort(0, (int) strlen(data) - 1, data);
    return data;
}

int main (int argc, const char * argv[])
{
    FILE* fd;
    const char* fname = "english.txt";
    char lines[NUM][LINELEN];
    
    std::cout << "opening " << fname << '\n';

    // Open file for reading
    if (!(fd = fopen(fname, "r"))) {
        perror(fname);
        exit(1);
    }
        
    for (int i = 0; i<NUM; i++) {
        char *line = lines[i];
        fgets(line, LINELEN, fd);
        size_t len = strlen(line) - 1;
        line[len] = '\0';
        std::cout << "String " << i << ": " << line << " -> " << sort(line) << '\n';
    }
    return 0;
}