// src/components/posts/PostAddPage.js
import React, { useState } from "react";
import axios from "axios";
import useAuthToken from "../../hooks/useAuthToken";
import { useNavigate } from "react-router-dom";
import "../../assets/css/PostAddPage.css";

// 카테고리 Enum 값
const POST_CATEGORIES = {
  ETC: "잡담",
  INFORMATION: "정보",
  QUESTION: "질문",
  REVIEW: "후기",
};

function PostCreateForm() {
  const navigate = useNavigate();
  const { getToken } = useAuthToken();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("ETC");
  const [images, setImages] = useState([]);
  const [imagePreviews, setImagePreviews] = useState([]);

  const [errors, setErrors] = useState({
    title: "",
    content: "",
    image: "",
  });

  // [ 에러 상태 초기화 함수 ]
  const clearErrors = () => {
    setErrors({
      title: "",
      content: "",
      image: "",
    });
  };

  // [ 게시글 등록 버튼을 클릭한 경우 ]
  const handleSubmit = async (e) => {
    e.preventDefault(); // 폼 기본 제출 동작 방지
    clearErrors();

    const atk = getToken();

    if (!atk) {
      alert("로그인 정보가 만료되었습니다. 다시 로그인해주세요.");
      navigate("/login");
      return;
    }

    // 폼 데이터 정의
    const formData = new FormData();
    formData.append("title", title);
    formData.append("content", content);
    formData.append("category", selectedCategory);

    images.forEach((file) => {
      formData.append("images", file);
    });

    // API 호출 (게시글 등록)
    try {
      await axios.post("/api/posts", formData, {
        headers: {
          Authorization: `Bearer ${atk}`,
        },
      });

      alert("게시글이 성공적으로 등록되었습니다.");
      navigate(`/posts`);
    } catch (error) {
      console.error("게시글 등록 중 오류 발생:", error);
      clearErrors(); // 에러 발생 시 에러 상태 초기화

      if (error.response) {
        const errorData = error.response.data;

        // 에러 종류(errorCode)에 따라 에러 메시지 설정
        if (errorData.errorCode) {
          switch (errorData.errorCode) {
            case "POST_TITLE_EMPTY":
            case "POST_TITLE_EXCEEDS_MAX_LENGTH":
              setErrors((prev) => ({ ...prev, title: errorData.message }));
              break;
            case "POST_CONTENT_EMPTY":
              setErrors((prev) => ({ ...prev, content: errorData.message }));
              break;
            case "IMAGE_EXCEEDS_MAX_SIZE":
              setErrors((prev) => ({ ...prev, image: errorData.message }));
              break;
            default:
              alert(errorData.message || "알 수 없는 오류가 발생했습니다.");
          }
        } else {
          alert(errorData.message || "알 수 없는 오류가 발생했습니다.");
        }
      } else {
        alert(`게시글 등록 실패: ${error.message}`);
      }
    }
  };

  // [ 첨부파일이 변경된 경우 ]
  const handleImageChange = (e) => {
    // 기존 미리보기 URL 해제
    imagePreviews.forEach((url) => URL.revokeObjectURL(url));

    // 새로운 이미지 파일 저장
    const files = Array.from(e.target.files);
    setImages(files);

    // 에러 메시지 초기화
    setErrors((prev) => ({ ...prev, image: "" }));

    // 미리보기 URL 생성
    const newImagePreviews = files.map((file) => URL.createObjectURL(file));
    setImagePreviews(newImagePreviews);
  };

  // [ 취소 버튼을 클릭한 경우 ]
  const handleCancel = () => {
    imagePreviews.forEach((url) => URL.revokeObjectURL(url));
    navigate("/posts");
  };

  return (
    <div className="page-container">
      <div className="post-form-container">
        <h1>새 게시글 작성</h1>

        <form onSubmit={handleSubmit}>
          {/* 카테고리 */}
          <div className="form-group">
            <label>카테고리</label>
            <div className="category-buttons">
              {Object.entries(POST_CATEGORIES).map(([key, value]) => (
                <div
                  key={key}
                  className={`category-button ${
                    selectedCategory === key ? "active" : ""
                  }`}
                  onClick={() => {
                    setSelectedCategory(key);
                  }}
                >
                  {value}
                </div>
              ))}
            </div>
          </div>

          {/* 게시글 제목 */}
          <div className="form-group">
            <label htmlFor="postTitle">제목</label>
            <input
              type="text"
              id="postTitle"
              name="title"
              value={title}
              onChange={(e) => {
                setTitle(e.target.value);
                setErrors((prev) => ({ ...prev, title: "" })); // 입력 시 에러 메시지 초기화
              }}
              placeholder="제목을 입력하세요"
              className={errors.title ? "error" : ""}
            />
            {/* 에러 메시지 표시 */}
            {errors.title && (
              <p className="error-message">{errors.title}</p>
            )}{" "}
          </div>

          {/* 게시글 내용 */}
          <div className="form-group">
            <label htmlFor="postContent">내용</label>
            <textarea
              id="postContent"
              name="content"
              rows="10"
              value={content}
              onChange={(e) => {
                setContent(e.target.value);
                setErrors((prev) => ({ ...prev, content: "" })); // 입력 시 에러 메시지 초기화
              }}
              placeholder="내용을 입력하세요"
              className={errors.content ? "error" : ""}
            ></textarea>
            {/* 에러 메시지 표시 */}
            {errors.content && (
              <p className="error-message">{errors.content}</p>
            )}{" "}
          </div>

          {/* 이미지 첨부파일 */}
          <div className="form-group">
            <label htmlFor="postImages">이미지 첨부</label>
            <input
              type="file"
              id="postImages"
              name="images"
              accept="image/*"
              multiple
              onChange={handleImageChange}
            />
            <p className="help-text">여러 개의 이미지를 선택할 수 있습니다.</p>
            {/* 에러 메시지 표시 */}
            {errors.image && <p className="error-message">{errors.image}</p>}
            {/* 이미지 미리보기 */}
            {imagePreviews.length > 0 && (
              <div className="selected-images-preview">
                <p>
                  선택된 이미지: {images.map((file) => file.name).join(", ")}
                </p>
                <div className="image-thumbnails">
                  {imagePreviews.map((url, index) => (
                    <img
                      key={index}
                      src={url}
                      alt={`Preview ${index}`}
                      className="thumbnail"
                    />
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* 게시글 등록 및 취소 버튼 */}
          <div className="form-actions">
            <button type="submit" className="submit-button">
              게시글 등록
            </button>
            <button
              type="button"
              onClick={handleCancel}
              className="cancel-button"
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default PostCreateForm;
