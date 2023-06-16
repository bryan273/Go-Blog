import postApiClient from "../../utils/postApiClient";

export function getPostById(postId) {
  const res = postApiClient
    .get("/get_post/"+postId)
    .then((response) => {
      return response;
    })
    .catch((error) => {
      return {
        status: error.response.status,
        message: error.response.data.message ?? "",
      };
    });
  return res;
}
