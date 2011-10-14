//
//  exercise2.cpp
//  C++ Course Exercises
//
//  Created by gitmo on 11.10.11.
//  Copyright 2011 Team Awesome. All rights reserved.
//

#include <iostream>
#include <cstring>
#include "exercise2.h"

char lower (char c) {
    if( 'A' <= c && c <= 'Z' )
        return c + 'a' - 'A';
    return c;
}

void reverse (char str[]) {
    size_t n = 0;
    size_t len = strlen(str);
    for (size_t i = len; i > len / 2 ; i--) {
        char c = str[i - 1];
        str[i - 1] = lower(str[n]);
        str[n] = lower(c);
        n++;
    }
}


int main (int argc, const char * argv[])
{
    char str[] = "Hamster";
    std::cout << str << '\n';
    reverse(str);
    std::cout << str << '\n';
    return 0;
}

