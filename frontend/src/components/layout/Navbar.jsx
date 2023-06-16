import SearchBar from "@components/elements/SearchBar";
import LinkedButton from "../elements/LinkedButton";
import { useUser } from "../hooks/useUser";
import UserDropdown from "../elements/UserDropdown";
import { useEffect } from "react";

const Navbar = ({ onRender }) => {
  useEffect(() => {
    onRender();
  }, [onRender]);

  const { user } = useUser();
  return (
    // <div className="w-full navbar bg-white  drop-shadow-lg justify-between">
    <div
      className="w-full navbar flex justify-between items-center gap-3"
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        backgroundColor: "#fff",
        boxShadow: "0px 2px 6px rgba(0, 0, 0, 0.1)",
        zIndex: 1000,
      }}
    >
      <div className="flex-none lg:hidden">
        <label htmlFor="my-drawer-3" className="btn btn-square btn-ghost">
          <SidebarSVG />
        </label>
      </div>

      <a href={user != null ? "/home" : "/"}>
        <img src="/assets/logo.jpg" alt="Logo Go-Blog" />
      </a>

      {user != null && (
        <div className="md:w-1/2 lg:w-1/3">
          <SearchBar />
        </div>
      )}

      <div className="flex-none hidden lg:block">
        <ul className="menu menu-horizontal text-black gap-x-3 justify-end">
          {user != null && user != {} ? (
            <UserDropdown />
          ) : (
            <>
              <LinkedButton link={"/auth/login"} title={"login"} />
              <LinkedButton link={"/auth/register"} title={"register"} />
            </>
          )}
        </ul>
      </div>
    </div>
  );
};

const SidebarSVG = () => {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      className="inline-block w-6 h-6 stroke-current"
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth="2"
        d="M4 6h16M4 12h16M4 18h16"
      ></path>
    </svg>
  );
};

export default Navbar;
