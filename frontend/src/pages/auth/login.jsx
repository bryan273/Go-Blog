import LoginForm from "@components/modules/auth/LoginForm";
import { Toaster } from "react-hot-toast";

const Login = () => {
  return (
    <main className="max-w-screen min-h-screen w-screen bg-white flex items-center justify-center">
      <LoginForm />
    </main>
  );
};
export default Login;
