const Drawer = ({ children }) => {
  return (
    <div className="drawer">
      <input id="my-drawer-3" type="checkbox" className="drawer-toggle" />
      <div className="drawer-content flex flex-col">{children}</div>
      <div className="drawer-side">
        <label htmlFor="my-drawer-3" className="drawer-overlay"></label>
        <ul className="menu p-4 w-80 bg-gray-100 text-gray-800">
          <li>
            <a href="/">Home</a>
          </li>

          <li>
            <a href="/about">About Us</a>
          </li>
          <li>
            <a href="/products"> Products</a>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default Drawer;
