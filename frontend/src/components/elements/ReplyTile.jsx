import styles from "@styles/Comment.module.css";
import { RxAvatar } from "react-icons/rx";
import DotsDropdown from "./DotsDropdown";
import { useUser } from "../hooks/useUser";

const ReplyTile = ({ comment, index, comments, commentsSetter }) => {
  const date = new Date(comment.timeCreated);
  const options = { year: "numeric", month: "short", day: "numeric" };
  const formattedDate = date.toLocaleDateString(["en-Us", "id-ID"], options);

  const { user } = useUser();

  return (
    <div className="mb-10 mr-4">
      <div className={"w-full flex justify-between"}>
        <div className=" flex items-start">
          <RxAvatar className="text-emerald-600 w-10 h-10" />
          <div className="flex flex-col items-start ml-3 justify-start">
            <p className="text-gray-700 font-semibold text-[17px]">
              {comment.creator.username}
            </p>
            <p className="text-[15px]">{"@" + comment.creator.username}</p>
          </div>
          <p className="ml-1 mt-[0.5px] text-[14px]">{formattedDate}</p>
        </div>
        {comment.creator.id == user.userId && (
          <DotsDropdown
            isPost={false}
            postData={comment}
            posts={comments}
            postsSetter={commentsSetter}
            index={index}
          />
        )}
      </div>
      <div className={styles["comment-content"] + " text-justify"}>
        {comment.content}
      </div>
    </div>
  );
};

export default ReplyTile;
