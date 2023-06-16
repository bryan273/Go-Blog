import { MdQuestionAnswer } from "react-icons/md";

const NotFound = ({ message }) => {
  return (
    <div className="flex flex-col items-center justify-between gap-y w-full">
      <MdQuestionAnswer className="text-slate-300 w-[360px] h-[360px]" />
      <h3 className="text-3xl text-gray-400 font-semibold tracking-wider">
        {message ?? "Not Found"}
      </h3>
    </div>
  );
};

export default NotFound;
