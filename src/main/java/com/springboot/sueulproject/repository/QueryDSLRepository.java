package com.springboot.sueulproject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.springboot.sueulproject.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryDSLRepository {
    private final JPAQueryFactory queryFactory;
    private final QDetail detail = QDetail.detail;
    private final QBookmarkBridge bookmarkBridge = QBookmarkBridge.bookmarkBridge;
    private final QMember member = QMember.member;
    private final QTagBridge tagBridge = QTagBridge.tagBridge;

    public List<Detail> QfindByAll(){
        return queryFactory.select(detail).from(detail).fetch();
    }

    public List<Detail> QfindByBookmarkBridge(String memberid){
        return queryFactory.selectFrom(detail).leftJoin(bookmarkBridge)
                .on(detail.eq(bookmarkBridge.detail))
                .where(bookmarkBridge.member.memberId.eq(memberid)).fetch();
    }
    public List<Detail> QfindByTasteTag(Long tid){
        return queryFactory.selectFrom(detail).leftJoin(tagBridge)
                .on(detail.eq(tagBridge.detail))
                .where(tagBridge.tasteTag.tid.eq(tid)).fetch();
    };
    @Transactional
    public Long memberUpdate(String memberid, String newmember, String info){
        JPAUpdateClause clause = queryFactory.update(member);

        if(info.equals("pw")){
            clause.set(member.memberPw,newmember);
        }else if(info.equals("nn")){
            clause.set(member.nickname,newmember);
        }else if(info.equals("mn")){
            clause.set(member.memberName,newmember);
        }else if(info.equals("pn")){
            clause.set(member.memberPhoneNum,newmember);
        }
        return clause.where(member.memberId.eq(memberid)).execute();
    }
}
