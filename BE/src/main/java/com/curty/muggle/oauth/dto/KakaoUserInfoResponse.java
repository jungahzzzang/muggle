package com.curty.muggle.oauth.dto;

import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.entity.MemberRole;
import com.curty.muggle.member.entity.MemberState;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * 카카오 OAuth 로그인 후 받아오는 사용자 정보 응답 DTO
 * JSON 프로퍼티 매핑과 내부 클래스를 통해 카카오 계정 정보를 캡슐화
 *
 * toMember() 메서드를 통해 Member 엔티티로 변환 가능하며,
 * 기본 권한은 USER, 상태는 ACTIVE로 설정됨
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class KakaoUserInfoResponse {
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Profile {
            private String nickname;
        }
    }

    public Member toMember() {
        return Member.builder()
                .email(this.kakaoAccount.getEmail())
                .nickname(this.kakaoAccount.getProfile().getNickname())
                .role(MemberRole.USER)
                .state(MemberState.ACTIVE)
                .build();
    }

}
