package com.springboot.sueulproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.sueulproject.DTO.NaverShopingMapDTO;
import com.springboot.sueulproject.profile.WeatherVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetailServiceImpl implements DetailService{

    @Value("src/main/resources/static/detail")
    private String uploadPath;

    @Override
    public void multipartFileSave(MultipartFile img,Long detailId) {
        String fileName = uploadPath + File.separator + detailId +".png";
        Path savePath = Paths.get(fileName);

        try {
            img.transferTo(savePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
}
