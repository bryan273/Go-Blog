import Sidebar from "@components/layout/Sidebar";
import Post from "@components/modules/post/Post";
import Router from "next/router";
import styles from "../styles/Post.module.css";
import { useUser } from "../components/hooks/useUser";
import { useEffect, useState } from "react";
import { getAllPost } from "../queries/post/getAllPost";
import { errorToast, expiredTokenToast } from "../utils/toast";
import NotFound from "../components/elements/NotFound";
import Loader from "../components/elements/Loader";

const Home = () => {
  const { user, removeUser } = useUser();
  const [posts, setPosts] = useState(null);

  useEffect(() => {
    const fetchAllPost = async () => {
      const res = await getAllPost();
      if (res.status == 200) {
        setPosts(res.data);
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

    if (user != null) {
      fetchAllPost();
    }
  }, []);

  if (user == null) {
    Router.replace("/auth/login");
    return;
  }
  if (user == null) {
    replace("/auth/login");
    return;
  }

  return (
    <main className=" bg-gray-100 items-center justify-center">
      <div style={{ marginTop: "4rem" }}>
        <div
          className="sidebar"
          style={{ backgroundColor: "#eee", position: "fixed", top: "4rem" }}
        >
          <Sidebar posts={posts} postsSetter={setPosts} />
        </div>
        {posts == null ? (
          <Loader fullscreen={true} />
        ) : (
          <div
            className="content flex flex-col items-center w-full"
            style={{
              padding: "50px",
              width: "calc(80vw)",
              marginLeft: "calc(20vw - 50px)",
            }}
          >
            {posts.length == 0 ? (
              <NotFound message={"Buat post pertamamu!"} />
            ) : (
              <div className="bg-white border-x border-b-1 min-w-[512px]">
                <div className={styles.container + " border-b"}>
                  <div className={styles.col}>
                    {posts.map((post, index) => (
                      <Post
                        key={post.postId}
                        postData={post}
                        posts={posts}
                        postsSetter={setPosts}
                        index={index}
                        showLikeAndCommentIcon={true}
                        truncateContent={true}
                      />
                    ))}
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </main>
  );
};
export default Home;
