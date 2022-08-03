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

-----------------------------------------코드 설명----------------------------  


test_mrp.py : n분간 $GNGLL 포멧으로 들어온 경도, 위도 분석   
map_mrp.py : test_mrp.py의 데이터로 map

실험영상 : https://www.youtube.com/watch?v=fOSTibGu1l0

-----------------------------------------세팅 방법----------------------------  

a. 품목 준비
필수 구성품목 : mrp-2000세트(본체, gnss & dmb 안테나, rs232 to usb serial port, 라즈베리파이3(16gb 이상 sd카드포함)  
선택 구성품목 : led, 저항(100~1k 옴)  

구성품목 참조 사이트  
1. http://rtk.mbc.co.kr/en/support/manual-MRP-2000-v1.2.pdf  
2. https://www.devicemart.co.kr/main/index  


b. 코드세팅

















