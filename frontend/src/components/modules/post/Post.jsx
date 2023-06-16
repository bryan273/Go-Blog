import React, { useState } from "react";
import { RiHeartLine, RiChat1Line } from "react-icons/ri";
import styles from "../../../styles/Post.module.css";
import { RxAvatar } from "react-icons/rx";
import DotsDropdown from "../../elements/DotsDropdown";
import Router from "next/router";
import { useUser } from "../../hooks/useUser";

function Post({
  postData,
  posts,
  postsSetter,
  index,
  showLikeAndCommentIcon,
  truncateContent,
}) {
  let { creator, title, content, postId, likes, timeCreated } = postData;

  const date = new Date(timeCreated);
  const options = { year: "numeric", month: "short", day: "numeric" };
  const formattedDate = date.toLocaleDateString(["en-Us", "id-ID"], options);

  const [numLikes, setNumLikes] = useState(likes); // add state for numLikes
  const [liked, setLiked] = useState(false); // add state for liked status
  const { user } = useUser();

  const toggleLike = () => {
    if (liked) {
      setNumLikes(numLikes - 1); // decrement numLikes
      setLiked(false); // set liked status to false
    } else {
      setNumLikes(numLikes + 1); // increment numLikes
      setLiked(true); // set liked status to true
    }
  };

  return (
    <article
      className="w-full border-b p-3 py-5 cursor-pointer"
      onClick={() => {
        if (showLikeAndCommentIcon) {
          Router.push(`/post/${postId}`);
        }
      }}
    >
      <div className="flex w-full items-start gap-x-2">
        <RxAvatar className="text-gray-500 w-12 h-12" />
        <div className="w-full flex flex-col gap-y-3">
          <div className="flex items-center justify-start">
            <div className="flex justify-between items-start w-full">
              <div className="flex flex-col items-start">
                <div className="flex items-center justify-start gap-x-2">
                  <p className="text-[17px] font-semibold text-gray-700">
                    {creator.username}
                  </p>
                  <p className="text-[15px] mt-1">{formattedDate}</p>
                </div>
                <p className="text-[15px]">{"@" + creator.username}</p>
              </div>
              {creator.id === user.userId ? (
                <div onClick={(e) => e.stopPropagation()}>
                  <DotsDropdown
                    postData={postData}
                    posts={posts}
                    postsSetter={postsSetter}
                    index={index}
                    isPost={true}
                  />
                </div>
              ) : (
                <></>
              )}
            </div>
          </div>
          <article className="py-2 flex flex-col justify-start items-start">
            <h3 className="text-xl font-semibold text-gray-600 mb-2">
              {" "}
              {title}
            </h3>
            <p className="">
              {truncateContent && content.length >= 255
                ? content.slice(254) + "..."
                : content}
            </p>
          </article>

          {showLikeAndCommentIcon && (
            <div className="flex items-center justify-start gap-x-2">
              <button
                className={`${styles[liked ? "like-btn" : "unlike-btn"]}`}
                onClick={(e) => {
                  e.stopPropagation();
                  toggleLike();
                }}
              >
                <RiHeartLine className={styles["like-icon"]} /> {numLikes}
              </button>
              <button className={styles["comment-btn"]}>
                <RiChat1Line /> {0}
              </button>
            </div>
          )}
        </div>
      </div>
    </article>
  );
}
//   return (
//     <div className="">
//       <div className={styles["post-header"] + " w-full"}>
//         <img
//           className={styles["profile-img"]}
//           src={profileImg}
//           alt="Author Profile"
//         />
//         <div className={styles["post-author"]}>
//           <h1 className={styles["post-author-name"]}>{author}</h1>
//           <p className={styles["post-username"]}>{username}</p>
//         </div>
//         <div className="place-self-end">
//           halo
//         </div>
//       </div>
//       <div className={styles.post + " rounded-none"}>
//         <a
//           href="/comments"
//           onClick={(e) => {
//             e.preventDefault();
//             window.location.href = "/comments";
//           }}
//         >
//           {/* <img className={styles["post-img"]} src={postImg} alt="Post Image" /> */}
//         </a>
//         <div className={styles["post-footer"] + " rounded-none"}>
//           <div className={styles["post-words"]}>
//             <div>
//               <h3 className={styles["post-title"]}>{title}</h3>
//               <p className={styles["post-date"]}>{date}</p>
//             </div>
//           </div>
//           <div className={styles["post-actions"]}>
//             <button
//               className={`${styles[liked ? "like-btn" : "unlike-btn"]}`}
//               onClick={toggleLike}
//             >
//               <RiHeartLine className={styles["like-icon"]} /> {numLikes}
//             </button>
//             <button className={styles["comment-btn"]}>
//               <RiChat1Line /> {comments}
//             </button>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// }

export default Post;
