import axios from "axios";

const commentApiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_NOTIFICATION_API_URL, // Set your base URL here
  headers: {
    "Content-Type": "application/json", // Set your default request headers here
  },
});

commentApiClient.interceptors.request.use(
  (config) => {
    // Do something before sending the request, e.g. add an authorization header
    config.headers.set("X-JWT-TOKEN", localStorage.getItem("token"));
    return config;
  },
  (error) => {
    // Do something with the request error
    return Promise.reject(error);
  }
);

commentApiClient.interceptors.response.use(
  (response) => {
    // Do something with the response data
    return response;
  },
  (error) => {
    // Do something with the response error
    return Promise.reject(error);
  }
);

export default commentApiClient;
