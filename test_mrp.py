#!/usr/bin/env python

# /etc/init.d/test.py
### BEGIN INIT INFO
# Provides:          scriptname
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start daemon at boot time
# Description:       Enable service provided by daemon.
### END INIT INFO

from gpiozero import LED
import serial
import time
import os
import string
import argparse
#import cv2
import sys
import time
import codecs
from datetime import datetime
import bluetooth
now = datetime.now()
now_0 = str(now.hour) + ":" + str(now.minute) + ":" + str(now.second)


#raw_0 = 'data1' + now_0 + ".txt" 
raw_1 = 'data2' + now_0 + ".txt"

start0 = time.time()
end0 = time.time()
os.system("sudo chmod a+rw /dev/ttyUSB0")
global ser
ser = serial.Serial('/dev/ttyUSB0', 115200)



def lat(t):
 if t == '':
   return 0
  
 return (float(t[:2]) + float(t[2:])/60)

def lon(g):
 if g == '':
   return 0
  
 return (float(g[:3]) + float(g[3:])/60)


server_sock=bluetooth.BluetoothSocket( bluetooth.RFCOMM )

port=bluetooth.PORT_ANY
server_sock.bind(("",port))
server_sock.listen(1)
 
client_sock,address = server_sock.accept()

forsplit = '	'
yellow = LED(4)
yellow.on()

i = 0
print('lat	lon')

while end0 - start0 <= 600:
 #data0 =  open(raw_0, 'a')
 data =  open(raw_1, 'a')
  
 string0 = ''
 end0 = time.time()
 line = ser.readline()
 #line = str(line, errors='ignore')
 
 print(line)
 #data0.write(line.decode("utf-8"))
 data_list = line.decode("utf-8").split(',')
 #print(data_list[0])
 
 if data_list[0] == '$GNGLL':
  string0 = str(end0-start0) + forsplit + str(lat(data_list[1])) + forsplit + str(lon(data_list[3])) + "\n"
  print(str(end0-start0) + ": " + string0)
  client_sock.send(string0);
  #print(i)
  data.write(string0)
  #time.sleep(0.01)
  #i = i + 1
 data.close()
 #data0.close()
 
client_sock.close()
server_sock.close() 
yellow.off()
