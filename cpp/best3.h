#include <vector>
#include <iostream>

static unsigned int _count_all = 0;
static unsigned int _count_obj = 0;

class best3 {


    private:
        std::vector<unsigned int> v;
        unsigned int _count;

    public:
        best3();
        void push(unsigned int x);
        void print();
        unsigned int count();
        unsigned int count_all();
        unsigned int count_obj();
};
