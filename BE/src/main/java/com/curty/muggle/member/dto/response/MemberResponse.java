package com.curty.muggle.member.dto.response;

import com.curty.muggle.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 정보를 클라이언트에 응답할 때 사용하는 DTO 클래스
 * Member 엔티티를 받아 필요한 필드만 변환하여 전달
 */
@Getter
@NoArgsConstructor
@Setter
public class MemberResponse {
    private String email;
    private String nickname;
    private String role;
    private Long memberId;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.role = member.getRole().name();
        this.memberId = member.getMemberId();
    }
}
