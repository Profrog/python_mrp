# python_mrp


<img src = "https://user-images.githubusercontent.com/26535065/175887768-87aab946-71c0-4087-b54a-ab333bb2f7d2.jpg" width="25%" height="25%">

-----------------------------------------기본 스펙----------------------------  
model : MRP-2000

base type : DMB or LTE

RTK position Accuracy
-Horizontal : 0.010m + 1ppm CEP
-Vertical : 0.010m + 1ppm CEP
GNSS TTFF(Time To First Fix) : 28s(DMB mode), 38s(LTE mode)
RTK Convergence time : <10s

0.010m + 1ppm cep : 1 cm plus 1 mm per Km of baseline length

So 2 cm CEP when Base and Rover 10 Km apart​  
실험영상 : https://www.youtube.com/watch?v=fOSTibGu1l0  

-----------------------------------------코드 설명----------------------------  


test_mrp.py : n분간 $GNGLL 포멧으로 들어온 경도, 위도 분석  
map_mrp.py : test_mrp.py의 데이터로 map  
map_mrp2.py : fixed
test : 자동화 시스템을 위한 bash 파일  
bash.ros.mrp : ros를 이용해 mrp-2000 topic내용을 받아볼 수 있는 bash파일(사전 ros관련 세팅이 되어 있어야함)    

-----------------------------------------세팅 방법----------------------------  

a. 품목 준비  
필수 구성품목 : mrp-2000세트(본체, gnss & dmb 안테나, rs232 to usb serial port, 라즈베리파이3(16gb 이상 sd카드포함)  
선택 구성품목 : led, 저항(100~1k 옴)  

구성품목 참조 사이트  
1. http://rtk.mbc.co.kr/en/support/manual-MRP-2000-v1.2.pdf  
2. https://www.devicemart.co.kr/main/index  

---

b. 라즈베리파이 세팅  
b-1 : 라즈베리파이3에 라즈비안(제작자 기준버전 : 2022-04-04-raspios-buster-armhf.img) 설치 후  
home -> pi디렉토리에 파이썬 코드 추가

> touch test_mrp.py  
> https://github.com/Profrog/python_mrp/blob/main/test_mrp.py   
> (코드 내용 복사)  
  
b-2 : 부팅 시 실행할 bash파일 생성
> touch test  
> chmod +x test  
> https://github.com/Profrog/python_mrp/blob/main/test  
> (코드 내용 복사)


b-3 부팅 스크립트 수정
> sudo nano /etc/profile.d/bash_completion.sh  
> "sudo ./test &" 기입  

![image](https://user-images.githubusercontent.com/26535065/182524309-f8955f16-83aa-47b3-90a9-1b92e5efac9f.png)

b-4 블루투스 설정  

> sudo apt-get install bluez libbluetooth-dev pi-bluetooth  
> sudo apt-get install libreadline-gplv2-dev libncursesw5-dev libssl-dev libsqlite3-dev tk-dev libgdbm-dev libc6-dev libbz2-dev  
> sudo sdptool add SP  
> sudo vi /lib/systemd/system/bluetooth.service
 
"ExecStart=/usr/lib/bluetooth/bluetoothd -noplugin=sap" 으로 수정

> sudo systemctl daemon-reload  
> sudo systemctl restart bluetooth  
> sudo systemctl enable bluetooth
> sudo pip3 install pybluez pybleno  
> sudo hciconfig hci0 piscan  

연결하고자 하는 휴대폰에서 라즈베리파이가 인식되면 등록한다  
![image](https://user-images.githubusercontent.com/26535065/182525849-f65b8775-3309-4374-a927-9f92bf20fc2d.png)  


b-5 led세팅(선택)

![image](https://user-images.githubusercontent.com/26535065/182523967-30f8819b-ec3e-4286-9ced-cd02ba5cf6de.png)

led의 긴 방향(+) : 7번핀(GPIO 4)에 연결  
led의 짧은 방향(-) : 저항과 연결 후 저항의 다른쪽 끝을 6번핀(ground)에 연결

![image](https://user-images.githubusercontent.com/26535065/182526211-d17f260b-a0df-4d6d-b3c2-7c339e7e7bcb.png)

---

c 휴대폰 세팅

![image](https://user-images.githubusercontent.com/26535065/185283879-89c41c11-3714-4337-8dd6-5de81dc8eaac.png)


다운로드 : https://onesto.re/0000764474  
https://github.com/Profrog/python_mrp/tree/app_code  

참조


---앱이 미작동시-----

 https://play.google.com/store/apps/details?id=com.crhostservices.com.crhostservices.androidbtcontrol  

해당 앱 설치  
![image](https://user-images.githubusercontent.com/26535065/182527228-3fad81f2-391f-409d-a415-50cf9f1feda5.png)

---

d 작동 방법  

d-1 : mrp-2000에 gnss,dmb 안테나 연결 후 전원 연결  
d-2 : mrp-2000의 정상동작 확인 후 usb시리얼 포트를 세팅 완료된 라즈베리파이에 연결 그 후 라즈베리파이에 전원 연결  
d-3 : 정상적으로 시스템 동작 완료되면 라즈베리파이에 연결된 led에 불이 들어온다  
d-4 : 휴대폰에 설치된 앱을 통해 mrp데이터 값을 받아볼 수 있다.  
https://www.youtube.com/watch?v=sng3KRxmICw
