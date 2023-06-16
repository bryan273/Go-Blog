import { useContext, useEffect } from "react";
import { UserAuthContext } from "../contexts/UserAuthContext";

export const useUser = () => {
  const { user, setUser, isPreparing } = useContext(UserAuthContext);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user") ?? "null");
    if (user) addUser(user);
  }, []);

  const addUser = (user) => {
    setUser(user);
    localStorage.setItem("user", JSON.stringify(user));
    localStorage.setItem("token", user.token);
  };

  const removeUser = () => {
    delete localStorage.user;
    delete localStorage.token;
    setUser(null);
  };

  return { user, addUser, removeUser, isPreparing };
};
