import ModalTemplate from "../../elements/ModalTemplate";
import { RxCross2 } from "react-icons/rx";
import { useRef, useState } from "react";
import { useUser } from "../../hooks/useUser";
import { createPost } from "../../../queries/post/createPost";
import {
  errorToast,
  successToast,
  expiredTokenToast,
} from "../../../utils/toast";
import Router from "next/router";

const CreatePostModal = ({ htmlFor, posts, postsSetter }) => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const { user, removeUser } = useUser();
  const closeRef = useRef(null);

  function closeModal() {
    setTitle("");
    setContent("");
    closeRef.current.click();
  }

  async function submitHandler() {
    let data = {
      title: title,
      content: content,
      creator: user.username,
      creatorId: user.userId,
    };

    const res = await createPost(data);

    if (res.status == 200) {
      if (posts && postsSetter) {
        postsSetter((posts) => [res.data, ...posts]);
      }
      successToast("your post has been created!");
      closeModal();
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
  }

  return (
    <ModalTemplate htmlFor={htmlFor}>
      <div className="w-full flex justify-between items-center">
        <h3 className="font-semibold text-2xl text-gray-800">
          Create new post
        </h3>
        <label
          ref={closeRef}
          htmlFor={htmlFor}
          className="cursor-pointer p-1 rounded-md hover:bg-gray-200 transition-all duration-100"
        >
          <RxCross2 className="w-7 h-7 text-gray-700" />
        </label>
      </div>
      <form
        className="w-full flex flex-col justify-start gap-y-4 mt-5"
        method="POST"
        onSubmit={(e) => e.preventDefault()}
      >
        <div className="flex flex-col items-start gap-y-1">
          <label className="font-semibold text-lg text-gray-600">Title</label>
          <input
            type="text"
            name="title"
            id="title"
            placeholder="title goes here"
            className="input h-[40px] w-full rounded-md focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600"
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>

        <div className="flex flex-col items-start gap-y-1">
          <label className="font-semibold text-lg text-gray-600">Content</label>
          <textarea
            type="text"
            name="text"
            id="text"
            placeholder="say something"
            style={{ resize: "none" }}
            className="textarea h-[300px] w-full rounded-md focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600"
            onChange={(e) => setContent(e.target.value)}
          />
        </div>
        <div className="w-full flex justify-end">
          <button
            className="p-2 px-4 rounded-[5px] bg-cyan-500 text-white hover:bg-cyan-600"
            type="submit"
            onClick={() => submitHandler()}
          >
            create
          </button>
        </div>
      </form>
    </ModalTemplate>
  );
};

export default CreatePostModal;
