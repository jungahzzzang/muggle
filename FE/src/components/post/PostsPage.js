// src/components/posts/PostsPage.js
import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import useAuthToken from "../../hooks/useAuthToken";
import { useNavigate } from "react-router-dom";
import "../../assets/css/PostsPage.css";

const PostsPage = () => {
  const navigate = useNavigate();
  const { getToken } = useAuthToken();

  const [posts, setPosts] = useState([]);
  const [error, setError] = useState(null);
  const [pageInfo, setPageInfo] = useState({
    size: 10,
    number: 0,
    totalElements: 0,
    totalPages: 0,
  });
  const [currentPage, setCurrentPage] = useState(1);
  const [keyword, setKeyword] = useState("");
  const [searchKeyword, setSearchKeyword] = useState("");
  const [sort, setSort] = useState("LATEST");
  const [category, setCategory] = useState("");

  const categoryMap = {
    ETC: "잡담",
    INFORMATION: "정보",
    QUESTION: "질문",
    REVIEW: "후기",
  };

  // [ 게시글 목록을 불러오기 위한 API 호출 함수 ]
  const fetchPosts = useCallback(
    async (
      page = 1,
      currentSort = sort,
      currentSearchKeyword = searchKeyword,
      currentCategory = category
    ) => {
      setError(null);

      try {
        const response = await axios.get(`/api/posts`, {
          params: {
            page: page,
            sort: currentSort,
            keyword: currentSearchKeyword,
            category: currentCategory,
          },
        });

        setPosts(response.data.content || []);
        setPageInfo({
          ...response.data.page,
          number: response.data.page.number + 1,
        });
        setCurrentPage(page);
      } catch (err) {
        console.error("게시글을 불러오는 중 오류가 발생했습니다.", err);
        setError(err);
        setPosts([]);
        setPageInfo({ size: 10, number: 1, totalElements: 0, totalPages: 0 });
        setCurrentPage(1);
      }
    },
    [sort, searchKeyword, category]
  );

  // [ 트리거 ]
  useEffect(() => {
    // 키워드, 정렬 조건, 카테고리 중 하나라도 변경될 때 API 다시 호출
    // 페이지는 항상 1페이지로 초기화 (새로운 검색/정렬/카테고리 선택 시)
    fetchPosts(1, sort, searchKeyword, category);
  }, [sort, searchKeyword, category, fetchPosts]);

  // [ 작성일을 '상대 시간'으로 변경해주는 함수 ]
  const formatTimeAgo = (isoString) => {
    if (!isoString) return "";
    const postDate = new Date(isoString);
    const now = new Date();
    const diffSeconds = Math.floor((now.getTime() - postDate.getTime()) / 1000);

    const MINUTE = 60;
    const HOUR = MINUTE * 60;
    const DAY = HOUR * 24;
    const MONTH = DAY * 30;
    const YEAR = DAY * 365;

    if (diffSeconds < MINUTE) {
      return "방금";
    } else if (diffSeconds < HOUR) {
      return `${Math.floor(diffSeconds / MINUTE)}분 전`;
    } else if (diffSeconds < DAY) {
      return `${Math.floor(diffSeconds / HOUR)}시간 전`;
    } else if (diffSeconds < MONTH) {
      return `${Math.floor(diffSeconds / DAY)}일 전`;
    } else if (diffSeconds < YEAR) {
      return `${Math.floor(diffSeconds / MONTH)}달 전`;
    } else {
      return `${Math.floor(diffSeconds / YEAR)}년 전`;
    }
  };

  // [ 페이지 번호 배열을 계산하는 함수 ]
  const getPaginationNumbers = () => {
    const numbers = [];
    const maxPagesToShow = 10;

    let startPage = Math.max(
      1,
      pageInfo.number - Math.floor(maxPagesToShow / 2)
    );
    let endPage = Math.min(pageInfo.totalPages, startPage + maxPagesToShow - 1);

    if (endPage - startPage + 1 < maxPagesToShow) {
      startPage = Math.max(1, endPage - maxPagesToShow + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      numbers.push(i);
    }
    return numbers;
  };

  // [ 검색 버튼을 클릭하거나 엔터 키를 입력 시 호출될 핸들러 ]
  const handleSearch = () => {
    setSearchKeyword(keyword);
  };

  // [ 정렬 박스 변경 시 호출될 핸들러 ]
  const handleSortChange = (e) => {
    setSort(e.target.value);
  };

  // [ 카테고리 박스 변경 시 호출될 핸들러 ]
  const handleCategoryChange = (e) => {
    setCategory(e.target.value);
  };

  // [ 에러 시 에러 메시지 표시 ]
  if (error) {
    return <div>게시글을 불러오지 못했습니다. 오류: {error.message}</div>;
  }

  // [ 등록하기 버튼을 클릭한 경우 ]
  const handleRegisterClick = () => {
    const atk = getToken();

    if (atk) {
      navigate("/posts/add");
    } else {
      alert("로그인을 먼저 진행해주세요.");
      navigate("/login");
    }
  };

  return (
    <div className="posts-container">
      <h1>게시판</h1>

      <div className="posts-header-controls">
        {/* 검색창 그룹 */}
        <div className="search-input-group">
          {/* 입력창 */}
          <input
            type="text"
            placeholder="검색어를 입력하세요"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                handleSearch();
              }
            }}
            className="search-input"
          />
          {/* 검색 버튼 */}
          <button onClick={handleSearch} className="search-button">
            검색
          </button>
        </div>

        {/* 카테고리/정렬 박스 및 등록하기 버튼 그룹 */}
        <div className="sort-register-group">
          {/* 카테고리 박스 */}
          <select
            value={category}
            onChange={handleCategoryChange}
            className="category-select"
          >
            <option value="">전체</option>
            {Object.entries(categoryMap).map(([key, value]) => (
              <option key={key} value={key}>
                {value}
              </option>
            ))}
          </select>

          {/* 정렬 박스 */}
          <select
            value={sort}
            onChange={handleSortChange}
            className="sort-select"
          >
            <option value="LATEST">최신순</option>
            <option value="OLDEST">오래된순</option>
            <option value="MOST_VIEWED">조회수순</option>
            <option value="MOST_LIKED">좋아요순</option>
          </select>

          {/* 등록하기 버튼 */}
          <button
            onClick={handleRegisterClick}
            className="register-button"
          >
            등록하기
          </button>
        </div>
      </div>

      {/* 게시글 목록 테이블 */}
      {posts.length === 0 ? (
        <p>아직 등록된 게시글이 없습니다.</p>
      ) : (
        <table className="posts-table">
          <thead>
            <tr>
              <th>번호</th>
              <th>제목</th>
              <th>작성자</th>
              <th>작성일</th>
              <th>조회수</th>
              <th>좋아요</th>
            </tr>
          </thead>
          <tbody>
            {posts.map((post) => (
              <tr key={post.postId}>
                <td>{post.postId}</td>
                <td className="title-cell">
                  <span
                    className={`post-category ${
                      post.state === "DELETED" ? "deleted" : ""
                    }`}
                  >
                    [{categoryMap[post.category] || post.category}]
                  </span>
                  {post.state === "DELETED" ? (
                    <span className="post-title deleted">{post.title}</span>
                  ) : (
                                        <span
                      onClick={() => navigate(`/posts/${post.postId}`)}
                      className="post-title"
                      style={{ cursor: "pointer" }}
                    >
                      {post.title}
                      {/* 댓글 개수를 게시글 제목과 같이 표시 */}
                      {post.commentCount > 0 && (
                        <span className="comment-count-badge">
                          [{post.commentCount}]
                        </span>
                      )}
                    </span>
                  )}
                </td>
                <td>{post.memberNickname}</td>
                <td>{formatTimeAgo(post.createdAt)}</td>
                <td>{post.viewCount}</td>
                <td>{post.likeCount}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {/* 페이지네이션 */}
      {pageInfo.totalPages > 0 && (
        <div className="pagination-container">
          {/* 이전 버튼 */}
          <button
            onClick={() =>
              fetchPosts(currentPage - 1, sort, searchKeyword, category)
            }
            disabled={currentPage <= 1}
            className={`pagination-button ${
              currentPage <= 1 ? "disabled" : ""
            }`}
          >
            이전
          </button>

          {/* 페이지 번호들 */}
          {getPaginationNumbers().map((pageNumber) => (
            <button
              key={pageNumber}
              onClick={() =>
                fetchPosts(pageNumber, sort, searchKeyword, category)
              }
              className={`pagination-button ${
                pageNumber === currentPage ? "active" : ""
              }`}
            >
              {pageNumber}
            </button>
          ))}

          {/* 다음 버튼 */}
          <button
            onClick={() =>
              fetchPosts(currentPage + 1, sort, searchKeyword, category)
            }
            disabled={currentPage >= pageInfo.totalPages}
            className={`pagination-button ${
              currentPage >= pageInfo.totalPages ? "disabled" : ""
            }`}
          >
            다음
          </button>
        </div>
      )}
    </div>
  );
};

export default PostsPage;
