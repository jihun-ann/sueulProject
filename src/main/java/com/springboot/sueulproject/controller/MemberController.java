package com.springboot.sueulproject.controller;

import com.springboot.sueulproject.entity.Member;
import com.springboot.sueulproject.repository.MemberRepository;
import com.springboot.sueulproject.repository.QueryDSLRepository;
import com.springboot.sueulproject.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {
    final MemberRepository memberRe;
    final MemberService memberService;
    final QueryDSLRepository queryDSLRe;

    @Autowired
    public MemberController(MemberRepository memberRe, MemberService memberService, QueryDSLRepository queryDSLRe) {
        this.memberRe = memberRe;
        this.memberService = memberService;
        this.queryDSLRe = queryDSLRe;
    }

    @GetMapping("/signIn")
    public String signIn(@CookieValue(value = "memberId",required = false)String memberId, Model mo, HttpServletRequest request, HttpSession session){
        String prevUrl = request.getHeader("Referer");
        prevUrl = prevUrl.substring(24);
        session.setAttribute("prevUrl",prevUrl);

        if(memberId != null){
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "redirect:"+prevUrl;
        }else{
            return "views/signIn";
        }
    }

    @PostMapping("/signIn")
    public String userSignIn(Member member, @RequestParam(value="cookieSave") String check, Model mo, HttpServletResponse response,HttpSession session) {
        String prevUrl = (String) session.getAttribute("prevUrl");

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
                return "redirect:"+prevUrl;
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
    public String signUp(Model mo,@CookieValue(value = "memberId", required = false)String memberId){

        if(memberId == null) {
            return "views/signUp";
        }else{
            Member member = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",member);
            return "/views/index";
        }
    }

    @PostMapping("/signUp")
    public String signUpsueul(Member member,HttpServletResponse response,Model mo,HttpSession session){
        member.setRole("member");

        memberRe.save(member);
        Cookie cookie = memberService.cookieSet(member.getMemberId(),"off");
        response.addCookie(cookie);
        mo.addAttribute("member",member);
        String prevUrl = (String) session.getAttribute("prevUrl");
        return "redirect:"+prevUrl;
    }


    @RequestMapping("/callback/naver")
    public String callback(@RequestParam("code") String code,
                           HttpSession session,
                           HttpServletResponse response,
                           @CookieValue(value = "memberId", required = false)String memberId,
                           Model mo){

        String prevUrl = (String) session.getAttribute("prevUrl");
        System.out.println(">>>>>>>>>>>>>>>>>"+prevUrl);
        if(memberId != null){
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "redirect:"+prevUrl;
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
                    return "redirect:"+prevUrl;
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

    @PostMapping("/memberUpdate/{info}")
    public String memberPwUpdate(
            @PathVariable("info") String info,
            @RequestParam("newmember") String newmember,
            @CookieValue(value = "memberId", required = false)String memberId,
            Model mo){
        if(memberId == null || newmember==null){
            return "views/signIn";
        }else{
            //int result = memberRe.memberPwUpdate(memberId,memberPw);
            Long result = queryDSLRe.memberUpdate(memberId,newmember,info);
            System.out.println(result);
            Member hasUser = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",hasUser);
            return "views/member/memberEdit";
        }
    }

//    @PostMapping("/memberNnUpdate")
//    public String memberNnUpdate(@RequestParam("nickname") String nickname,@CookieValue(value = "memberId", required = false)String memberId,Model mo){
//        System.out.println(nickname);
//        if(memberId == null || nickname ==null){
//            return "views/signIn";
//        }else{
//            //int result = memberRe.memberNnUpdate(memberId,nickname);
//            String info = "nn";
//            Long result = queryDSLRe.memberUpdate(memberId,nickname,info);
//            System.out.println(result);
//            Member hasUser = memberRe.findById(memberId).orElseThrow();
//            mo.addAttribute("member",hasUser);
//            return "views/member/memberEdit";
//        }
//    }
//
//    @PostMapping("/memberNameUpdate")
//    public String memberNameUpdate(@RequestParam("memberName") String memberName,@CookieValue(value = "memberId", required = false)String memberId,Model mo){
//        if(memberId == null || memberName==null){
//            return "views/signIn";
//        }else{
//            int result = memberRe.memberNameUpdate(memberId,memberName);
//            System.out.println(result);
//            Member hasUser = memberRe.findById(memberId).orElseThrow();
//            mo.addAttribute("member",hasUser);
//            return "views/member/memberEdit";
//        }
//    }
//
//    @PostMapping("/memberPnUpdate")
//    public String memberPnUpdate(@RequestParam("memberPhoneNum") String memberPhoneNum,@CookieValue(value = "memberId", required = false)String memberId,Model mo){
//        if(memberId == null || memberPhoneNum==null){
//            return "views/signIn";
//        }else{
//            int result = memberRe.memberPnUpdate(memberId,memberPhoneNum);
//            System.out.println(result);
//            Member hasUser = memberRe.findById(memberId).orElseThrow();
//            mo.addAttribute("member",hasUser);
//            return "views/member/memberEdit";
//        }
//    }

    @GetMapping("/memberEditAdmin")
    public String adminMemberEdit(@CookieValue(value = "memberId", required = false)String memberId, Model mo){

        if(memberId != null){
            Member member = memberRe.findById(memberId).orElseThrow();

            if(member.getRole().equals("admin")){
                List<Member> mlst = memberRe.findByRoleAll("member",0);
                List<Member> adlst = memberRe.findByRoleAll("admin",0);
                mo.addAttribute("member",member);
                mo.addAttribute("memberLst",mlst);
                mo.addAttribute("adminLst",adlst);
                return "views/admin/memberList";
            }else{
                mo.addAttribute("alert","권한이 없습니다.");
                mo.addAttribute("url","/");
                return "views/alert";
            }
        }else{
            return "views/index";
        }
    }
    @GetMapping("/admin/memberEdit/{userId}")
    public String memberEditMaster(@CookieValue(value = "memberId", required = false)String memberId, @PathVariable("userId")String userId, Model mo){

        if(memberId != null){
            Member member = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",member);

            if(member.getRole().equals("admin")){
                Member user = memberRe.findById(userId).orElseThrow();
                mo.addAttribute("member",member);
                mo.addAttribute("user",user);
                return "views/admin/memberEdit";
            }else{
                mo.addAttribute("alert","권한이 없습니다.");
                mo.addAttribute("url","/");
                return "views/alert";
            }
        }else{
            mo.addAttribute("alert","불완전한 접근입니다. 다시 한번 시도하여 주세요.");
            mo.addAttribute("url","/");
            return "views/alert";
        }
    }

    @PostMapping("/memberEditAdmin")
    public String memberEditMaster(Model mo,@CookieValue(value = "memberId", required = false)String memberId,Member user){
        if(memberId != null){
            Member member = memberRe.findById(memberId).orElseThrow();
            mo.addAttribute("member",member);

            if(member.getRole().equals("admin")) {
                memberRe.save(user);
            }
        }
        return "redirect:/memberEditAdmin";
    }
}
