#include "best3.h"
#include <iostream>
#include <vector>
#include <algorithm>

best3::best3() {
    _count_obj++;
    _count = 0;
}

void best3::push(unsigned int x) {
    _count++;
    _count_all++;
    if(v.empty() || v.size() <= 2) {
        //stil place for insert
        v.insert(v.begin(),x);
    } else {
        std::vector<unsigned int>::iterator it;
        //sort the vector
        std::sort(v.begin(), v.begin()+3);
        for(it = v.begin(); it != v.end(); ++it) {
            if(x > *it) {
                //insert element if greater then element at position it
                *it = x;
                break;
            }
        }
    }
}

unsigned int best3::count_all() {
    return _count_all;
}

unsigned int best3::count() {
    return _count;
}

unsigned int best3::count_obj() {
    return _count_obj;
}

void best3::print() {
    std::vector<unsigned int>::iterator it;
    for(it = v.begin(); it != v.end(); ++it) {
        std::cout << *it << '\n';
    }
}


int main() {
    //realy awesome test ;)
    best3 *best = new best3();
    best->push(5);
    best->push(2);
    best->push(2);
    best->push(4);
    best->push(5);
    best->push(4);
    best->push(5);
    best->push(5);
    best->push(5);
    best->push(5);
    best->print();
    best3 *best2 = new best3();
    best2->push(42);
    best2->push(23);
    best2->push(13);
    best2->push(7);
    std::cout << "Count: " << best->count() << std::endl;
    std::cout << "CountAll: " << best->count_all() << std::endl;
    std::cout << "CountObj: " << best->count_obj() << std::endl;
}
