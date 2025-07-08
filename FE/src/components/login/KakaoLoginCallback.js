import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMember } from '../../context/MemberContext';
import useAuthToken from '../../hooks/useAuthToken';
import axios from 'axios';
import Loading from '../shared/Loading';

const KakaoLoginCallback = () => {
    const navigate = useNavigate();
    const { saveToken } = useAuthToken();
    const { setMember } = useMember();

    useEffect(() => {
        // 중복 호출 방지
        const code = new URL(window.location.href).searchParams.get("code");
        if (!code) return;

        if (sessionStorage.getItem("code_used") === code) return;
        sessionStorage.setItem("code_used", code);

        if (code) {
            axios.get(`/oauth/callback/kakao?code=${code}`)
                .then((res) => {
                    console.log("카카오 로그인 성공", res.data);
                    const { access_token, member } = res.data;
                    // 멤버 정보 설정   
                    setMember(member);
                    // 토큰을 쿠키에 저장
                    saveToken(access_token);
                    // URL의 쿼리 파라미터 제거
                    window.history.replaceState(null, "", "/")
                    // 로그인 후 메인 페이지로 이동
                    navigate('/');
                })
                .catch((err) => {
                    console.error("카카오 로그인 실패", err);
                    navigate('/');
                });
        } else {
            console.error("카카오 로그인 요청 실패");
            navigate('/');
        }
    }, [navigate]);

    return <Loading />;
};

export default KakaoLoginCallback;