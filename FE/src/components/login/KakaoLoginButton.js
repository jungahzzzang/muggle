import React from "react";
import kakao_login_button from "../../assets/images/login/kakao_login_button.png";

const KakaoLoginButton = ({ clientId, redirectUri }) => {
    const handleKakaoLogin = () => {
        console.log("Kakao login button clicked");
        console.log("Client ID:", clientId);
        console.log("Redirect URI:", redirectUri);
        if (clientId && redirectUri) {
            window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code`;
        }
    };

    return (
        <a onClick={handleKakaoLogin}>
            <img src={kakao_login_button} width="200" alt="kakao" style={{ cursor: "pointer" }}/>
        </a>
    )
}

export default KakaoLoginButton;