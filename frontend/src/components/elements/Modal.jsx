// Modal.js
import React from "react";
import styles from "../../styles/modal.module.css";

const Modal = ({ show, onClose, children }) => {
  if (!show) {
    return null;
  }

  return (
    <div className={styles.modalBackdrop} onClick={onClose}>
      <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
        <div className={styles.contentContainer}>{children}</div>
      </div>
    </div>
  );
};

export default Modal;
