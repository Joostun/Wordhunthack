import serial 
import time
import subprocess

print("input grid")
grid = input();

#for justin's personal testing use.
# result = subprocess.run(args = ['java', '-jar', 'out/artifacts/wordhunthack_jar/wordhunthack.jar', grid],
#                         universal_newlines = True,
#                         stdout=subprocess.PIPE)

#for standard file structure
result = subprocess.run(args = ['java', '-jar', 'wordhunthack.jar', grid],
                        universal_newlines = True,
                        stdout=subprocess.PIPE)

codeLines = result.stdout.splitlines()


#com port should be changed based on usb port used.
#most likely error is that the com port is not updated
ser = serial.Serial(port="COM5", baudrate=115200)


ser.write(b"\r\n\r\n") # Wake up microcontroller
time.sleep(1)
ser.reset_input_buffer()

wordCounter = 0

for command in codeLines:
    if command.strip().startswith(';') or command.isspace() or len(command) <=0:
        continue
    else:
        ser.write((command+'\n').encode())
        while(1): # Wait untile the former gcode has been completed.
            if ser.readline().startswith(b'ok'):
                print("recieved ok")
                break

ser.close() 
