import postApiClient from "../../utils/postApiClient";

export async function deletePost(postId) {
  const res = postApiClient
    .delete(`/delete_post/${postId}`)
    .then((response) => {
      //
      return response;
    })
    .catch((error) => {
      //
      return {
        status: error.response.status,
        message: error.response.data.message ?? "",
      };
    });
  return res;
}
