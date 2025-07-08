import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import styles from "../../assets/css/Header.module.css";
import logo from '../../assets/images/logo/muggle_logo.png';
import { useMember } from '../../context/MemberContext';
import Logout from '../logout/Logout';

 const Header = () => {
  const memberContext = useMember();
  const member = memberContext?.member;

    return (
      <header>
        <div className={styles.banner_inner}>
          <nav>
            <Link to="/" className={styles.homepage}>
              <div className={styles.logo_area}>
                <img src={logo} className={styles.logo_img}/>
              </div>
            </Link>
            <Link to="/posts">
              게시판
            </Link>
          </nav>
          <div className={styles.log}>
            {member ? (
              <>
                <div className={styles.name}>{member.nickname}님 안녕하세요!</div>
                <Logout />
              </>
            ) : (
              <Link to="/login" className={styles.auth_button}>
                로그인
              </Link>
            )}
          </div>
        </div>
      </header>
  );
 };

 

 export default Header;