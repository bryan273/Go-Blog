import userApiClient from "../../utils/userApiClient";

export function editUserProfile(data) {
  const res = userApiClient
    .put("/edit_profile", {
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
