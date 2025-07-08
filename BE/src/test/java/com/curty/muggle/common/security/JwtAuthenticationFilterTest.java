package com.curty.muggle.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.curty.muggle.common.jwt.JwtProvider;
import com.curty.muggle.common.security.JwtAuthenticationFilter;
import com.curty.muggle.common.security.KakaoMemberDetails;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.member.entity.MemberRole;
import com.curty.muggle.member.entity.MemberState;
import com.curty.muggle.member.service.MemberService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private MemberService memberService;

    @BeforeEach
    public void init() {

    }

    @Test
    void doFilterInternal_인증정보세팅() throws Exception {
        //given
        String token = "test.jwt.token";
        String email = "test@test.com";
        Member mockMember = new Member(null, MemberRole.USER, MemberState.ACTIVE, email, "nickname");
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(token)).thenReturn(email);
        when(memberService.findByEmail(email)).thenReturn(mockMember);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        //when
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KakaoMemberDetails userDetails = (KakaoMemberDetails) authentication.getPrincipal();

        //then
        assertNotNull(authentication);
        assertEquals(mockMember, userDetails.getMember());
        verify(chain).doFilter(request, response);
    }
}
