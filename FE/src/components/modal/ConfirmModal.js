import React, { useEffect, useRef } from "react";
import "../../assets/css/ConfirmModal.css";

export default function ConfirmModal({ message, onConfirm, onCancel }) {
  const modalRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (modalRef.current && !modalRef.current.contains(event.target)) {
        onCancel(); // 바깥 누르면 닫기
      }
    };
      
    const handleKeyDown = (event) => {
      if (event.key === "Escape") {
        onCancel(); // Esc 키 누르면 닫기
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [onCancel]);


  return (
    <div className="modal-overlay">
      <div className="modal-content" ref={modalRef}>
        <p>{message}</p>
        <div className="modal-buttons">
          <button onClick={onConfirm}>네</button>
          <button onClick={onCancel}>아니오</button>
        </div>
      </div>
    </div>
  );
}