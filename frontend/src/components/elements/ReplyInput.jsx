import { IoIosSend } from "react-icons/io";
import { errorToast, successToast, expiredTokenToast } from "../../utils/toast";
import { useRef, useState } from "react";
import { useRouter } from "next/router";
import { createComment } from "../../queries/comment/createComment";
import { useUser } from "../hooks/useUser";

const ReplyInput = ({ postId, allComment, allCommentSetter }) => {
  const [comment, setComment] = useState("");
  const { user } = useUser();
  const replyInputRef = useRef(null);

  const { replace } = useRouter();

  const handleSubmit = async () => {
    if (comment.length <= 0) {
      errorToast("comment value can't be empty!");
      return;
    }
    if (comment.length >= 255) {
      errorToast("maximum length is 255 characters");
      return;
    }

    const data = {
      content: comment,
      creator: user.username,
      creatorId: user.userId,
      postId: postId,
    };

    const res = await createComment(data);

    if (res.status == 200) {
      allCommentSetter((allComment) => [...allComment, res.data]);
      replyInputRef.current.value = "";

      successToast("your comment has been sent");
    } else if (res.status == 401) {
      expiredTokenToast();

      setTimeout(() => {
        removeUser();
        replace("/auth/login");
      }, 2000);
    } else if (res.status >= 400) {
      errorToast(res.message);
    } else {
      errorToast("unknown error while processing your request.");
    }
  };

  return (
    <div className=" w-full">
      <div className="flex flex-col relative w-full">
        <input
          ref={replyInputRef}
          onChange={(e) => setComment(e.target.value)}
          type="text"
          placeholder="say something"
          className={
            "input h-[40px] rounded-[5px] pr-14 focus:outline-none border-2 border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600"
          }
        ></input>
        <button
          onClick={handleSubmit}
          className="absolute right-0 bg-cyan-500 p-3 py-2 rounded-r-[5px] hover:bg-cyan-600 h-full"
        >
          <IoIosSend className="text-white text-base h-6" />
        </button>
      </div>
    </div>
  );
};
export default ReplyInput;
