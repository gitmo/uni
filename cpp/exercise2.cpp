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

bool reverse (char str[]) {
    bool palindrom = true;
    size_t n = 0;
    size_t len = strlen(str);
    for (size_t i = len; i > len / 2 ; i--) {
        char c = str[i - 1];
        str[i - 1] = lower(str[n]);
        str[n] = lower(c);
        if (str[n] != str[i - 1])
            palindrom = false;
        n++;
    }
    return palindrom;
}

void test(const char * s)
{
    char str[strlen(s) + 1];
    strcpy(str, s);
    std::cout << str << '\n';
    if (reverse(str))
        std::cout << "Palindrom!" << std::endl;
    std::cout << str << std::endl;
}

int main (int argc, const char * argv[])
{
    test("Hamster");
    test("Anna");
    return 0;
}

