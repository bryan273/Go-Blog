import postApiClient from "../../utils/postApiClient";

export function searchPost(queryTitle) {
  const res = postApiClient
    .post("/search", { queryTitle: queryTitle })
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
