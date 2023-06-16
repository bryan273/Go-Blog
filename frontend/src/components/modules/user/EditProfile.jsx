import React from "react";
import { useRouter } from "next/router";
import { useState } from "react";
import { useUser } from "../../hooks/useUser";
import { editUserProfile } from "../../../queries/user/editUserProfile";
import {
  errorToast,
  successToast,
  expiredTokenToast,
} from "../../../utils/toast";

const EditProfile = ({ userData }) => {
  console.log(userData);

  const {user, removeUser, addUser} = useUser();


  const { username, fullname, email } = userData;

  const router = useRouter();
  const [showModal, setShowModal] = useState(false);

  const [profileData, setProfileData] = useState({
    fullname: fullname,
    username: username,
    email: email,
  });

  const [fullnameError, setFullnameError] = useState("");
  const [usernameError, setUsernameError] = useState("");
  const [emailError, setEmailError] = useState("");

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProfileData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
    setFullnameError("");
    setUsernameError("");
    setEmailError("");
  };


  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!profileData.fullname || profileData.fullname.trim() === "") {
      setFullnameError("Full name must be filled!");
    } else {
      setFullnameError("");
    }

    if (!profileData.username || profileData.username.trim() === "") {
      setUsernameError("Username must be filled!");
    } else {
      setUsernameError("");
    }

    if (!profileData.email || profileData.email.trim() === "") {
      setEmailError("Email must be filled!");
    } else {
      setEmailError("");
    }
    if (profileData.username && profileData.fullname) {
      userData.username = profileData.username;
      userData.fullname = profileData.fullname;

      const res = await editUserProfile(userData);

      if (res.status == 200) {
        let newUser = user
        newUser.username = res.data.username;
        removeUser()
        addUser(newUser)
        successToast("Your profile has been updated");
        router.replace("/user/profile");
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
    }
  };

  return (
    <div class="h-screen">
      <div class="flex h-screen justify-center pt-15 items-center">
        <div class="w-3/5 mx-auto p-5 border border-gray-300 rounded-lg bg-gray-100">
          <h1 className="font-bold text-xl text-gray-500 mb-2">Edit Profile</h1>
          <br></br>
          <form onSubmit={handleSubmit}>
            <div className="font-bold text-gray-500">Full Name</div>
            <input
              className="shadow appearance-none bg-cyan-50 border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              type="text"
              name="fullname"
              placeholder="Full Name"
              value={profileData.fullname}
              onChange={handleInputChange}
            ></input>
            {fullnameError && (
              <p className="error-message text-sm text-red-400">
                {fullnameError}
              </p>
            )}
            <div className="font-bold pt-2 text-gray-500">Username</div>
            <input
              className="shadow appearance-none bg-cyan-50 border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              type="text"
              name="username"
              placeholder="Username"
              value={profileData.username}
              onChange={handleInputChange}
            ></input>
            {usernameError && (
              <p className="error-message text-sm text-red-400">
                {usernameError}
              </p>
            )}
            <div className="font-bold pt-2 text-gray-500">Email</div>
            <input
              disabled={true}
              className="disabled shadow appearance-none bg-cyan-50 border rounded w-full py-2 px-3 text-gray-400 leading-tight focus:outline-none focus:shadow-outline"
              type="text"
              name="email"
              placeholder="Email"
              value={profileData.email}
              onChange={handleInputChange}
            ></input>
            {emailError && (
              <p className="error-message text-sm text-red-400">{emailError}</p>
            )}
            <div class="flex pt-5">
              <div className="flex flex-row ml-auto gap-3">
                <button
                  className="bg-cyan-500 p-2 text-white rounded-md hover:bg-cyan-600"
                  onClick={() => {
                    if (
                      profileData.fullname &&
                      profileData.email &&
                      profileData.username &&
                      profileData.aboutme
                    ) {
                      setShowModal(true);
                    }
                  }}
                >
                  Save Profile
                </button>
                <p
                  className="border border-red-500 p-2 text-red-500 rounded-md hover:bg-gray-300"
                  onClick={() => router.back()}
                >
                  Cancel
                </p>
              </div>
            </div>
          </form>
          {showModal ? (
            <div className="fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white border-2 border-grey-800 rounded-lg shadow-xl w-[300px] h-[150px] p-4">
              <div className="flex flex-col justify-center items-center h-full gap-3">
                <h1 className="text-base mt-2 mx-4 text-gray-400 font-semibold text-center">
                  Successfully edited your profile
                </h1>
                <button
                  className="bg-cyan-500 p-2 text-white rounded-md hover:bg-cyan-600"
                  onClick={() => setShowModal(false)}
                >
                  Close
                </button>
              </div>
            </div>
          ) : null}
        </div>
      </div>
    </div>
  );
};
export default EditProfile;
