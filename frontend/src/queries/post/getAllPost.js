import postApiClient from "../../utils/postApiClient";

export function getAllPost() {
  const res = postApiClient
    .get("/get_all_post")
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
