import Sidebar from "@components/layout/Sidebar";
import Notification from "@components/modules/post/Notification";
import { useRouter } from "next/router";
import { useUser } from "../components/hooks/useUser";
import {
  getNewNotification,
  getOldNotification,
} from "../queries/notification/getNotification";
import { useState, useEffect } from "react";
import { expiredTokenToast, errorToast } from "../utils/toast";
import Router from "next/router";
import Loader from "../components/elements/Loader";
import NoNotification from "../components/elements/NoNotification";

const Notifications = () => {
  const router = useRouter();
  const { user, removeUser } = useUser();
  const [notifications, setNotifications] = useState(null);

  useEffect(() => {
    const getNotif = async () => {
      const res = await getOldNotification(user.userId);

      if (res.status == 200) {
        setNotifications(res.data);
      } else if (res.status == 401) {
        expiredTokenToast();

        setTimeout(() => {
          removeUser();
          Router.replace("/auth/login");
        }, 2000);
      } else if (res.status >= 400) {
        errorToast(res.message);
      } else {
        errorToast("unknown error while processing your request.");
      }
    };

    if (user != null) {
      getNotif();
    }
  }, []);

  if (!user) {
    router.push("/auth/login");
    return;
  }

  return (
    <main
      className="bg-gray-100"
      style={{
        height: "80vh",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div style={{ marginTop: "4rem" }}>
        <div
          className="sidebar"
          style={{ backgroundColor: "#eee", position: "fixed", top: "4rem" }}
        >
          <Sidebar />
        </div>
        <div
          className=""
          style={{
            padding: "50px",
            width: "calc(80vw)",
            marginLeft: "calc(20vw - 50px)",
          }}
        >
          {notifications == null ? (
            <Loader fullscreen={true} />
          ) : notifications.length == 0 ? (
            <NoNotification />
          ) : (
            <Notification notifications={notifications} />
          )}
        </div>
      </div>
    </main>
  );
};
export default Notifications;
