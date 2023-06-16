import Link from "next/link";

const UserProfile = ({ userData }) => {
  const { username, fullname, email, description } = userData;

  return (
    <div className="w-full mx-auto p-5 border border-gray-300 rounded-lg bg-gray-100">
      <h1 className="font-bold text-xl mb-2">Your Profile</h1>
      <div className="flex flex-row pt-2 pb-3">
        <div class="flex flex-col w-full">
          <div className="font-bold pl-5">Full Name</div>
          <div class="flex pl-5">
            <div
              className="shadow bg-cyan-50 appearance-none border rounded w-full h-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="username"
              type="text"
              placeholder="Username"
            >
              <h1>{fullname}</h1>
            </div>
          </div>
          <div className="font-bold pl-5 pt-3">Username</div>
          <div className="flex pl-5">
            <div
              className="shadow bg-cyan-50 appearance-none border rounded w-full h-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="username"
              type="text"
              placeholder="Username"
            >
              <h1>{username}</h1>
            </div>
          </div>
          <div className="font-bold pl-5 pt-3">Email</div>
          <div className="flex pl-5">
            <div
              className="shadow bg-cyan-50 appearance-none border rounded w-full h-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              id="username"
              type="text"
              placeholder="Username"
            >
              <h1>{email}</h1>
            </div>
          </div>
          <div className="flex pt-14">
            <Link className="flex flex-row ml-auto" href={"/user/edit"}>
              <button className="bg-cyan-500 p-2 text-white rounded-md hover:bg-cyan-600">
                Edit Profile
              </button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
