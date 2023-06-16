import userApiClient from "../../utils/userApiClient";

export function getUserProfile() {
  const res = userApiClient
    .get("/get_profile")
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
