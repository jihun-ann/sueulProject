package com.springboot.sueulproject.DTO;

import lombok.Data;

import java.util.List;

@Data
public class NaverShopingMapDTO {
        public String lastBuildDate;
        public Integer total;
        public Integer start;
        public Integer display;
        public List<Item> items;


    @Data
    public static class Item {

        public String title;
        public String link;
        public String image;
        public String lprice;
        public String hprice;
        public String mallName;
        public String productId;
        public String productType;
        public String brand;
        public String maker;
        public String category1;
        public String category2;
        public String category3;
        public String category4;

    }
    }
