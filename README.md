# GPIO_I2C
GUI for I2C and GPIO

You will need to download WiringPi onto your computer 

for compiling JNI Wrapper
(REPLACE WriteJNI with whatever file name it is)
(SAME with libmyWrite.so to whatever libmy"blank")
(For Files that use wiring pi add -lwiringPi (to the c code compiler)

•	Compile Java code with 
o	javac (name).java –h .
•	Compile the header file
o	gcc -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -c "WriteJNI.h"
•	 Compille the c code
o	gcc -o libmyWrite.so -shared -fPIC -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux WriteJNI.c -lc
•	the rest
o	sudo cp libmyWrite.so /usr/lib
