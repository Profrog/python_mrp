import serial
import time
import os
import string
import argparse
import cv2
import sys
import time
import codecs
from datetime import datetime
now = datetime.now()
now_0 = str(now.hour) + ":" + str(now.minute) + ":" + str(now.second)


raw_0 = 'data1' + now_0 + ".txt" 
raw_1 = 'data2' + now_0 + ".txt"

start0 = time.time()
end0 = time.time()
os.system("sudo chmod a+rw /dev/ttyUSB0")
global ser
ser = serial.Serial('/dev/ttyUSB0', 115200)
data0 =  open(raw_0, 'a')
data =  open(raw_1, 'a')


def lat(t):
 return (float(t[:2]) + float(t[2:])/60)

def lon(g):
 return (float(g[:3]) + float(g[3:])/60)


forsplit = '	'

i = 0
print('lat	lon')
while end0 - start0 <= 600:
 string0 = ''
 end0 = time.time()
 line = ser.readline()
 #line = str(line, errors='ignore')
 
 print(line)
 data0.write(line.decode("utf-8"))
 data_list = line.decode("utf-8").split(',')
 #print(data_list[0])
 
 if data_list[0] == '$GNGLL':
  string0 = str(end0-start0) + forsplit + str(lat(data_list[1])) + forsplit + str(lon(data_list[3])) + "\n"
  print(str(end0-start0) + ": " + string0)
  #print(i)
  data.write(string0)
  #time.sleep(0.01)
  #i = i + 1
data.close()
data0.close()
