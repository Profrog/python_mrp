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
import math
import string
import argparse
import sys
import time
import codecs
from datetime import datetime
import decimal
import bluetooth
now = datetime.now()
now_0 = str(now.hour) + ":" + str(now.minute) + ":" + str(now.second)

lat_list = [ 37.450323, 37.450358, 37.450385, 37.450424, 37.450467, 37.450473, 37.450451, 37.450414, 37.450388, 37.450341 ]
lon_list = [ 126.657258, 126.657261, 126.657279, 126.657279, 126.657275, 126.657176, 126.657156, 126.657153, 126.657165, 126.657153]



#raw_0 = 'data1' + now_0 + ".txt" 
raw_1 = 'data2' + now_0 + ".txt"

start0 = time.time()
end0 = time.time()
os.system("sudo chmod a+rw /dev/ttyUSB0")
global ser
ser = serial.Serial('/dev/ttyUSB0', 115200)


lat0 = 0
lon0 = 0
time0 = 0
lat_conv = 91170  
lon_conv = 111000
l_conv = 1.11

global l_count
l_count = 0  

max_time = 600
yellow = LED(4)
global client_sock

def bluetooth0():
 server_sock=bluetooth.BluetoothSocket( bluetooth.RFCOMM )
 port=bluetooth.PORT_ANY
 server_sock.bind(("",port))
 server_sock.listen(1)
 print(port)
 client_sock,address = server_sock.accept()
 return client_sock

def lat(t):
 if t == '':
   return 0
  
 return (float(t[:2]) + float(t[2:])/60)

def lon(g):
 if g == '':
   return 0
  
 return (float(g[:3]) + float(g[3:])/60)
 
 
def speed0(lat1, lon1,time1):
 if l_count == 0:
   return 0
 
 else:
   #print(str(time1 - time0))
   return (math.sqrt(math.pow(lat_conv * (lat1-lat0) * l_conv , 2) + math.pow(lon_conv * (lon1-lon0) * l_conv , 2)))/(time1 - time0)

def zone_num(lat1, lon1):
  if lon1 < lon_list[0] and lon1 > lat_list[5]:
    if lat1 > lat_list[0] and lat1 < lat_list[1]:
      return "A"
  
  if lon1 < lon_list[0] and lon1 > lat_list[5]:
    if lat1 > lat_list[1] and lat1 < lat_list[2]:
      return "B"    
        
  if lon1 < lon_list[0] and lon1 > lat_list[5]:
    if lat1 > lat_list[2] and lat1 < lat_list[3]:
      return "C"
  
  if lon1 < lon_list[0] and lon1 > lat_list[5]:
    if lat1 > lat_list[3] and lat1 < lat_list[4]:
      return "D"
  
  else: 
   return "F"            



def serial_cont():
  forsplit = ','

  i = 0
  print('lat	lon')
  end0 = time.time()

  while end0 - start0 <= max_time:
   #data0 =  open(raw_0, 'a')
   data =  open(raw_1, 'a')
    
   string0 = ''
   end0 = time.time()
   line = ser.readline()
   
   #print(line)
   #data0.write(line.decode("utf-8"))
   data_list = line.decode("utf-8").split(',')
 
   if data_list[0] == '$GNGLL':
    lat1 = lat(data_list[1])
    lon1 = lon(data_list[3]) 
    time1 = float(data_list[5] if data_list[5] != '' else 1)
    
    
    #string0 = str(end0-start0) + forsplit + str(lat1) + forsplit + str(lon1) + forsplit + str(speed0(lat1,lon1,time1)) + "\n"
    lat2 = float(str(lat1)[6:9]) if lat1 > 0 else lat1
    lon2 = float(str(lot1)[7:10]) if lon1 > 0 else lon1
    string0 = str(end0-start0) + forsplit + str(lat2) + forsplit + str(lon2) + forsplit + zone_num(lat1,lon1) + forsplit + str(speed0(lat1,lon1,time1)) + "\n"
    
    data.write(string0)
    print(str(end0-start0) + ": " + string0)
    client_sock.send(string0);
    
    lat0 = lat1
    lon0 = lon1
    time0 = time1
     
    l_count = 1
    #time.sleep(0.01)
    
   data.close()
   #data0.close()

if True:
  yellow.on()

  ###bleutooth####
  client_sock = bluetooth0()
  ########
 
  serial_cont() 
  #client_sock.close()
  #server_sock.close() 
  yellow.off()
#except:
  #print("waiting")
  #client_sock = bluetooth0()
  #serial_cont()
