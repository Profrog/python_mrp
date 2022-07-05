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

# 해당 python 파일을 gnss 장비의 로그를 기록한 .txt 파일과 같이 둡니다.
# 로그 .txt 파일의 이름을 input.txt로 변형합니다
# linux 명령어 창에서 "python3 convert.py" 명령어를 통하여 본 프로그램을 실행하면 "output.txt"에 정리되어 나옵니다

b_time = 60 #10초 간격으로 묶음
a = 1 #기본값
bound = 1 #0일때는 모두 같은색, 1일때는 b_time 간격으로 다른색
file_name = 'move'


def lat(t):
 return float(t[:2]) + float(t[2:])/60

def lon(g):
 return float(g[:3]) + float(g[3:])/60
 
data2 = open(file_name + '.txt', 'r')
#data3 = open('output.txt', 'a')



string0 = data2.readline()
forsplit = '	'
l_size = 1000

test1 = np.zeros((l_size,l_size))


def point_mark(x1,x2,y1,y2,data):    
 for x0 in range(x1,x2 +1,1):
   for y0 in range(y1,y2 +1,1):
     test1[x0,y0] = int(data) #after using other way  


while string0:
 try: 
  data_list0 = string0.split(forsplit)
  time_a = float(data_list0[0])
  x0 = str(float(data_list0[1]))[7:10]
  y0 = str(float(data_list0[3]) -100)[7:10]
  x1 = int(x0)
  y1 = int(y0)
  
  if time_a > b_time: 
   a = a + bound
   b_time = b_time * 2
  
  point_mark(y1,y1+4,x1,x1+4,a)
  
  
  print(str(x0) + " "  + str(y0))  

  string0 = data2.readline()
   
 except:
  print("error " + string0) 


#cmap0 = plt.get_cmap('Greys')
plt.matshow(test1)
#plt.grid(True, color = 'black')
#plt.show()
plt.savefig(file_name + str(bound) + '.png', dpi=300)  
  
 
    
