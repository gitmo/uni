#include "best3v2.h"
#include <iostream>
using namespace std;

unsigned int best3::_count_obj = 0;
unsigned int best3::_count_all = 0;

best3::best3() {
    _count_obj++;
    _count = 0;
    v[0] = v[1] = v[2] = 0;
}

void best3::push(unsigned int x) {
    _count++;
    _count_all++;
    for(int i = 0; i < 3; ++i) {
        if (v[i] < x) {
            v[i] = x;
            break;
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
    for(int i = 0; (i < 3 && i < _count); ++i) {
        cout << v[i] << '\n';
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
    cout << "Count: " << best->count() << endl;
    cout << "CountAll: " << best->count_all() << endl;
    cout << "CountObj: " << best->count_obj() << endl;
}
