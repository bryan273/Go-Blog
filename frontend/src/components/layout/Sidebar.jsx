import { useState, useEffect } from "react";
import { FaPlus } from "react-icons/fa";
import { FaBell } from "react-icons/fa";
import { FaTimes } from "react-icons/fa";
import { FaFileAlt } from "react-icons/fa";
import styles from "../../styles/sidebar.module.css";
import styles2 from "../../styles/modal.module.css";
import Modal from "../elements/Modal";
import { useRouter } from "next/router";
import CreatePostModal from "../modules/post/CreatePostModal";
import Link from "next/link";
import { getNewNotification } from "../../queries/notification/getNotification";
import { useUser } from "../hooks/useUser";
import { expiredTokenToast } from "../../utils/toast";

const Sidebar = ({ posts, postsSetter }) => {
  const [modalVisible, setModalVisible] = useState(false);
  const [activeButton, setActiveButton] = useState("Posts");
  const [notificationCounter, setNotificationCounter] = useState(0);

  const { pathname, replace } = useRouter();

  const { user, removeUser } = useUser();

  const fetchNewNotification = async () => {
    const res = await getNewNotification(user.userId);
    if (res.status == 200) {
      setNotificationCounter(
        (notificationCounter) => notificationCounter + (res.data.length ?? 0)
      );
    } else if (res.status == 401) {
      expiredTokenToast();
      setTimeout(() => {
        removeUser();
        replace("/auth/login");
      }, 2000);
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      if (user != null) {
        fetchNewNotification();
      }
    }, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleModalClose = () => {
    setModalVisible(false);
  };

  const handleCreatePostClick = () => {
    setModalVisible(true);
  };

  return (
    <div
      className={styles.sidebar}
      style={{ width: "calc(20vw - 50px)", height: "calc(100vh)" }}
    >
      <button href="#">
        <i className="fas fa-bars"></i>
      </button>
      <div>
        <div className="flex justify-center items-center py-6">
          <label
            htmlFor="create-post"
            className={`${styles.createPostButton} create-post-button bg-slate-400 text-white rounded-full p-3 hover:bg-slate-500 transition-colors flex items-center`}
            style={{ fontSize: "1.15rem" }}
            //onClick={handleCreatePostClick}
          >
            <FaPlus className="mr-2" /> Create Post
          </label>
        </div>
        <hr
          className={styles.separator}
          style={{ borderWidth: "4px", borderColor: "darkgray" }}
        ></hr>
        <ul className={styles.sidebarList}>
          <li>
            <a
              href="../home"
              className={`${styles.sidebarLink} ${
                activeButton === "Posts" && pathname != "/notification"
                  ? styles.active
                  : ""
              } flex items-center px-2 py-5`} // Modified
              onClick={() => setActiveButton("Posts")} // Modified
            >
              <FaFileAlt className={`${styles.icon} mr-2`} />
              <div className={styles.item}>Posts</div>
            </a>
          </li>
          <li>
            <Link
              href="/notification"
              className={`${styles.sidebarLink} ${
                activeButton === "Notifications" || pathname == "/notification"
                  ? styles.active
                  : ""
              } flex items-center px-2 py-5 relative`} // Modified
              onClick={() => {
                setActiveButton("Notifications");
                setNotificationCounter(0);
              }} // Modified
            >
              {notificationCounter > 0 ? (
                <>
                  <p className="h-[17px] w-[17px] rounded-full absolute bg-red-500 text-[13px] text-white text-center flex items-center justify-center left-3 top-3 animate-ping"></p>

                  <p className="h-[17px] w-[17px] rounded-full absolute bg-red-500 text-[13px] text-white text-center flex items-center justify-center left-3 top-3">
                    {notificationCounter}
                  </p>
                </>
              ) : (
                <></>
              )}
              <FaBell className={`${styles.icon} mr-2`} />
              <div className={styles.item}>Notifications</div>
            </Link>
          </li>
        </ul>
      </div>
      <CreatePostModal
        htmlFor={"create-post"}
        posts={posts}
        postsSetter={postsSetter}
      />
      <Modal show={modalVisible} onClose={handleModalClose}>
        <button className={styles2.closeButton} onClick={handleModalClose}>
          <FaTimes />
        </button>
        <div className={styles2.labelText}>
          <h2>Create New Post</h2>
        </div>
        <form>
          <div>
            <label htmlFor="title">Title</label>
          </div>
          <input type="text" id="title" className={styles2.inputField} />
          <div>
            <label htmlFor="content">Content</label>
          </div>
          <textarea id="content" className={styles2.inputFieldArea}></textarea>
          <div className="flex justify-end mt-4">
            <button type="submit" className={styles2.createButton}>
              Create
            </button>

            <div>
              <div className="flex justify-center items-center py-6">
                <button
                  className={`${styles.createPostButton} create-post-button bg-slate-400 text-white rounded-full p-3 hover:bg-slate-500 transition-colors flex items-center`}
                  style={{ fontSize: "1.15rem" }}
                  onClick={handleCreatePostClick}
                >
                  <FaPlus className="mr-2" /> Create Post
                </button>
              </div>
              <hr
                className={styles.separator}
                style={{ borderWidth: "4px", borderColor: "darkgray" }}
              ></hr>
              <ul className={styles.sidebarList}>
                <li>
                  <a
                    href="../home"
                    className={`${styles.sidebarLink} ${
                      activeButton === "Posts" ? styles.active : ""
                    } flex items-center px-2 py-5`} // Modified
                    onClick={() => setActiveButton("Posts")} // Modified
                  >
                    <FaFileAlt className={`${styles.icon} mr-2`} />
                    <div className={styles.item}>Posts</div>
                  </a>
                </li>
                <li>
                  <Link
                    href={"/notification"}
                    className={`${styles.sidebarLink} ${
                      activeButton === "Notifications" ? styles.active : ""
                    } flex items-center px-2 py-5`} // Modified
                    onClick={() => setActiveButton("Notifications")} // Modified
                  >
                    <FaBell className={`${styles.icon} mr-2`} />
                    <div className={styles.item}>Notifications</div>
                  </Link>
                </li>
              </ul>
            </div>
            <Modal show={modalVisible} onClose={handleModalClose}>
              <button
                className={styles2.closeButton}
                onClick={handleModalClose}
              >
                <FaTimes />

                <button
                  type="button"
                  className={styles2.cancelButton}
                  onClick={handleModalClose}
                >
                  Cancel
                </button>
              </button>
            </Modal>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Sidebar;
