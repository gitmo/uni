#include <unistd.h>
#include <sys/ptrace.h>
#include <stdio.h>

int main(int argc, char **argv)
{
   if (ptrace(PTRACE_TRACEME, 0, NULL, NULL) == -1) {
       fprintf(stdout, "debugger present.\n");
       return 1;
   }

   fprintf(stdout, "no debugger present.\n");

   return 0;
}

