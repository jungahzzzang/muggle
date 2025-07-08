import { useEffect } from "react";
import { useState } from "react";
import Container from "../../components/layout/Container";
import KakaoLoginButton from "../../components/login/KakaoLoginButton";
import "../../assets/css/LoginPage.css";

const LoginPage = () => {

    const [clientId, setClientId] = useState(null);
    const [redirectUri, setRedirectUri] = useState(null);

    useEffect(() => {
        fetch("/oauth/kakao-info")
            .then((res) => res.json()) 
            .then((data) => {
                setClientId(data.client_id);
                setRedirectUri(data.redirect_uri);
            })
            .catch((err) => console.error(err)); 
    }, []);


    return (
        <>
            <Container>
                <div className="login-wrapper">
                <h2 className="login-title">로그인</h2>
                <p className="login-sub">SNS 계정으로 간편 로그인하기</p>

                <div className="login-buttons">
                    <KakaoLoginButton clientId={clientId} redirectUri={redirectUri} />
                    {/* 추후 네이버 로그인 버튼 */}
                    {/* <NaverLoginButton clientId={naverId} redirectUri={redirectUri} /> */}
                </div>
                </div>
            </Container>
        </>
    )
};

export default LoginPage;