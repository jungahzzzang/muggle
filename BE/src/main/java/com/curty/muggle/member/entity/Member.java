package com.curty.muggle.member.entity;

import com.curty.muggle.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID", nullable = false, unique = true)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    private MemberState state;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "NICKNAME", nullable = false, unique = true)
    private String nickname;
}