import React from "react";
import styles from "../../../styles/Notification.module.css";
import { RiUser3Line } from "react-icons/ri";
import Link from "next/link";

function NotificationCard({ notification }) {
  let { creator, timeCreated, commentContent, postId } = notification;
  const date = new Date(timeCreated);
  const options = { year: "numeric", month: "short", day: "numeric" };
  const formattedDate = date.toLocaleDateString(["en-Us", "id-ID"], options);

  return (
    <div className={styles.card + " border-b"}>
      <RiUser3Line className={styles.icon} />
      <div className={styles.info}>
        <p>
          <strong>{creator.username}</strong> {"commented "}{" "}
          {`"${
            commentContent.length >= 100
              ? commentContent.slice(99)
              : commentContent
          }"`}
        </p>
        <span className={styles.time}>{formattedDate}</span>
      </div>
      <Link href={"/post/" + postId}>
        <p className="text-blue-500 underline">see post</p>
      </Link>
    </div>
  );
}

function Notification({ notifications }) {
  return (
    <div className={styles.container + ""}>
      <div className={styles.notifications + " shadow-md"}>
        {notifications.map((notification, index) => (
          <NotificationCard key={notification} notification={notification} />
        ))}
      </div>
    </div>
  );
}

export default Notification;
