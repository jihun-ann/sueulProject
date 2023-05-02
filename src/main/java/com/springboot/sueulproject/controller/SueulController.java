package com.springboot.sueulproject.controller;

import com.springboot.sueulproject.entity.*;
import com.springboot.sueulproject.repository.*;
import com.springboot.sueulproject.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class SueulController {

    final DetailRepository detailRe;
    final OriginRepository originRe;
    final TypeRepository typeRe;
    final MemberRepository memberRe;
    final TasteTagRepository tasteTagRe;
    final DetailService detailService;
    final TagBridgeRepository tagBridgeRe;
    final BookmarkBridgeRepository bookmarkBridgeRe;
    final StarRateBridgeRepository starRateBridgeRe;

    @Autowired
    public SueulController(DetailRepository detailRe, OriginRepository originRe, TypeRepository typeRe, MemberRepository memberRe, TasteTagRepository tasteTagRe, DetailService detailService, TagBridgeRepository tagBridgeRe, BookmarkBridgeRepository bookmarkBridgeRe, StarRateBridgeRepository starRateBridgeRe) {
        this.detailRe = detailRe;
        this.originRe = originRe;
        this.typeRe = typeRe;
        this.memberRe = memberRe;
        this.tasteTagRe = tasteTagRe;
        this.detailService = detailService;
        this.tagBridgeRe = tagBridgeRe;
        this.bookmarkBridgeRe = bookmarkBridgeRe;
        this.starRateBridgeRe = starRateBridgeRe;
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

        //List<Detail> detailList = detailRe.findAll();
        //mo.addAttribute("detailList",detailList);
        return "views/index";

    }

    @GetMapping("/logIn")
    public String login(@CookieValue(value = "memberId", required = false)String memberId, Model mo){
        if(memberId != null) {
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "views/index";
        }else{
            return "views/signIn";
        }
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
                List<TasteTag> ttlst = tasteTagRe.findAll();
                mo.addAttribute("member",member);
                mo.addAttribute("olst",olst);
                mo.addAttribute("tlst",tlst);
                mo.addAttribute("ttlst",ttlst);
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
        Detail reqDetail = detailRe.save(detail);

        List<TagBridge> tlst = tagId.stream().map(t -> {
            Long l = Long.parseLong(t);
            TasteTag tt = tasteTagRe.findById(l).orElseThrow();
            TagBridge tb = TagBridge.builder().detail(reqDetail).tasteTag(tt).build();
            return tb;
        }).collect(Collectors.toList());
        reqDetail.getTbList().addAll(tlst);
        detailRe.save(reqDetail);

        detailService.multipartFileSave(img,reqDetail.getDid());


        return "views/admin/detailAdd";
    }


    @GetMapping("/detailList/{kind}")
    public String detailList(@CookieValue(value = "memberId", required = false)String memberId,@PathVariable("kind")String kind, Model mo) {
        List<Detail> dtlst;
        if(kind.substring(0,1).equals("t")){
            int type = Integer.parseInt(kind.substring(1));
            dtlst = detailRe.findByType(type);
            Type tinfo = typeRe.findById(type).orElseThrow();
            mo.addAttribute("detailList",dtlst);
            mo.addAttribute("kind",kind);
            mo.addAttribute("kindInfo",tinfo);
        }else if(kind.substring(0,1).equals("o")){
            int origin = Integer.parseInt(kind.substring(1));
            dtlst = detailRe.findByOrigin(origin);
            Origin oinfo = originRe.findById(origin).orElseThrow();
            mo.addAttribute("detailList",dtlst);
            mo.addAttribute("kind",kind);
            mo.addAttribute("kindInfo",oinfo);
        }else{
            System.out.println("%%%%%%%%%실패%%%%");
        }


        if(memberId != null){
            Member member = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",member);
        }
        return "views/detailList";
    }

    @GetMapping("/detailInfo/{did}")
    public String detailInfo(@PathVariable("did")Long did, Model mo,@CookieValue(value = "memberId", required = false)String memberId){
        Optional optional = detailRe.findById(did);
        if(optional.isEmpty()){
            return "views/redirect";
        }else {
            Detail detail = (Detail) optional.orElseThrow();
            mo.addAttribute("detail",detail);

            if(memberId != null){
                Member member = memberRe.findById(memberId).orElseThrow();
                BookmarkBridge bm = bookmarkBridgeRe.findByMidDid(memberId,did);
                StarRateBridge st = starRateBridgeRe.findByMidDid(memberId,did);
                mo.addAttribute("bookmark",bm);
                mo.addAttribute("starRate",st);
                mo.addAttribute("memberRole",member.getRole());
                mo.addAttribute("member",member);
            }else{
                mo.addAttribute("memberRole","nothing");
            }
            return "views/detailInfo";
        }
    }
    @GetMapping("/admin/detailEdit/{did}")
    public String detailEdit(@CookieValue(value = "memberId", required = false)String memberId,@PathVariable("did")Long did, Model mo){
        if(memberId == null){
            return "views/signIn";
        }else{
            Member member = memberRe.findById(memberId).orElseThrow();
            if(member.getRole().equals("admin")){
                if (did != null) {
                    Detail detail = detailRe.findById(did).orElseThrow();
                    List<Origin> olst = originRe.findAll();
                    List<Type> tlst = typeRe.findAll();
                    List<TasteTag> ttlst = tasteTagRe.findAll();
                    List<TasteTag> selectedttlst = tagBridgeRe.findByDetailId(did);

                    mo.addAttribute("detail",detail);
                    mo.addAttribute("member",member);
                    mo.addAttribute("olst",olst);
                    mo.addAttribute("tlst",tlst);
                    mo.addAttribute("ttlst",ttlst);
                    mo.addAttribute("tblst",selectedttlst);
                    return "views/admin/detailEdit";
                }else{
                    mo.addAttribute("alert","불완전한 접근입니다.다시 한번 시도하여 주세요.");
                    mo.addAttribute("/");
                    return "views/alert";
                }
            }else{
                mo.addAttribute("alert","권한이 없습니다.");
                mo.addAttribute("/");
                return "views/alert";
            }
        }
    }
}
