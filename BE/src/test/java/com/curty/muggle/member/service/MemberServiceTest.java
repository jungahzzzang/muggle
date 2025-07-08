package com.curty.muggle.member.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.repository.MemberRepository;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse;
import com.curty.muggle.oauth.dto.KakaoUserInfoResponse.KakaoAccount.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private KakaoUserInfoResponse user;
    private String testEmail;
    private String testNickname;

    @BeforeEach
    public void init() {
        testEmail = "test@example.com";
        testNickname = "testNickname";
        user = createMockUser(testEmail, testNickname);
        member = user.toMember();
    }

    @Test
    void isMember_이메일존재할때_true반환() {
        when(memberRepository.existsByEmail(testEmail)).thenReturn(true);

        // when
        boolean result = memberService.isMember(user.getKakaoAccount().getEmail());

        // then
        assertTrue(result);
        verify(memberRepository).existsByEmail(testEmail);
    }

    @Test
    void isMember_이메일없을때_false반환() {
        when(memberRepository.existsByEmail(testEmail)).thenReturn(false);

        // when
        boolean result = memberService.isMember(user.getKakaoAccount().getEmail());

        // then
        assertFalse(result);
        verify(memberRepository).existsByEmail(testEmail);
    }

    @Test
    void save_회원가입() {
        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.save(member);
        assertNotNull(result);
        verify(memberRepository).save(member);
    }

    private KakaoUserInfoResponse createMockUser(String email, String nickname) {
        KakaoUserInfoResponse.KakaoAccount kakaoAccount = new KakaoUserInfoResponse.KakaoAccount();
        kakaoAccount.setEmail(email);
        kakaoAccount.setProfile(new Profile(nickname));

        KakaoUserInfoResponse user = new KakaoUserInfoResponse();
        user.setKakaoAccount(kakaoAccount);
        return user;
    }
}
