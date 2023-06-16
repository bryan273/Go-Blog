import { toast } from "react-hot-toast";
import { RiErrorWarningLine } from "react-icons/ri";

export function errorToast(message) {
  toast.error(message, {
    style: {
      border: "1px solid #ff5b24",
      padding: "16px",
      color: "#ff5b24",
    },
    iconTheme: {
      primary: "#ff5b24",
      secondary: "#FFFAEE",
    },
  });
}

export function successToast(message) {
  toast.success(message, {
    style: {
      border: "1px solid #089c0d",
      padding: "16px",
      color: "#089c0d",
    },
    iconTheme: {
      primary: "#089c0d",
      secondary: "#FFFAEE",
    },
  });
}

export function expiredTokenToast() {
  toast.custom(
    (t) => (
      <div
        className={`${
          t.visible ? "animate-enter" : "animate-leave"
        } max-w-xl h-[250px] w-full bg-white shadow-lg rounded-lg pointer-events-auto ring-1 ring-black ring-opacity-5`}
      >
        <div className="w-full flex justify-start items-start p-6 rounded-md gap-y-6">
          <RiErrorWarningLine className="w-10 h-10 font-semibold text-gray-700" />
          <div className="ml-10 flex flex-col items-start justify-start gap-y-3">
            <h4 className="font-semibold text-gray-700 tracking-wider text-xl">
              Your token has expired
            </h4>
            <p className="text-gray-500"> logging you out...</p>
          </div>
        </div>
      </div>
    ),
    {
      duration: 2000,
    }
  );
}
