import { useRef } from "react";
import { expiredTokenToast, errorToast, successToast } from "../../utils/toast";
import { useUser } from "../hooks/useUser";
import { useRouter } from "next/router";

const DeleteModal = ({
  htmlFor,
  isPost,
  deleteCallback,
  postId,
  posts,
  postsSetter,
  index,
}) => {
  const { removeUser } = useUser();
  const closeModalRef = useRef(null);

  const { pathname, replace } = useRouter();
  const handleDelete = async () => {
    const res = await deleteCallback(postId);

    if (res.status == 200) {
      if (posts && postsSetter) {
        let postList = [...posts];
        postList.splice(index, 1);
        postsSetter(postList);
      }

      successToast(`Your ${isPost ? "post" : "comment"} has been deleted`);
      closeModalRef.current.click();
      if (isPost && pathname != "/home") {
        replace("/home");
      }
    } else if (res.status == 401) {
      expiredTokenToast();

      setTimeout(() => {
        removeUser();
        Router.replace("/auth/login");
      }, 2000);
    } else if (res.status >= 400) {
      errorToast(res.message);
    } else {
      errorToast("sorry we're unable to process your request");
    }
  };

  return (
    <div>
      <input type="checkbox" id={htmlFor} className="modal-toggle" />
      <div className="modal modal-bottom sm:modal-middle">
        <div className="modal-box bg-white flex flex-col items-start justify-start">
          <h4 className="font-bold text-gray-700 text-xl">{`Delete ${
            isPost ? "Post" : "Comment"
          }`}</h4>

          <p className="text-[16px] mb-4">
            {` Are you sure you want to delete this ${
              isPost ? "post" : "comment"
            }? Deleted ${
              isPost ? "post" : "comment"
            } can't be restored in any ways in the future.`}
          </p>
          <div className="w-full flex justify-center items-center gap-x-4">
            <button
              onClick={() => handleDelete()}
              className="p-3 rounded-md border border-red-500 text-red-500 bg-white hover:bg-gray-100 transition-all duration-100"
            >
              Delete
            </button>
            <label
              ref={closeModalRef}
              htmlFor={htmlFor}
              className=" p-3 rounded-md border text-white bg-slate-900 cursor-pointer"
            >
              Cancel
            </label>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DeleteModal;
