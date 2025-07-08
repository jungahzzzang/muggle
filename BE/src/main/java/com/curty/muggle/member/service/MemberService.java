package com.curty.muggle.member.service;

import static com.curty.muggle.common.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.curty.muggle.common.exception.custom.EntityNotFoundException;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.repository.MemberRepository;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 회원 관련 비즈니스 로직 처리 서비스
 */
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 비회원일 경우 회원가입을 진행하고, 기존 회원이면 조회하여 반환
     *
     * @param user 카카오에서 받은 사용자 정보 DTO {@link KakaoUserInfoResponse}
     * @return 기존 회원 또는 새로 가입된 회원 엔티티 {@link Member}
     */
    public Member findOrCreate(KakaoUserInfoResponse user) {
        return memberRepository.findByEmail(user.getKakaoAccount().getEmail())
                .orElseGet(() -> {
                    logger.info("[비회원] 회원 가입 진행");
                    return memberRepository.save(user.toMember());
                });
    }

    /**
     * 이메일로 회원 존재 여부를 확인
     *
     * @param email 회원 이메일
     * @return 존재하면 true, 아니면 false
     */
    public boolean isMember(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 회원 정보를 저장
     *
     * @param member 저장할 회원 엔티티
     * @return 저장된 회원 엔티티 {@link Member}
     */
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    /**
     * 이메일로 회원을 조회
     *
     * @param email 조회할 회원 이메일
     * @return 조회된 회원 엔티티 {@link Member}
     * @throws EntityNotFoundException 회원을 찾지 못했을 경우 예외 발생
     */
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
    }
}
