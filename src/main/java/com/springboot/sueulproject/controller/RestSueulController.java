package com.springboot.sueulproject.controller;

import com.springboot.sueulproject.entity.Member;
import com.springboot.sueulproject.entity.TasteTag;
import com.springboot.sueulproject.repository.MemberRepository;
import com.springboot.sueulproject.repository.TasteTagRepository;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
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
    @Autowired
    public RestSueulController(MemberRepository memberRe,TasteTagRepository tagRe) {
        this.memberRe = memberRe;
        this.messageService = NurigoApp.INSTANCE.initialize("NCSNXTUAO5TMAGBM","547E6L3OV0H5SP0HR6LYJWOFJMP19QBD","https://api.coolsms.co.kr");
        this.tagRe = tagRe;
    }

    @RequestMapping(value="/idChecking", method = RequestMethod.POST)
    public boolean SignIdChecking(@RequestParam("memberId") String memberId){
        Optional opUser = memberRe.findById(memberId);
        if(opUser.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    @RequestMapping(value = "/pwChecking", method = RequestMethod.POST)
    public boolean memberPwChecking(@CookieValue(value = "memberId", required = false)String memberId, @RequestParam("memberPw") String memberPw){
        Member mem = memberRe.findBymemberIdPw(memberId,memberPw);
        if(mem != null){
            return true;
        }else{
            return false;
        }
    }

    @RequestMapping(value="/smsAuthentication",method = RequestMethod.POST)
    public String smsAuthentication(@RequestParam("memberPn")String memberPn){
        //인증번호 만들기
        String random = String.valueOf(Math.random());
        random = random.substring(2,7);
        String smsContent = "[Sueul] 본인인증번호는 ["+random+"]입니다.";

        Message message = new Message();

        //발신번호는 설정해둔것만 가능
        message.setFrom("01072711283");
        message.setTo(memberPn);
        message.setText(smsContent);

        //SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));

        return random;
    }

    @RequestMapping("/nnChecking")
    public boolean nicknameChecking(@RequestParam("nickname")String membernn){
        Member user = memberRe.findByuserNickname(membernn);
        if (user != null){
            return false;
        }else{
            return true;
        }
    }

    @RequestMapping("/memberContain")
    public boolean memberSsnChecking(@RequestParam("memberSsn")String memberSsn){
        Member mem = memberRe.findBymemberSsn(memberSsn);
        if(mem != null){
            return false;
        }else {
            return true;
        }
    }

    @RequestMapping("/phoneNumContain")
    public boolean memberpnChecking(@RequestParam("memberPn")String memberPn){
        Member mem = memberRe.findBymemberPn(memberPn);
        if(mem != null){
            return false;
        }else {
            return true;
        }
    }

    @RequestMapping("/tagSearch")
    public List<TasteTag> tasteTagSearch(@RequestParam("tasteTag")String tasteTag) {
        List<TasteTag> tlst = tagRe.tagFindByName(tasteTag);

        return tlst;
    }

    @RequestMapping("/tagFindAll")
    public List<TasteTag> tasteTagsFindAll(){
        List<TasteTag> tlst = tagRe.findAll();
        System.out.println("lst@@@@@@@@@"+tlst);
        return tlst;
    }


    @RequestMapping("/tagSave")
    public List<TasteTag> tagSave(@RequestParam("newTag") List<String> newTag){
        List<TasteTag> tt =  newTag.stream().map(t -> TasteTag.builder().content(t).build()).collect(Collectors.toList());
        System.out.println("stream>>>>>>>>>"+tt);

        List<TasteTag> result = tagRe.saveAll(tt);
        System.out.println("result"+result);
        return result;
    }


}
