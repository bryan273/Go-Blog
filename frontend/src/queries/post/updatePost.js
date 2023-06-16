import postApiClient from "../../utils/postApiClient";

export async function updatePost(data) {
  const res = postApiClient
    .put(`/update_post`, {
      ...data,
    })
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
