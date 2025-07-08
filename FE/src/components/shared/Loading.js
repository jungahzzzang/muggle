import React from "react";
import styled from "styled-components";
import Spinner from "../../assets/images/loading/spinner.gif"

const LogoWrapper = styled.div`
    position: absolute;
    width: 100vw;
    height: 100vh;
    top: 0;
    left: 0;
    background: #ffffffb7;
    z-index: 999;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
`;

const LoadingText = styled.div`
    font: 'Note Sans KR';
    text-alian: center;
`;

const Loading = () => {

    return (
        <LogoWrapper>
            <LoadingText>잠시만 기다려주세요.</LoadingText>
            <img src={Spinner} alt="로딩중" width="5%" />
        </LogoWrapper>
    )
}

export default Loading;