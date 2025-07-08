// src/components/posts/PostPage.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import useAuthToken from "../../hooks/useAuthToken";
import { useParams, useNavigate } from "react-router-dom";
import "../../assets/css/PostPage.css";

import emptyHeart from "../../assets/images/post/empty-heart.svg";
import filledHeart from "../../assets/images/post/heart.svg";

const PostPage = () => {
  const navigate = useNavigate();
  const { getToken } = useAuthToken(); // getToken 함수를 여기서 가져옵니다.

  const { postId } = useParams();

  const [post, setPost] = useState(null);
  const [error, setError] = useState(null);
  const [isLiked, setIsLiked] = useState(false);

  const [comments, setComments] = useState([]);
  const [commentContent, setCommentContent] = useState("");

  // [ PostCategory ENUM 값을 한글로 매핑하기 위한 Map ]
  const categoryMap = {
    ETC: "잡담",
    INFORMATION: "정보",
    QUESTION: "질문",
    REVIEW: "후기",
  };

  // [ 게시글 상세 정보 및 좋아요 상태 조회 ]
  useEffect(() => {
    const fetchPostAndLikeStatus = async () => {
      setError(null);
      try {
        const postResponse = await axios.get(`/api/posts/${postId}`);
        setPost(postResponse.data);

        const atk = getToken();
        if (atk) {
          const likeCheckResponse = await axios.get(
            `/api/posts/${postId}/like/check`,
            {
              headers: { Authorization: `Bearer ${atk}` },
            }
          );
          setIsLiked(likeCheckResponse.data);
        } else {
          setIsLiked(false);
        }
      } catch (err) {
        console.error("게시글 데이터를 불러오는 중 오류 발생:", err);
        setError(err);
        setPost(null);
      }
    };

    fetchPostAndLikeStatus();
  }, [postId]);

  // [ 댓글 첫 페이지 목록 조회 ]
  useEffect(() => {
    const fetchInitialComments = async () => {
      try {
        const response = await axios.get(`/api/posts/${postId}/comments`, {
          params: { cursorId: null },
        });
        setComments(response.data);
      } catch (err) {
        console.error("댓글을 불러오는 중 오류 발생:", err);
        setComments([]);
      }
    };

    fetchInitialComments();
  }, [postId]);

  // [ 작성일을 '정확한 시간'으로 변경해주는 함수 ]
  const formatDateTime = (isoString) => {
    if (!isoString) return "";
    const date = new Date(isoString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}`;
  };

  // [ 좋아요 버튼을 클릭한 경우 ]
  const handleLikeClick = async () => {
    const atk = getToken();
    if (!atk) {
      alert("로그인을 먼저 진행해주세요.");
      navigate("/login");
      return;
    }
    try {
      await axios.post(`/api/posts/${postId}/like?hasLike=${isLiked}`, null, {
        headers: { Authorization: `Bearer ${atk}` },
      });
      setIsLiked(!isLiked);
      setPost((prevPost) => ({
        ...prevPost,
        likeCount: isLiked ? prevPost.likeCount - 1 : prevPost.likeCount + 1,
      }));
    } catch (err) {
      console.error("좋아요 처리 중 오류 발생:", err);
      alert("좋아요 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
    }
  };

  // [ 에러 시 에러 메시지 표시 ]
  if (error) {
    return (
      <div className="post-detail-container">
        게시글을 불러오는 데 실패했습니다: {error.message}
      </div>
    );
  }

  // [ 로딩 중 ]
  if (!post) {
    return <div className="post-detail-container">게시글을 불러오는 중...</div>;
  }

  // [ 수정 버튼을 클릭한 경우 ]
  const handleUpdateClick = async () => {
    const atk = getToken();
    try {
      await axios.get(`/api/posts/${postId}/author/check`, {
        headers: { Authorization: `Bearer ${atk}` },
      });
    } catch (err) {
      if (err.response && err.response.status === 401) {
        alert("다른 유저의 게시글을 수정할 수 없습니다.");
      } else {
        console.error("권한 확인 중 오류 발생:", err);
        alert("권한 확인 중 문제가 발생했습니다. 다시 시도해주세요.");
      }
      return;
    }
    navigate(`/posts/${postId}/edit`);
  };

  // [ 삭제 버튼을 클릭한 경우 ]
  const handleDeleteClick = async () => {
    if (!window.confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
      return;
    }
    const atk = getToken();
    try {
      await axios.get(`/api/posts/${postId}/author/check`, {
        headers: { Authorization: `Bearer ${atk}` },
      });
    } catch (err) {
      if (err.response && err.response.status === 401) {
        alert("다른 유저의 게시글을 삭제할 수 없습니다.");
      } else {
        console.error("권한 확인 중 오류 발생:", err);
        alert("권한 확인 중 문제가 발생했습니다. 다시 시도해주세요.");
      }
      return;
    }
    try {
      const response = await axios.delete(`/api/posts/${postId}`, {
        headers: { Authorization: `Bearer ${atk}` },
      });
      if (response.status === 200) {
        alert("게시글 삭제가 완료되었습니다.");
        navigate("/posts");
      }
    } catch (err) {
      console.error("삭제 처리 중 오류 발생:", err);
      if (err.response) {
        alert(
          err.response.data.message ||
            "게시글 삭제 중 문제가 발생했습니다. 다시 시도해주세요."
        );
      } else {
        alert(`삭제 처리 중 문제가 발생했습니다: ${err.message}`);
      }
    }
  };

  // [ 댓글 등록 시 ]
  const handleCommentSubmit = async (e) => {
    e.preventDefault();

    // AccessToken 확인
    const atk = getToken();
    if (!atk) {
      alert("로그인해야 댓글을 작성할 수 있습니다.");
      navigate("/login");
      return;
    }

    // 유효성 검사
    if (commentContent.trim() === "") {
      alert("댓글 내용을 입력해주세요.");
      return;
    }

    // API 호출 (댓글 등록)
    try {
      await axios.post(
        `/api/posts/${postId}/comments`,
        { content: commentContent, parentId: null },
        { headers: { Authorization: `Bearer ${atk}` } }
      );
      alert("댓글이 성공적으로 등록되었습니다.");
      setCommentContent("");

      // 댓글을 등록한 후, 현재 페이지의 댓글 목록을 다시 불러와서 최신 댓글 반영
      try {
        const response = await axios.get(`/api/posts/${postId}/comments`, {
          params: { cursorId: null },
        });
        setComments(response.data);
      } catch (err) {
        console.error("댓글 새로고침 중 오류 발생:", err);
      }
    } catch (err) {
      console.error("댓글 등록 중 오류 발생:", err);
      if (err.response) {
        alert(
          err.response.data.message ||
            "댓글 등록 중 문제가 발생했습니다. 다시 시도해주세요."
        );
      } else {
        alert(`댓글 등록 실패: ${err.message}`);
      }
    }
  };

  return (
    <div className="post-detail-container">
      <h1 className="post-detail-title">
        <span className="post-category">
          [{categoryMap[post.category] || post.category}]
        </span>{" "}
        {post.title}
      </h1>
      <div className="post-meta">
        <span className="meta-item">작성자: {post.memberName}</span>
        <span className="meta-item">
          작성일: {formatDateTime(post.createdAt)}
        </span>
        <span className="meta-item right-aligned-meta-item">
          조회수: {post.viewCount}
        </span>
        <span className="meta-item">좋아요: {post.likeCount}</span>
      </div>
      <div className="post-content">
        {post.images && post.images.length > 0 && (
          <div className="post-images-section">
            {post.images.map((image, index) => (
              <img
                key={index}
                src={image.imageUrl}
                alt={image.originalImageName || `첨부 이미지 ${index + 1}`}
                className="post-image"
              />
            ))}
          </div>
        )}
        <p>{post.content}</p>
      </div>

      <div className="like-button-area">
        <button className="like-button" onClick={handleLikeClick}>
          <img
            src={isLiked ? filledHeart : emptyHeart}
            alt="좋아요"
            className="heart-icon"
          />
          좋아요 {post.likeCount}
        </button>
      </div>

      <div className="post-actions">
        <button
          className="post-action-button"
          onClick={handleUpdateClick}
          style={{ backgroundColor: "#ffc107" }}
        >
          수정
        </button>
        <button
          className="post-action-button"
          onClick={handleDeleteClick}
          style={{ backgroundColor: "#dc3545" }}
        >
          삭제
        </button>
        <button
          className="post-action-button"
          onClick={() => navigate("/posts")}
          style={{ backgroundColor: "#6c757d" }}
        >
          목록으로
        </button>
      </div>

      {/* 댓글 섹션 */}
      <div className="comments-section">
        <h2>댓글 ({comments.length})</h2>
        {/* 댓글 입력 폼 */}
        <form className="comment-form" onSubmit={handleCommentSubmit}>
          <textarea
            className="comment-textarea"
            placeholder="댓글을 입력하세요..."
            value={commentContent}
            onChange={(e) => setCommentContent(e.target.value)}
            rows="4"
          ></textarea>
          <button type="submit" className="comment-submit-button">
            댓글 등록
          </button>
        </form>

        {/* 댓글 목록 표시 */}
        <div className="comment-list">
          {comments.length === 0 ? (
            <p className="no-comments">아직 댓글이 없습니다.</p>
          ) : (
            comments.map((comment) => (
              <div key={comment.commentId} className="comment-item">
                <div className="comment-meta">
                  {/* 댓글 작성자 */}
                  <span className="comment-author">
                    {comment.memberNickname}
                    {post.memberId === comment.memberId && (
                      <span className="author-badge"> (글쓴이)</span>
                    )}
                  </span>
                  {/* 댓글 작성 시간 */}
                  <span className="comment-date">
                    {formatDateTime(comment.createdAt)}
                  </span>
                </div>
                <p className="comment-content">{comment.content}</p>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default PostPage;
