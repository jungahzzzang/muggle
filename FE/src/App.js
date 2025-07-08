import { BrowserRouter, Route, Routes } from "react-router-dom";
import React, { Suspense, useState } from "react";
import styled from "styled-components";
import Header from "./components/layout/Header";
import LoginPage from "./page/member/LoginPage";
import KakaoLoginCallback from "./components/login/KakaoLoginCallback";
import { MemberProvider } from "./context/MemberContext";
import TheaterSeatPage from "./page/theater/TheaterSeatPage";
import HomePage from "./page/HomePage";
import PostsPage from "./components/post/PostsPage";
import PostPage from "./components/post/PostPage";
import PostAddPage from "./components/post/PostAddPage";
import PostEditPage from "./components/post/PostEditPage";

const StyledApp = styled.main`
  display: flex;
  flex-direction: column;
  min-height: 100vh;
`;

const StyledCoontent = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
`;

function App() {
  return (
    <MemberProvider>
      <BrowserRouter>
        <Suspense fallback={<div></div>}>
          <StyledApp>
            <Header />
            <StyledCoontent>
              <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/oauth/callback/kakao" element={<KakaoLoginCallback />} />
                <Route path="/theater/:theaterId" element={<TheaterSeatPage />} />
                <Route path="/posts" element={<PostsPage />} />
                <Route path="/posts/:postId" element={<PostPage />} />
                <Route path="/posts/add" element={<PostAddPage />} />
                <Route path="/posts/:postId/edit" element={<PostEditPage />} />
              </Routes>
            </StyledCoontent>
          </StyledApp>
        </Suspense>
      </BrowserRouter>
    </MemberProvider>
  );
}

export default App;
