import commentApiClient from "../../utils/commentApiClient";

export function createComment(data) {
  const res = commentApiClient
    .post("/create", {
      ...data,
    })
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
