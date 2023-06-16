import notifApiClient from "../../utils/notifApiClient";

export function getNewNotification(userId) {
  const res = notifApiClient
    .get(`/get_new_notification/${userId}`)
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

export function getOldNotification(userId) {
  const res = notifApiClient
    .get(`/get_old_notification/${userId}`)
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
