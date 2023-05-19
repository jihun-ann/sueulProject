# Sueul_Project
Mini_Project

<br>
<br>
<br>
<br>
<br>
<br>

## 프로젝트 개요
![스크린샷 2023-05-11 141058](https://github.com/jihun-ann/sueulProject/assets/118144876/29649c20-c584-4445-8a6c-4ee54884512c)
  프로젝트 : Sueul_Project<br><br>
  구성인원 : 1명 (개인 프로젝트)<br><br>
  제작기간 : 23.04.03~23.05.10<br><br>
  배포일 : 23.05.11<br><br>
  주요 기능 : 카테고리별 상품리스트 노출, 상품 북마크 및 별점 기능, 시간과 날씨에 따라 정해 놓은 기준의 맞는 상품을 추천<br><br>
  사용기술 : <br>
  Java - 상품,원산지,종류라는 데이터, 즉 객체간 상호작용을 위하여 사용<br><br>
  JPA - 관계형데이터, JPARepository, QueryDSL(추가예정)등을 사용하기 위하여 사용<br><br>
  SpringBoot - 개발하는데 있어 ConnectionPool, Bean 등등 자동으로 처리해주는 기능들이 있어 편하여 사용<br><br>
  RestAPI - Naver 로그인, Naver 쇼핑 검색 내역으로 사용하였는데, 사용자들이 주로 사용하는 네이버를 통하여 로그인하고 결제할 수 있도록 네이버를 연동하여 해당 정보를 가져오기 유용하여 사용<br><br>
  OpenAPI - 기상청API를 사용하였는데, 현재 날씨와 시간대를 비교하여 어울리는 술을 추천할 수 있도록, 날씨정보를 가져오는데 사용<br><br>
  AWS EC2,RDS - 무료로 가상 컴퓨터를 만들어 배포를 체험해볼 수 있고, AWS 서비스를 많이 사용하는 추세로 보여 일부인 EC2와 RDS를 체험해보고자 사용
  <br><br><br>



 
<br>
<br>
<br>
<br>
<br>
<br>

## 🛠️SKils and Tools

<br>

* BackEnd
  * Java_11
  * SpringBoot_2.7.9
  * JPA
  * MySQL_8.0.32
  * AWS EC2, RDS

* FrontEnd
  * HTML
  * JavaScript
  * JQuery

* Tools
  * IntelliJ
  * PhotoShop

* API
  * Naver 소셜 로그인 / 쇼핑 검색
  * 기상청 API
  * CoolSMS

<br>
<br>
<br>
<br>
<br>
<br>

  ## 🔗링크
  -	서비스 링크 : http://13.209.97.14:7890/

<br>
<br>
<br>
<br>
<br>
<br>

## 기능 구현
#### 사용자 페이지
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/1f55ad6f-aafc-40e0-8a26-f54068c195a0)
<br><br>
#### 최대 10개의 추천 상품리스트를 구현하기 위하여 th:each를 사용하여 필요한 데이터를 반복적으로 뿌려
<br><br><br><br>

![image](https://github.com/jihun-ann/sueulProject/assets/118144876/7496c92c-867b-4638-b161-05935617a5fd)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/4373957b-1d49-47ac-8634-a3e68812a1c6)
<br><br>

#### JQuery를 사용하여 keyup시 입력된 값을 ajax로 넘겨 해당 아이디의 유무를 체크
##### keydown이 아닌 up을 쓴 이유는 down시 해당 키의 입력값이 들어가기 전이기때문
<br><br><br><br>
#### 휴대폰 인증버튼을 눌렀을때 해당 번호가 가입되어있는지 체크 후, 가입되어 있지 않다면 인증절차가 진행되도록 구현

<br><br><br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/1d7bbd9e-d2d5-49b6-93c6-93d33217a128)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/33ba8827-d73c-4ba7-84e8-3841816ca68a)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/db1b3ce8-7c35-4817-aa76-7e0c56f1b0fd)
<br><br>
#### pathValue를 이용하여 페이지의 들어갈 종류별로 리스트화 하도록 구현
##### 주종을 누르면 t가 붙고, 지역을 누르면 o, 상품태그를 누르면 b, 북마크를 누르면 m가 붙게 만들어서 해당 문자의 따라 불러오는 상품의 종류들이 달라지고 이를 받는 페이지를 하나만 두어 반복적으로 사용되는 코드를 
<br><br>
<br><br>

#### 해당 상품리스트의 스크롤 마지막에 도착하게 되면 그 다음 리스트가 append되도록 
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/71bc75f0-8027-42d7-aa6e-2e772a026c9d)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/652c71a9-4b42-4ad5-8010-75b72eaaaf5c)
<br><br>

####상품의 이름으로 네이버 쇼핑의 검색한 결과값을 반환 받는 기능을 구현
<br><br>
<br><br>

## 관리자 페이지
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/ddc0c499-8942-42ae-8570-cc02690627a7)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/deee7702-d652-4fba-b1c4-5ebda8271a4d)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/367a94f0-205b-48a4-88bd-c359a8295281)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/8f219b4b-ab88-46ab-b5a8-6e3eab88648b)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/7919c8b2-a3cf-43b0-92aa-da3e34a34102)
