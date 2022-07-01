import serial
import time
import os
import string
import argparse
import cv2
import sys
import time

start0 = time.time()
end0 = time.time()
os.system("sudo chmod a+rw /dev/ttyUSB0")
global ser
ser = serial.Serial('/dev/ttyUSB0', 115200)
data0 =  open('data1.txt', 'a')
data =  open('data2.txt', 'a')

forsplit = '	'

i = 0
print('lat	lon')
while end0 - start0 <= 600:
 string0 = ''
 end0 = time.time()
 line = ser.readline()
 data0.write(line.decode("utf-8"))
 data_list = line.decode("utf-8").split(',')
 #print(data_list[0])
 
 if data_list[0] == '$GNGLL':
  string0 = str(end0-start0) + forsplit + data_list[1] + forsplit + data_list[3] + "\n"
  print(str(end0-start0) + ": " + string0)
  #print(i)
  data.write(string0)
  #time.sleep(0.01)
  #i = i + 1
data.close()
data0.close()
