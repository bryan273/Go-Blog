import Head from "next/head";
import styles from "../styles/Home.module.css";

export default function Home() {
  return (
    <div className={styles.container1}>
      <Head>
        <title>Go-Blog | Welcome </title>
        <meta
          name="description"
          content="go-blog is a forum post built for everyone to share their interests. 
        Come join us and be the first person to experience our community!"
        />
        <link rel="icon" href="/assets/title logo.jpg" />
      </Head>

      <main className={styles.main}>
        <h1 className={styles.title}>Welcome to Go-Blog!</h1>

        <p className={styles.description}>Let's get started</p>
      </main>

      <section className={styles.hidden}>
        <div
          className={styles.flexbox}
          style={{
            backgroundImage: `url("/assets/bg blog 2.jpg")`,
            height: "100%",
            backgroundPosition: "center",
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
          }}
        >
          <div className={styles.first}>
            <div className={styles.row}>
              <div className={styles.col}>
                <h1 className={styles.h1}>Create Your Post</h1>
                <p>
                  Publish insights, thoughts, and stories on your own blog about
                  any topic
                </p>{" "}
                <br />
                <a href="/home">
                  <button
                    className="create-post-button bg-cyan-500 text-white rounded-full p-3 hover:bg-cyan-600 transition-colors flex items-center"
                    style={{ fontSize: "1.3rem" }}
                    hr
                  >
                    Create Post
                  </button>
                </a>
              </div>
              <div className={styles.col}></div>
            </div>
          </div>
        </div>
      </section>

      <section className={styles.hidden}>
        <div className={styles.flexbox}>
          <div className={styles.second}>
            <div className={styles.row}>
              <div className={styles.col}>
                <img src="/assets/explore.jpg" alt="" />
              </div>
              <div className={styles.col}>
                <div className={styles.h1w}>
                  <a href="/home">
                    <button
                      className="create-post-button bg-yellow-500 text-white rounded-full p-3 hover:bg-cyan-600 transition-colors flex items-center"
                      style={{ fontSize: "2rem" }}
                      hr
                    >
                      Explore
                    </button>
                  </a>
                </div>
                <p className="text-white">
                  Explore other's posts, leave a like and comments. Also, don't
                  forget to subscribe if you want to get updates from them.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
