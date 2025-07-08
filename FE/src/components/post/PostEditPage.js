import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import useAuthToken from "../../hooks/useAuthToken";
import { useNavigate, useParams } from "react-router-dom";
import "../../assets/css/PostAddPage.css";
import "../../assets/css/PostEditPage.css";

// 카테고리 Enum 값
const POST_CATEGORIES = {
  ETC: "잡담",
  INFORMATION: "정보",
  QUESTION: "질문",
  REVIEW: "후기",
};

function PostEditForm() {
  const navigate = useNavigate();
  const { getToken } = useAuthToken();

  const { postId } = useParams();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("ETC");
  const [existingImages, setExistingImages] = useState([]);

  const [newImages, setNewImages] = useState([]); // 새로 추가할 이미지 파일
  const [newImagePreviews, setNewImagePreviews] = useState([]); // 새로 추가할 이미지 파일의 미리보기 URL
  const [imagesToDelete, setImagesToDelete] = useState([]); // 삭제할 이미지 파일의 ID 목록

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

  // [ 게시글 데이터 불러오기 ]
  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await fetch(`/api/posts/${postId}/form`);

        if (response.status === 200) {
          const data = await response.json();

          // 폼 데이터 세팅
          setTitle(data.title);
          setContent(data.content);
          setSelectedCategory(data.category);
          setExistingImages(data.images || []);
        } else {
          const errorData = await response.json();

          alert(errorData.message || "게시글 정보를 불러오는데 실패했습니다.");
          navigate("/posts");
          return;
        }
      } catch (err) {
        console.error("게시글 불러오기 오류:", err);
        alert(`게시글 정보를 불러오는데 실패했습니다: ${err.message}`);

        navigate("/posts");
      }
    };

    fetchPost();
  }, [postId, navigate]);

  // [ 게시글 수정 버튼을 클릭한 경우 ]
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

    imagesToDelete.forEach((uuid) => {
      formData.append("imagesToDelete", uuid);
    });

    newImages.forEach((file) => {
      formData.append("newImages", file);
    });

    // API 호출 (게시글 수정)
    try {
      await axios.put(`/api/posts/${postId}`, formData, {
        headers: {
          Authorization: `Bearer ${atk}`,
        },
      });

      alert("게시글이 성공적으로 수정되었습니다.");
      navigate(`/posts/${postId}`);
    } catch (error) {
      console.error("게시글 수정 중 오류 발생:", error);
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
        alert(`게시글 수정 실패: ${error.message}`);
      }
    }
  };

  // [ 첨부파일이 변경된 경우 ]
  const handleNewImageChange = (e) => {
    // 기존 미리보기 URL 해제
    newImagePreviews.forEach((url) => URL.revokeObjectURL(url));

    // 새로운 이미지 파일 저장
    const files = Array.from(e.target.files);
    setNewImages(files);

    // 에러 메시지 초기화
    setErrors((prev) => ({ ...prev, image: "" }));

    // 미리보기 URL 생성
    const previews = files.map((file) => URL.createObjectURL(file));
    setNewImagePreviews(previews);
  };

  // [ 기존 이미지를 삭제하는 경우 ]
  const handleRemoveExistingImage = (postImageId) => {
    setExistingImages((prevImages) =>
      prevImages.filter((img) => img.postImageId !== postImageId)
    );
    setImagesToDelete((prevIds) => [...prevIds, postImageId]);
  };

  // [ 취소 버튼을 클릭한 경우 ]
  const handleCancel = () => {
    newImagePreviews.forEach((url) => URL.revokeObjectURL(url));
    navigate(`/posts/${postId}`);
  };

  return (
    <div className="page-container">
      <div className="post-form-container">
        <h1>게시글 수정</h1>

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
                setErrors((prev) => ({ ...prev, title: "" }));
              }}
              placeholder="제목을 입력하세요"
              className={errors.title ? "error" : ""}
            />
            {errors.title && <p className="error-message">{errors.title}</p>}{" "}
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
                setErrors((prev) => ({ ...prev, content: "" }));
              }}
              placeholder="내용을 입력하세요"
              className={errors.content ? "error" : ""}
            ></textarea>
            {errors.content && (
              <p className="error-message">{errors.content}</p>
            )}{" "}
          </div>

          {/* 이미지 첨부파일 */}
          <div className="form-group">
            <label htmlFor="postImages">이미지 첨부</label>
            {/* 기존 이미지 미리보기 및 삭제 */}
            {existingImages.length > 0 && (
              <div className="selected-images-preview">
                <p>기존 이미지:</p>
                <div className="image-thumbnails">
                  {existingImages.map((img) => (
                    <div key={img.postImageId} className="thumbnail-wrapper">
                      <img
                        src={img.imageUrl}
                        alt={`기존 이미지 ${
                          img.originalImageName || img.postImageId
                        }`}
                        className="thumbnail"
                      />
                      {/* 이미지 삭제 버튼 */}
                      <button
                        type="button"
                        className="remove-image-button"
                        onClick={() =>
                          handleRemoveExistingImage(img.postImageId)
                        }
                      >
                        X
                      </button>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* 새 이미지 파일 첨부 */}
            <input
              type="file"
              id="postImages"
              name="newImages"
              accept="image/*"
              multiple
              onChange={handleNewImageChange}
            />
            <p className="help-text">
              새로운 이미지를 여러 개 선택할 수 있습니다.
            </p>
            {errors.image && <p className="error-message">{errors.image}</p>}

            {/* 새로 추가할 이미지 미리보기 */}
            {newImagePreviews.length > 0 && (
              <div className="selected-images-preview">
                <p>새로 추가할 이미지:</p>
                <div className="image-thumbnails">
                  {newImagePreviews.map((url, index) => (
                    <img
                      key={url}
                      src={url}
                      alt={`새 이미지 미리보기 ${index}`}
                      className="thumbnail"
                    />
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* 게시글 수정 및 취소 버튼 */}
          <div className="form-actions">
            <button type="submit" className="submit-button">
              게시글 수정
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

export default PostEditForm;
