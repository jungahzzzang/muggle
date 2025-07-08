import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMember } from '../../context/MemberContext';
import useAuthToken from '../../hooks/useAuthToken';
import ConfirmModal from "../modal/ConfirmModal";
import styles from "../../assets/css/Header.module.css";

const Logout = () => {
  const [showModal, setShowModal] = useState(false);
  const { setMember } = useMember();
  const navigate = useNavigate();
  const { removeToken } = useAuthToken();

  const handleLogoutClick = () => {
    setShowModal(true);
  };

  const confirmLogout = () => {
    removeToken();        // 쿠키 삭제
    setMember(null);      // 멤버 초기화
    sessionStorage.removeItem("code_used"); // 인가 코드 초기화
    navigate("/");   // 메인 페이지로 이동
    console.log("로그아웃 성공");
  };

  return (
    <>
    <button onClick={handleLogoutClick} className={styles.auth_button}>
      로그아웃
    </button>
    {showModal && (
        <ConfirmModal
          message="정말 로그아웃하실 건가요? 🥺"
          onConfirm={() => {
            console.log("로그아웃 확인");
            confirmLogout();
            setShowModal(false);
          }}
          onCancel={() => setShowModal(false)}
        />
      )}
      </>
  );
};

export default Logout;