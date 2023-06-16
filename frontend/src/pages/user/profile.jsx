import { useEffect, useState } from "react";
import { useRouter } from "next/router";
import { useUser } from "../../components/hooks/useUser";
import UserProfile from "../../components/modules/user/UserProfile.jsx";
import BackButton from "../../components/elements/BackButton";
import { getUserProfile } from "../../queries/user/getUserProfile";
import { errorToast, expiredTokenToast } from "../../utils/toast";
import Loader from "../../components/elements/Loader";

const UserProfilePage = () => {
  const { user, removeUser } = useUser();
  const router = useRouter();
  const [userData, setUserData] = useState(null);

  const fetchUser = async () => {
    const res = await getUserProfile();

    setTimeout(() => {
      if (res.status == 200) {
        setUserData(res.data);
      } else if (res.status == 401) {
        expiredTokenToast();
        setTimeout(() => {
          removeUser();
          router.replace("/auth/login");
        }, 2000);
      } else if (res.status >= 400) {
        errorToast(res.message);
      } else {
        errorToast("unknown error while processing your request.");
      }
    }, 500);
  };

  useEffect(() => {
    if (!user) router.replace("/");
    if (user) fetchUser();
  }, []);

  if (!user) return <></>;

  return userData == null ? (
    <Loader fullscreen={true} />
  ) : (
    <div className="h-screen">
      <div className="flex h-screen justify-center pt-16 items-center">
        <main className=" w-3/5 flex flex-col justify-start items-start gap-y-10">
          <BackButton />

          <UserProfile userData={userData} />
        </main>
      </div>
    </div>
  );
};

export default UserProfilePage;
