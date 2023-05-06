package com.springboot.sueulproject.controller;

import com.springboot.sueulproject.entity.*;
import com.springboot.sueulproject.repository.*;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class RestSueulController {
    final DefaultMessageService messageService;
    final MemberRepository memberRe;
    final TasteTagRepository tagRe;
    final DetailRepository detailRe;
    final StarRateBridgeRepository starRateBridgeRe;
    final BookmarkBridgeRepository bookmarkBridgeRe;

    @Autowired
    public RestSueulController(MemberRepository memberRe, TasteTagRepository tagRe, DetailRepository detailRe, StarRateBridgeRepository starRateBridgeRe, BookmarkBridgeRepository bookmarkBridgeRe) {
        this.memberRe = memberRe;
        this.detailRe = detailRe;
        this.starRateBridgeRe = starRateBridgeRe;
        this.bookmarkBridgeRe = bookmarkBridgeRe;
        this.messageService = NurigoApp.INSTANCE.initialize("NCSNXTUAO5TMAGBM", "547E6L3OV0H5SP0HR6LYJWOFJMP19QBD", "https://api.coolsms.co.kr");
        this.tagRe = tagRe;
    }

    @RequestMapping(value = "/idChecking", method = RequestMethod.POST)
    public boolean SignIdChecking(@RequestParam("memberId") String memberId) {
        Optional opUser = memberRe.findById(memberId);
        if (opUser.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/pwChecking", method = RequestMethod.POST)
    public boolean memberPwChecking(@CookieValue(value = "memberId", required = false) String memberId, @RequestParam("memberPw") String memberPw) {
        Member mem = memberRe.findBymemberIdPw(memberId, memberPw);
        if (mem != null) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/smsAuthentication", method = RequestMethod.POST)
    public String smsAuthentication(@RequestParam("memberPn") String memberPn) {
        //인증번호 만들기
        String random = String.valueOf(Math.random());
        random = random.substring(2, 7);
        String smsContent = "[Sueul] 본인인증번호는 [" + random + "]입니다.";

        Message message = new Message();

        //발신번호는 설정해둔것만 가능
        message.setFrom("01072711283");
        message.setTo(memberPn);
        message.setText(smsContent);

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));

        return random;
    }

    @RequestMapping("/nnChecking")
    public boolean nicknameChecking(@RequestParam("nickname") String membernn) {
        Member user = memberRe.findByuserNickname(membernn);
        if (user != null) {
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping("/memberContain")
    public boolean memberSsnChecking(@RequestParam("memberSsn") String memberSsn) {
        Member mem = memberRe.findBymemberSsn(memberSsn);
        if (mem != null) {
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping("/phoneNumContain")
    public boolean memberpnChecking(@RequestParam("memberPn") String memberPn) {
        Member mem = memberRe.findBymemberPn(memberPn);
        if (mem != null) {
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping("/tagSearch")
    public List<TasteTag> tasteTagSearch(@RequestParam("tasteTag") String tasteTag) {
        List<TasteTag> tlst = tagRe.tagFindByName(tasteTag);

        return tlst;
    }

    @RequestMapping("/tagFindAll")
    public List<TasteTag> tasteTagsFindAll() {
        List<TasteTag> tlst = tagRe.findAll();
        return tlst;
    }


    @RequestMapping("/tagSave")
    public List<TasteTag> tagSave(@RequestParam("newTag") List<String> newTag) {
        List<TasteTag> tt = newTag.stream().map(t -> TasteTag.builder().content(t).build()).collect(Collectors.toList());

        List<TasteTag> result = tagRe.saveAll(tt);
        return result;
    }


    @RequestMapping("/detailPaging")
    public List<Detail> detailPaging(@RequestParam("kind") String kind) {
        if (kind.substring(0, 1).equals("t")) {
            int type = Integer.parseInt(kind.substring(1));
            return detailRe.findByType(type);
        } else if (kind.substring(0, 1).equals("o")) {
            int origin = Integer.parseInt(kind.substring(1));
            return detailRe.findByOrigin(origin);
        } else {
            return null;
        }
    }

    @RequestMapping("/starRateSave")
    public boolean starRateSave(@CookieValue(value = "memberId", required = false) String memberId, @RequestParam("did") String did, @RequestParam("score") String score) {
        Long id = Long.parseLong(did.substring(1));
        int rescore = Integer.parseInt(score);

        if (did.substring(0, 1).equals("n")) {
            Detail detail = detailRe.findById(id).orElseThrow();
            Member member = memberRe.findById(memberId).orElseThrow();
            StarRateBridge st = StarRateBridge.builder().detail(detail).member(member).starScore(rescore).build();
            detail.setStarRating(detail.getStarRating()+rescore);
            detail.setStarRateCount(detail.getStarRateCount()+1);
            detailRe.save(detail);
            starRateBridgeRe.save(st);
            return true;
        } else if (did.substring(0, 1).equals("u")) {
            StarRateBridge st = starRateBridgeRe.findByMidDid(memberId,id);
            Detail detail = detailRe.findById(id).orElseThrow();
            detail.setStarRating(detail.getStarRating()- st.getStarScore()+rescore);

            st.setStarScore(rescore);
            detailRe.save(detail);
            starRateBridgeRe.save(st);
            return true;
        }else{
            return false;
        }
    }
    @RequestMapping("/bookmarking")
    public boolean bookmarking(@CookieValue(value = "memberId", required = false) String memberId, @RequestParam("did") String did){
        Long id = Long.parseLong(did.substring(1));
        if(did.substring(0, 1).equals("f")){
            Detail detail = detailRe.findById(id).orElseThrow();
            Member member = memberRe.findById(memberId).orElseThrow();
            detail.setBookmarkCount(detail.getBookmarkCount()+1);

            BookmarkBridge bm = BookmarkBridge.builder().detail(detail).member(member).build();

            detailRe.save(detail);
            bookmarkBridgeRe.save(bm);
            return true;
        }else if(did.substring(0, 1).equals("o")){
            Detail detail = detailRe.findById(id).orElseThrow();
            detail.setBookmarkCount(detail.getBookmarkCount()-1);

            BookmarkBridge bm = bookmarkBridgeRe.findByMidDid(memberId,id);
            detailRe.save(detail);
            bookmarkBridgeRe.delete(bm);

            return true;
        }else{
            return false;
        }
    }
}
