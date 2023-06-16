import Navbar from "./Navbar";
import Drawer from "./Drawer";
import { useState } from "react";
const Layout = ({ children }) => {
  const [isNavbarRendered, setIsNavbarRendered] = useState(false);
  return (
    <Drawer>
      <Navbar onRender={() => setIsNavbarRendered(true)} />
      {isNavbarRendered && <main>{children}</main>}
    </Drawer>
  );
};

export default Layout;
