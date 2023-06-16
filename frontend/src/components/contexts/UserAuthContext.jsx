import { createContext, useEffect, useMemo, useState } from "react";

export const UserAuthContext = createContext({
  user: null,
  setUser: () => {},
});

const UserAuthContextProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user") ?? "null");
    if (user) setUser(user);
  }, []);

  return (
    <UserAuthContext.Provider value={{ user, setUser }}>
      {children}
    </UserAuthContext.Provider>
  );
};

export default UserAuthContextProvider;
