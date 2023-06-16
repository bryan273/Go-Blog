import { AiFillNotification } from "react-icons/ai";

const NoNotification = () => {
  return (
    <div className="flex flex-col items-center justify-between gap-y w-full">
      <AiFillNotification className="text-slate-300 w-[360px] h-[340px]" />
      <h3 className="text-3xl text-gray-400 font-semibold tracking-wider">
        {"You have no new notification"}
      </h3>
    </div>
  );
};

export default NoNotification;
