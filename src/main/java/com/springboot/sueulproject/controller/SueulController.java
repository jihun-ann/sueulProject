package com.springboot.sueulproject.controller;

import com.springboot.sueulproject.entity.*;
import com.springboot.sueulproject.repository.*;
import com.springboot.sueulproject.service.DetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class SueulController {

    final DetailRepository detailRe;
    final OriginRepository originRe;
    final TypeRepository typeRe;
    final MemberRepository memberRe;
    final TasteTagRepository tasteTagRe;
    final DetailServiceImpl detailService;

    @Autowired
    public SueulController(DetailRepository detailRe, OriginRepository originRe, TypeRepository typeRe, MemberRepository memberRe, TasteTagRepository tasteTagRe, DetailServiceImpl detailService) {
        this.detailRe = detailRe;
        this.originRe = originRe;
        this.typeRe = typeRe;
        this.memberRe = memberRe;
        this.tasteTagRe = tasteTagRe;
        this.detailService = detailService;
    }

    @GetMapping("/")
    public String root(@CookieValue(value = "memberId", required = false)String memberId, Model mo){

        if(memberId != null){
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
        }

        List<Integer> alcoholCategory = new ArrayList<>();
        IntStream.rangeClosed(0,15).forEach(t -> alcoholCategory.add(t));
        mo.addAttribute("todaysAlcohol",alcoholCategory);
        //return "views/index";

        List<Origin> olst = originRe.findAll();
        List<Type> tlst = typeRe.findAll();
        List<TasteTag> ttlst = tasteTagRe.findAll();
        mo.addAttribute("olst",olst);
        mo.addAttribute("tlst",tlst);
        mo.addAttribute("ttlst",ttlst);
        return "views/admin/detailAdd";
    }

    @GetMapping("/logIn")
    public String login(){
        return "views/signIn";
    }

    @GetMapping("/admin/detailAdd")
    public String detailAdd(@CookieValue(value = "memberId", required = false)String memberId, Model mo){
        if(memberId == null){
           return "views/signIn";
        }else{
            Member member = memberRe.findById(memberId).orElseThrow();
            System.out.println(member);
            if(member.getRole().equals("admin")){
                List<Origin> olst = originRe.findAll();
                List<Type> tlst = typeRe.findAll();
                mo.addAttribute("olst",olst);
                mo.addAttribute("tlst",tlst);
                return "views/admin/detailAdd";
            }else{
                mo.addAttribute("alert","권한이 없습니다.");
                mo.addAttribute("/");
                return "views/alert";
            }
        }
    }

    @PostMapping("/detailSave")
    public String detailSave (Detail detail, MultipartFile img, @RequestParam("tags") List<String> tagId){
        System.out.println("도착????????????????");
        Detail reqDetail = detailRe.save(detail);
        System.out.println("detail>>>>>"+reqDetail);

        List<TagBridge> tlst = tagId.stream().map(t -> {
            Long l = Long.parseLong(t);
            TasteTag tt = tasteTagRe.findById(l).orElseThrow();
            TagBridge tb = TagBridge.builder().detail(reqDetail).tasteTag(tt).build();
            return tb;
        }).collect(Collectors.toList());
        System.out.println("tlst>>>>>"+tlst);
        detailRe.save(reqDetail);

        detailService.multipartFileSave(img,reqDetail.getDid());
        System.out.println("img"+img.getOriginalFilename());


        return null;
    }


    @GetMapping("/test")
    public void test(){

    }
}
