import "../styles/globals.css";
import UserAuthContextProvider from "@components/contexts/UserAuthContext";
import Layout from "@components/layout/Layout";
import { Toaster } from "react-hot-toast";

function GoBlog({ Component, pageProps }) {
  return (
    <UserAuthContextProvider>
      <Layout>
        <Component {...pageProps} />
        <Toaster position="bottom-center" />
      </Layout>
    </UserAuthContextProvider>
  );
}

export default GoBlog;
