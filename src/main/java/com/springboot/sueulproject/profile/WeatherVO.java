package com.springboot.sueulproject.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherVO {
    public Response response;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        public Header header;
        public Body body;

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {

        public String resultCode;
        public String resultMsg;

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Body {

        public String dataType;
        public Items items;
        public Integer pageNo;
        public Integer numOfRows;
        public Integer totalCount;

    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        public String baseDate;
        public String baseTime;
        public String category;
        public String fcstDate;
        public String fcstTime;
        public String fcstValue;
        public Integer nx;
        public Integer ny;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Items {

        public List<Item> item;

    }

}
