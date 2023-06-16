import { FaComments } from "react-icons/fa";

const NoComment = () => {
  return (
    <div className="w-full flex flex-col justify-center items-center gap-y-4 h-[450px]">
      <FaComments className="text-gray-300 w-[200px] h-[200px]" />
      <p className="font-semibold text-gray-400 text-xl">
        Be the first to leave comment!
      </p>
    </div>
  );
};

export default NoComment;
