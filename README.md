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
  사용기술 : SpringBoot, RestAPI, OpenAPI, AWS EC2,RDS<br><br>



 
<br>
<br>
<br>
<br>
<br>
<br>
##  🛠️SKils and Tools
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

```
 @Override
    public List<Map.Entry> todayWeather() {
        //현재의 시간의 30분전(밀리세컨드 기준)인 1800000을 빼준 시간을 yyyyMMddHH 형식으로 저장
        //기상청의 날씨 Update 시간이 매 시간 정각마다가 아닌 매시간의 30분마다 Update되기 때문에 30분 이전엔 해당 날씨가 조회되지 않아
        //30분이 되기 전이라면 30분을 빼주어 이전 시간대 기준으로 날씨를 조회하고, 30분이 지났다면 현재 시간대로 날씨를 조회
        Date today = new Date();
        today.setTime(today.getTime()-1800000);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        String todayString = df.format(today);

        String todayDate = todayString.substring(0,8); //"yyyyMMdd""만 사용할 수 있도록 만듦
        String todayHoure = todayString.substring(8)+"00"; // 반환받은 시간대를 갖고 ""HHmm"을 만듦

        String serviceKey = "5v6gumTVFlXEMr%2BBcExFwR1pZ2rlrJgL3mzhi4IOPaKXNexoPJ4EoN214w8k2ph%2FbDN9I2uN26uxyuBzSJVAag%3D%3D";

        // get방식으로 진행될 OpenAPI로 url이 될 부분들의 정보를 담아줌
        String urlString = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"
                + "?serviceKey="+serviceKey+"&numOfRows=100&pageNo=1&dataType=json"
                + "&base_date="+todayDate+"&base_time="+todayHoure+"&nx=57&ny=127";

        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String result = "";
        BufferedReader bf;
        try {
            //openStream 메서드로 해당 URL의 사이트 정보를 읽어올 수 있다.
            //url.openStream 이외에 HttpURLConnection을 사용하는 방법도 있으나,
            //HttpURLConnection은 요청방법이나 요청헤더, 응답코드같은 추가적인 설정이 가능하나
            //해당 기능은 심플하게 데이터만 가져오면 되는 상황이다 보니 더욱 보기 쉽고 직관적인 openStream을 사용하였다.
            bf = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            result = bf.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper obmapper = new ObjectMapper();
        WeatherVO requestWeatherVO;
        try {
            requestWeatherVO = obmapper.readValue(result, WeatherVO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //받아온 requestWeatherVO 안에 있는 items중 필요한 SKY, PTY만 List로 꺼내는 메서드
        List<WeatherVO.Item> itemLst = requestWeatherVO.getResponse().getBody().getItems().getItem().stream()
                .filter(t -> "SKY".equals(t.getCategory()) || "PTY".equals(t.getCategory())).collect(Collectors.toList());

        //타입(주종)
        int t1 = 0;
        int t2 = 0;
        int t3 = 0;
        int t4 = 0;
        int t5 = 0;

        int nowhoure = Integer.parseInt(todayHoure.substring(0,2));

        for (int i = 0; i <itemLst.size() ; i++) {
            String type = itemLst.get(i).getCategory();
            String test = itemLst.get(i).getFcstValue();
            if (6<=nowhoure && nowhoure<18){
                //System.out.println("낮 - 아침 6~저녁5:59까지");

                if(type.equals("SKY")){
                    switch (test){
                        case "1" : t2++; break; //맑음 - 청주
                        case "3" : t5++; break; //구름많음 - 증류주
                        case "4" : t1++; break; //흐림 - 탁주
                        default  : break;
                    }
                }else if(type.equals("PTY")){
                    switch (test){
                        case "0" : t2++; t5++; break; //없음 - 청주,증류주
                        case "1" : t1++; break; //비 - 탁주
                        case "2" : t1++; t4++; break; //비&눈 - 탁주, 과실주
                        case "3" : t4++; break; //눈 - 과실주
                        case "5" : t1++; break; //빗방울 - 탁주
                        case "6" : t1++; t4++;break; //빗방울&눈날림 - 탁주,과실주
                        case "7" : t4++; break; //눈날림 - 과실주
                        default  : break;
                    }
                }
            }else{
                //System.out.println("밤 - 저녁 6~아침5:59까지");
                if(type.equals("SKY")){
                    switch (test){
                        case "1" : t4++; break; //맑음 - 과실주
                        case "3" : t3++; break; //구름많음 - 약주
                        case "4" : t1++; break; //흐림 - 탁주
                        default  : break;
                    }
                }else if(type.equals("PTY")){
                    switch (test){
                        case "0" : t3++; t4++; break; //없음 - 과실주, 약주
                        case "1" : t1++; break; //비 - 탁주
                        case "2" : t5++; break; //비&눈 - 증류주
                        case "3" : t4++; break; //눈 - 과실주
                        case "5" : t1++; break; //빗방울 - 탁주
                        case "6" : t5++; break; //빗방울&눈날림 - 증류주
                        case "7" : t4++; break; //눈날림 - 과실주
                        default  : break;
                    }
                }
            }
        }

        Map<String,Integer> typeMap = new HashMap<>();
        typeMap.put("t1",t1);
        typeMap.put("t2",t2);
        typeMap.put("t3",t3);
        typeMap.put("t4",t4);
        typeMap.put("t5",t5);

        List<Map.Entry> mlst = new ArrayList<>(typeMap.entrySet());

        System.out.println(mlst);
        mlst.stream().forEach(t -> System.out.println(t));
        Collections.sort(mlst, new Comparator<Map.Entry>() {
            @Override
            public int compare(Map.Entry o1, Map.Entry o2) {
                // o2.value와 o1.value를 비교하여 높은수가 왼쪽으로 올 수 있도로 정렬
                int result = o2.getValue().toString().compareTo(o1.getValue().toString());
                return result;
            }
        });

        return mlst.subList(0,2);
    }

<div id="itemImgsList-wrapper">
  <div th:each="detail : ${detailList}"class="slide-img" th:value="${detail.getDid()}">
    <div class="itemImg" th:value="'/Detail/'+${detail.getDid()}+'.png'"></div>
    <span th:text="${detail.getName()}"></span>
  </div>
</div>
```

#### 최대 10개의 추천 상품리스트를 구현하기 위하여 th:each를 사용하여 필요한 데이터를 반복적으로 뿌려
<br><br><br><br>

![image](https://github.com/jihun-ann/sueulProject/assets/118144876/7496c92c-867b-4638-b161-05935617a5fd)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/4373957b-1d49-47ac-8634-a3e68812a1c6)
<br><br>
```
$(document).keyup(function (e) {
			if (e.target.getAttribute('name') == 'memberId') {
				let memberId = $('input[name="memberId"]').val();
				if (memberId == "" || memberId == null) {
					$('#idCheck').text("아이디를 입력해주세요");
					$('#idCheck').css({'font-size': '18px'});
					$('#idC').val('false');
				} else {
					$.ajax({
						url     : "/idChecking",
						type    : "post",
						dataType: "JSON",
						data    : {"memberId": memberId},
						success : function (e) {

							if (e == true) {
								$('#idCheck').text("사용가능한 아이디입니다");
								$('#idCheck').css({'font-size': '18px'});
								$('#idC').val('true');
							} else {
								$('#idCheck').text("중복되는 아이디입니다");
								$('#idCheck').css({'font-size': '18px'});
								$('#idC').val('false');
							}
						}, error: function (e) {
							console.log(e);
						}
					})
				}
```
#### JQuery를 사용하여 keyup시 입력된 값을 ajax로 넘겨 해당 아이디의 유무를 체크
##### keydown이 아닌 up을 쓴 이유는 down시 해당 키의 입력값이 들어가기 전이기때문
<br><br><br><br>
```
if(pn.length == 3 || pn2.length == 4 || pn3.length == 4) {
				let pnc = pn + pn2 + pn3;
				$('.modal-pnC').val(pnc);
				$('.sms-modal-wrap').css({'display': 'inline'})

				$('.sendsms').click(function () {
					let pnsubc = $('.modal-pnC').val();
					pnsubc = pnsubc.substring(0,3)+"-"+pnsubc.substring(3,7)+"-"+pnsubc.substring(7,11);

					$.ajax({
            url: "/phoneNumContain",
            type: "post",
            dataType : "JSON",
            data: {"memberPn":pnsubc},
            success: function (e){
							if (e == true) {
								$.ajax({
									url     : "/smsAuthentication",
									type    : "post",
									dataType: "JSON",
									data    : {"memberPn": pnc},
									success : function (e) {
										smsRandomnum = e;
										//console.log(e);
									}
								})
								$('.sendsms').val("재인증받기");
								$('.modal-pnC').prop('readonly',true);
							} else {
                alert("이미 가입된 번호입니다.");
								$('#pnCheck').text("이미 가입된 번호입니다");
								$('#pnCheck').css({'font-size': '18px'});
								$('#pnC').val('false');
							}
            }
          })
				})
				$('.sendsmsC').click(function () {
					if ($('.modal-pnC').val()!="" && $('.modal-smsC').val() == smsRandomnum) {
						$('#pnCheck').text("휴대폰 인증이 되었습니다.");
						$('#pnCheck').css({'font-size': '18px'});
						$('#pnC').val('true');
						$('#pnChecking').css({'display':'none'});

						let inputpn = $('.modal-pnC').val();
						let pn1 = inputpn.substring(0, 3);
						let pn2 = inputpn.substring(3,7);
						let pn3 = inputpn.substring(7,11);

            $('input[name=memberPhoneNum]').val(pn1+'-'+pn2+'-'+pn3);
						$('input[name=memberPn]').val(pn1);
						$('input[name=memberPn2]').val(pn2);
						$('input[name=memberPn3]').val(pn3);
						$('.modal-wrapper').css({'display': 'none'});
						$('.userPhoneNum').prop('readonly',true);
					} else {
						alert("다시 확인해주세요");
					}
				})
			}else{
					$('.alert-modal-wrap').css({'display':'inline'});
					$('.alertText').text('휴대폰 번호를 입력해주세요');
				}
		})
    
     @RequestMapping(value = "/smsAuthentication", method = RequestMethod.POST)
    public String smsAuthentication(@RequestParam("memberPn") String memberPn) {
        //인증번호 만들기
        //random값은 소수로 발생하기 떄문에 소수점 다음 숫자부터 필요한 숫자 5자리를 구할 수 있도록 substring
        String random = String.valueOf(Math.random());
        random = random.substring(2, 7);
        String smsContent = "[Sueul] 본인인증번호는 [" + random + "]입니다.";

        Message message = new Message();

        //발신번호는 설정해둔것만 가능
        message.setFrom("01072711283");
        message.setTo(memberPn);
        message.setText(smsContent);

        //coolsms의 api인 NurigoApp를 gradle로 등록해두어 사용
        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));

        //발송되고 나면 인증시 비교할 수 있도록 random값을 반환
        return random;
    }
```
#### 휴대폰 인증버튼을 눌렀을때 해당 번호가 가입되어있는지 체크 후, 가입되어 있지 않다면 인증절차가 진행되도록 구현

<br><br><br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/1d7bbd9e-d2d5-49b6-93c6-93d33217a128)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/33ba8827-d73c-4ba7-84e8-3841816ca68a)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/db1b3ce8-7c35-4817-aa76-7e0c56f1b0fd)
<br><br>
```
 @GetMapping("/detailList/{kind}")
    public String detailList(
            @CookieValue(value = "memberId", required = false)String memberId,
            @PathVariable("kind")String kind,
            Model mo) {

        List<Detail> dtlst;
        if(kind.substring(0,1).equals("t")){
            int type = Integer.parseInt(kind.substring(1));
            dtlst = detailRe.findByType(type);
            Type tinfo = typeRe.findById(type).orElseThrow();
            mo.addAttribute("detailList",dtlst);
            mo.addAttribute("kind",kind);
            mo.addAttribute("kindInfo",tinfo);
        }else if(kind.substring(0,1).equals("o")) {
            int origin = Integer.parseInt(kind.substring(1));
            dtlst = detailRe.findByOrigin(origin);
            Origin oinfo = originRe.findById(origin).orElseThrow();
            mo.addAttribute("detailList", dtlst);
            mo.addAttribute("kind", kind);
            mo.addAttribute("kindInfo", oinfo);
        }else if(kind.substring(0,1).equals("b")){
            Long tag = Long.parseLong(kind.substring(1));
            dtlst = detailRe.findByTasteTag(tag);
            TasteTag tb = tasteTagRe.tagFindByIdOne(tag);
            mo.addAttribute("detailList",dtlst);
            mo.addAttribute("kind",kind);
            mo.addAttribute("tasteTag",tb);
        }else if(kind.substring(0,1).equals("m")){
            if(memberId != null){
            dtlst = detailRe.bookmarkFindByMemberId(memberId);
            mo.addAttribute("detailList",dtlst);
            mo.addAttribute("kind",kind);
            }else{
                return "views/signIn";
            }
        }else{
            System.out.println("%%%%%%%%%실패%%%%");
        }
        
        if(memberId != null){
            Member member = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",member);
        }
        return "views/detailList";
    }
```
#### pathValue를 이용하여 페이지의 들어갈 종류별로 리스트화 하도록 구현
##### 주종을 누르면 t가 붙고, 지역을 누르면 o, 상품태그를 누르면 b, 북마크를 누르면 m가 붙게 만들어서 해당 문자의 따라 불러오는 상품의 종류들이 달라지고 이를 받는 페이지를 하나만 두어 반복적으로 사용되는 코드를 
<br><br>
<br><br>
```
//상품 리스트에서 어떤 종류를 선택했는지 담아 두었는데 이는 페이징처리를 위하여 사용
<input type="hidden" name="kind" th:value="${kind}"/>


  let nowpage = 1;
	let kind = $('input[name=kind]').val();

$(window).scroll(function (){
		let nowHeight = $(window).scrollTop();
		let lastHeight = $(document).height()-$(window).height();

		if(nowHeight == lastHeight){
			nowpage++;

      //상품의 수는 8개씩 보여지기 위하여 *8을 진행
      //해당 kind를 갖고 JSON을 하여 리스트를 다시 받고 해당 페이지의 맞는 순서에 있는 상품들을 html방식으로 append
			let startP = (nowpage-1)*8;
			let endP = (nowpage*8)-1;
			$.ajax({
        url:"/detailPaging",
        data:{"kind":kind},
        dataType:"JSON",
        type:"post",
        success:function (e){
					endP = e.length < endP? e.length:endP;
					let currentItem = $('.simpleDetail-wrap').length;
					let result = "";

					if(currentItem == e.length){
						//alert("마지막")
						result += "<div class='lastDetail'><span>마지막 상품입니다.</span></div>"
            $(window).off('scroll');
          }else{
            for (let i = startP; i < endP; i++) {
							let eDid = Number(e[i].did);
              result += "<div class='simpleDetail-wrap'><div class='detailImg-wrap simpleDetail'><img class='detailImg' src=\"/Detail/"+e[i].did+".png\" name='"+eDid+"' /></div><span class='detailFont simpleDetail' name='"+eDid+"'>"+e[i].name+"</span></div>"
            }
          }
            $('#detailList-wrap').append(result);
        }
      })
    }
  })
```
#### 해당 상품리스트의 스크롤 마지막에 도착하게 되면 그 다음 리스트가 append되도록 
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/71bc75f0-8027-42d7-aa6e-2e772a026c9d)
<br><br>
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/652c71a9-4b42-4ad5-8010-75b72eaaaf5c)
<br><br>
```
public NaverShopingMapDTO naverShopingSearch(String detailname){
	// 네이버로그인을 구현했을때 방식은 RestTemplate을 사용하여 post하는 방법을 채택했는데,
	// 생각했을때 로그인은 동기식으로 순차적으로 처리되어야 해당 필요한 모든 회원 정보가 필요하다 생각했고,
	// 네이버쇼핑의 경우 중요하지 않은 정보라 생각하여 순차적으로 처리해야할 필요가 없다고 판단하였으며, resttemplate이외에 기능으로 구현하고 싶어 시도하게 되었다.
	
        WebClient webClient = WebClient.create();

        String result = webClient.get().uri("https://openapi.naver.com/v1/search/shop.json?query="+detailname+"&display=3")
                .header("X-Naver-Client-Id","PECAHnIiw17IlqkcNrm7")
                .header("X-Naver-Client-Secret","MET2Ze1XgJ")
                .retrieve().bodyToMono(String.class).block();

        System.out.println(">>>>>>"+result);

        ObjectMapper objectMapper = new ObjectMapper();
        NaverShopingMapDTO shopmap;
        try {
            shopmap = objectMapper.readValue(result, NaverShopingMapDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return shopmap;
    }
```
####상품의 이름으로 네이버 쇼핑의 검색한 결과값을 반환 받는 기능을 구현
<br><br>
<br><br>

## 관리자 페이지
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/ddc0c499-8942-42ae-8570-cc02690627a7)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/deee7702-d652-4fba-b1c4-5ebda8271a4d)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/367a94f0-205b-48a4-88bd-c359a8295281)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/8f219b4b-ab88-46ab-b5a8-6e3eab88648b)
![image](https://github.com/jihun-ann/sueulProject/assets/118144876/7919c8b2-a3cf-43b0-92aa-da3e34a34102)
