package com.springboot.sueulproject;

import com.springboot.sueulproject.entity.*;
import com.springboot.sueulproject.mapper.DetailMapper;
import com.springboot.sueulproject.repository.*;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootTest
class SueulApplicationTests {

    final DefaultMessageService messageService;
    final DetailRepository detailRe;
    final OriginRepository originRe;
    final TasteTagRepository tasteTagRe;
    final TypeRepository typeRe;
    final MemberRepository memberRe;
    final DetailMapper detailmapper;
    final TagBridgeRepository tagBridgeRe;

    @Autowired
    public SueulApplicationTests(DetailMapper detailmapper, DetailRepository detailRe, OriginRepository originRe, TasteTagRepository tasteTagRe, TypeRepository typeRe, MemberRepository memberRe, TagBridgeRepository tagBridgeRe) {
        this.tagBridgeRe = tagBridgeRe;
        this.messageService = NurigoApp.INSTANCE.initialize("NCSNXTUAO5TMAGBM","547E6L3OV0H5SP0HR6LYJWOFJMP19QBD","https://api.coolsms.co.kr");
        this.detailRe = detailRe;
        this.originRe = originRe;
        this.tasteTagRe = tasteTagRe;
        this.typeRe = typeRe;
        this.memberRe = memberRe;
        this.detailmapper = detailmapper;
    }

    @Test
    void contextLoads() {
        List<Integer> lst = new ArrayList<>();
        IntStream.rangeClosed(0,15).forEach(t -> lst.add(t));
        lst.stream().forEach(t -> System.out.println(t));
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void insertDetail(){
        Origin or = originRe.findById(1).orElseThrow();
        Type type = typeRe.findById(1).orElseThrow();
        Detail de = Detail.builder().name("tttest22").content("tttest22").origin(or).type(type).build();
        detailRe.save(de);
        List<TagBridge> lst = LongStream.range(1,10).mapToObj(t -> {
            System.out.println(t);
            TasteTag tag = tasteTagRe.tagFindByIdOne(t);
            TagBridge tb = TagBridge.builder().detail(de).tasteTag(tag).build();
            tag.getTbList().add(tb);
            return tb;
        }).collect(Collectors.toList());
        de.getTbList().addAll(lst);
        detailRe.save(de);
        System.out.println(de);
        System.out.println(lst.size());
    }

    @Test
    public void qwe(){
        List<TasteTag> tag = detailmapper.tagFindeAll();
        System.out.println(tag);
    }


    @Test
    public void substr() {
        String str = "01072711283";
        String str1 = str.substring(0, 3);
        String str2 = str.substring(3, 7);
        String str3 = str.substring(7, 11);
        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
    }

    @Test
    public void tt(){
        String Num = "01072711283";
        String random = String.valueOf(Math.random());
        random = random.substring(2,7);
        String smsContent = "[Sueul] 본인인증번호는 ["+random+"]입니다.";

        Message message = new Message();
        message.setFrom("01072711283");
        message.setTo(Num);
        message.setText(smsContent);

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));

        System.out.println(response);
    }



    @Test
    @Transactional
    public void qweqweq(){
        Detail de = detailRe.findById(6L).orElseThrow();
        TasteTag tt = tasteTagRe.findById(1L).orElseThrow();

        System.out.println(de.getTbList());
        System.out.println(tt);
    }
}
