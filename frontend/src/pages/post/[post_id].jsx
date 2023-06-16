import Sidebar from "@components/layout/Sidebar";
import { useRouter } from "next/router";
import { useUser } from "../../components/hooks/useUser";
import styles from "../../styles/Post.module.css";
import CommentSection from "@components/modules/post/CommentSection";
import Post from "@components/modules/post/Post";
import { useEffect, useState } from "react";
import { getPostById } from "../../queries/post/getPostById";
import { errorToast, expiredTokenToast } from "../../utils/toast";
import Loader from "../../components/elements/Loader";

export default function PostDetail() {
  const { query, isReady, replace } = useRouter();
  const { user, removeUser } = useUser();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);

  useEffect(() => {
    if (!isReady) {
      return;
    }

    const fetchPost = async () => {
      const res = await getPostById(query.post_id);

      if (res.status == 200) {
        setPost(res.data.post);
        setComments(res.data.comments);
      } else if (res.status == 401) {
        expiredTokenToast();

        setTimeout(() => {
          removeUser();
          replace("/auth/login");
        }, 2000);
      } else if (res.status >= 400) {
        errorToast(res.message);
      } else {
        errorToast("unknown error while processing your request.");
      }
    };

    fetchPost();
  }, [isReady]);

  if (!user) {
    router.push("/");
  }
  return post == null ? (
    <Loader fullscreen={true} />
  ) : (
    <main className=" bg-gray-100 items-center justify-center">
      <div style={{ marginTop: "4rem" }}>
        <div
          className="sidebar"
          style={{ backgroundColor: "#eee", position: "fixed", top: "4rem" }}
        >
          <Sidebar />
        </div>
        <div
          className="content flex-grow"
          style={{
            padding: "50px",
            width: "calc(80vw)",
            marginLeft: "calc(20vw - 50px)",
          }}
        >
          <div className="bg-white py-4 shadow-xl rounded-md">
            <div className="flex flex-row items-start">
              <div className={styles.col}>
                <Post
                  key={post.postId}
                  postData={post}
                  posts={undefined}
                  postsSetter={undefined}
                  index={undefined}
                  showLikeAndCommentIcon={false}
                  truncateContent={false}
                />
              </div>
              <div className="w-1/2 px-5">
                <CommentSection
                  postId={post.postId}
                  comments={comments}
                  commentsSetter={setComments}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
