package com.springboot.sueulproject.controller;

import com.springboot.sueulproject.entity.Member;
import com.springboot.sueulproject.repository.MemberRepository;
import com.springboot.sueulproject.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class MemberController {
    final MemberRepository memberRe;
    final MemberService memberService;

    @Autowired
    public MemberController(MemberRepository memberRe, MemberService memberService) {
        this.memberRe = memberRe;
        this.memberService = memberService;
    }

    @GetMapping("/signIn")
    public String signIn(@CookieValue(value = "memberId",required = false)String memberId,Model mo){
        if(memberId != null){
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "redirect:/";
        }else{
            return "views/signIn";
        }
    }

    @PostMapping("/signIn")
    public String userSignIn(Member member, @RequestParam(value="cookieSave") String check, Model mo, HttpServletResponse response) {
        Optional opUser = memberRe.findById(member.getMemberId());
        if (opUser.isEmpty()) {
            mo.addAttribute("alert", "아이디를 확인해주세요.");
            mo.addAttribute("url", "/signIn");
            return "views/alert";
        } else {
            Member hasUser = memberRe.findById(member.getMemberId()).orElseThrow();
            if (!hasUser.getMemberPw().equals(member.getMemberPw())) {
                mo.addAttribute("alert", "비밀번호를 확인해주세요.");
                mo.addAttribute("url", "/signIn");
                return "views/alert";
            } else {
                Cookie cookie = memberService.cookieSet(hasUser.getMemberId(),check);
                response.addCookie(cookie);
                mo.addAttribute("member", member);
                return "redirect:/";
            }
        }
    }

    @GetMapping("/signOut")
    public String signOut(HttpServletResponse response,Model mo){
        Cookie cookie = memberService.cookieRemove();
        response.addCookie(cookie);
        mo.addAttribute("member",null);
        return "redirect:/";
    }
    @GetMapping("/signUp")
    public String signUp(){
        return "views/signUp";
    }

    @PostMapping("/signUp")
    public String signUpsueul(Member member,HttpServletResponse response,Model mo){
        member.setRole("member");
        System.out.println(member);

        memberRe.save(member);
        Cookie cookie = memberService.cookieSet(member.getMemberId(),"off");
        response.addCookie(cookie);
        mo.addAttribute("member",member);
        return "redirect:/";
    }


    @GetMapping("/callBack/naver")
    public String callback(HttpServletResponse response,@CookieValue(value = "memberId", required = false)String memberId, Model mo, @RequestParam("code") String code){
        if(memberId != null){
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "redirect:/";
        }else{
            Member member = memberService.naverLogin(code);
            Optional ifUser = memberRe.findById(member.getMemberId());
            if(ifUser.isEmpty()){
                mo.addAttribute("navermember",member);
                return "views/naverSignUp";
            } else{
                Member hasUser = memberRe.findById(member.getMemberId()).orElseThrow();
                if(hasUser.getMemberId() == member.getMemberId()){
                    Cookie cookie = memberService.cookieSet(hasUser.getMemberId(),"off");
                    response.addCookie(cookie);
                    mo.addAttribute("member",member);
                    return "redirect:/";
                }else{
                    mo.addAttribute("alert","네이버 로그인 오류");
                    mo.addAttribute("/");
                    return "views/alert";
                }
            }
        }
    }

    @GetMapping("/member/memberEdit")
    public String memberEdit(@CookieValue(value = "memberId", required = false)String memberId, Model mo){

        if(memberId != null){
            Member member = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",member);
            return "/views/member/memberEdit";
        }else{
            return "views/signIn";
        }
    }

    @PostMapping("/memberPwUpdate")
    public String memberPwUpdate(@RequestParam("memberPw") String memberPw,@CookieValue(value = "memberId", required = false)String memberId,Model mo){
        if(memberId == null || memberPw==null){
            return "views/signIn";
        }else{
            int result = memberRe.memberPwUpdate(memberId,memberPw);
            System.out.println(result);
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "views/member/memberEdit";
        }
    }

    @PostMapping("/memberNnUpdate")
    public String memberNnUpdate(@RequestParam("nickname") String nickname,@CookieValue(value = "memberId", required = false)String memberId,Model mo){
        System.out.println(nickname);
        if(memberId == null || nickname ==null){
            return "views/signIn";
        }else{
            int result = memberRe.memberNnUpdate(memberId,nickname);
            System.out.println(result);
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "views/member/memberEdit";
        }
    }

    @PostMapping("/memberNameUpdate")
    public String memberNameUpdate(@RequestParam("memberName") String memberName,@CookieValue(value = "memberId", required = false)String memberId,Model mo){
        if(memberId == null || memberName==null){
            return "views/signIn";
        }else{
            int result = memberRe.memberNameUpdate(memberId,memberName);
            System.out.println(result);
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "views/member/memberEdit";
        }
    }

    @PostMapping("/memberPnUpdate")
    public String memberPnUpdate(@RequestParam("memberPhoneNum") String memberPhoneNum,@CookieValue(value = "memberId", required = false)String memberId,Model mo){
        if(memberId == null || memberPhoneNum==null){
            return "views/signIn";
        }else{
            int result = memberRe.memberPnUpdate(memberId,memberPhoneNum);
            System.out.println(result);
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "views/member/memberEdit";
        }
    }
}
