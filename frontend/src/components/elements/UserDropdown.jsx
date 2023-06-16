import Image from "next/image";
import { BsChevronDown, BsFillPersonFill } from "react-icons/bs";
import { ImExit } from "react-icons/im";
import { useUser } from "../hooks/useUser";
import Link from "next/link";
import { useRouter } from "next/router";

const UserDropdown = () => {
  const { removeUser, user } = useUser();
  const router = useRouter();

  function logout() {
    removeUser();
    router.replace("/auth/login");
  }

  return (
    <div className="dropdown dropdown-hover dropdown-end ">
      <label
        tabIndex={0}
        className=" flex items-center justify-between  gap-x-3 p-2 rounded-md cursor-pointer hover:bg-gray-200 transition-all duration-100"
      >
        <div className="rounded-full w-10 h-10 relative">
          <Image
            className="rounded-full"
            fill={true}
            src="/assets/nextjs.png"
          />
        </div>

        <div className="flex items-center gap-x-2">
          <p>{user.username ?? "guest"}</p>
          <BsChevronDown className="w-4 h-4 mt-1" />
        </div>
      </label>
      <div className="pt-2">
        <ul
          tabIndex={0}
          className="bg-white dropdown-content menu p-2 shadow  rounded-box w-52 "
        >
          <li>
            <Link
              href={"/user/profile"}
              className="w-full flex items-center hover:bg-gray-200 transition-all duration-100"
            >
              <BsFillPersonFill className="w-7 h-7" />
              <p>Profile</p>
            </Link>
          </li>
          <li>
            <div
              className="w-full flex items-center text-red-600 hover:bg-gray-200 transition-all duration-100"
              onClick={() => logout()}
            >
              <ImExit className="w-6 h-6 ml-1" />
              <p>logout</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default UserDropdown;
