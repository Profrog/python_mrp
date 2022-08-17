import serial
import time
import os
import string
import argparse
import cv2
import sys
import time
import math
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap


lat_list = [ 37.450323, 37.450358, 37.450385, 37.450424, 37.450467, 37.450473, 37.450451, 37.450414, 37.450388, 37.450341 ]
lon_list = [ 126.657258, 126.657261, 126.657289, 126.657279, 126.657275, 126.657196, 126.657156, 126.657153, 126.657165, 126.657133]
point_num = 10


# 해당 python 파일을 gnss 장비의 로그를 기록한 .txt 파일과 같이 둡니다.
# 로그 .txt 파일의 이름을 input.txt로 변형합니다
# linux 명령어 창에서 "python3 convert.py" 명령어를 통하여 본 프로그램을 실행하면 "output.txt"에 정리되어 나옵니다


b_time = 60 #10초 간격으로 묶음
a_time = 0

a = 1 #기본값
bound = 1 #0일때는 모두 같은색, 1일때는 b_time 간격으로 다른색
file_name = 'stop'


lat_m = 180
lon_m = 150


def lat(t):
 return (float(t[:2]) + float(t[2:])/60)

def lon(g):
 return (float(g[:3]) + float(g[3:])/60)
 
#data2 = open(file_name + '.txt', 'r')
#data3 = open('output.txt', 'a')


forsplit = '	'
l_size = 1000

test1 = np.zeros((l_size,l_size))


def point_mark(x1,x2,y1,y2,data):    
 for x0 in range(x1,x2+1,1):
   for y0 in range(y1,y2+1,1):
    
    x00 = x0 if x0 < l_size else 0
    y00 = y0 if y0 < l_size else 0
    test1[x00,y00] = int(data)
     
    
for i in range(0,10):

  #data_list0 = string0.split(forsplit)
  #time_a = float(data_list0[0])
  x0 = str(float(lat_list[i]))[6:9]
  y0 = str(float(lon_list[i]))[7:10]
  x1 = int(x0)
  y1 = int(y0)
  
  if x1 < 10:
   x1 = x1 * 100
  
  elif x1 < 100:
   x1 = x1 * 10
  
  if y1 < 10:
   y1 = y1 * 100
   
  elif y1 < 100:
   y1 = y1 * 10
  
  
  
  
  point_mark(x1,x1+10,y1,y1+10,a)
  
  
  print(str(x1) + " "  + str(y1))  

 
#cmap0 = plt.get_cmap('Greys')
plt.matshow(test1)
#plt.grid(True, color = 'black')
#plt.show()
plt.savefig(file_name + str(bound) + '.png', dpi=300)  
  
 
    
