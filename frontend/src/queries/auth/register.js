import authApiClient from "../../utils/authApiClient";

export function registerAccount(
  username,
  fullname,
  email,
  gender,
  birthdate,
  password
) {
  const res = authApiClient
    .post("/register", {
      username: username,
      fullname: fullname,
      email: email,
      gender: gender,
      birthdate: birthdate,
      password: password,
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
