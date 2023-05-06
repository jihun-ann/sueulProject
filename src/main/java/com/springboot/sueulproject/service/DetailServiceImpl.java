package com.springboot.sueulproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.sueulproject.profile.WeatherVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        Date today = new Date();
        today.setTime(today.getTime()-1800000);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        String todayString = df.format(today);

        String todayDate = todayString.substring(0,8);
        String todayHoure = todayString.substring(8)+"00";

        String serviceKey = "5v6gumTVFlXEMr%2BBcExFwR1pZ2rlrJgL3mzhi4IOPaKXNexoPJ4EoN214w8k2ph%2FbDN9I2uN26uxyuBzSJVAag%3D%3D";

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

        List<WeatherVO.Item> itemLst = requestWeatherVO.getResponse().getBody().getItems().getItem().stream()
                .filter(t -> "SKY".equals(t.getCategory()) || "PTY".equals(t.getCategory())).collect(Collectors.toList());

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
                        case "2" : t2++; break; //비&눈 - 청주
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
                int result = o2.getValue().toString().compareTo(o1.getValue().toString());
                return result;
            }
        });

        return mlst.subList(0,2);
    }
}
