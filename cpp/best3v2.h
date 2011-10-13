class best3 {
    unsigned int v[3];
    unsigned int _count;
    static unsigned int _count_obj;
    static unsigned int _count_all;

    public:
        best3();
        void push(unsigned int x);
        void print();
        unsigned int count();
        unsigned int count_all();
        unsigned int count_obj();
};