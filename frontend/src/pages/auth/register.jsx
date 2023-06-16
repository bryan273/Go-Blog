import { Toaster } from "react-hot-toast";
import RegisterForm from "@components/modules/auth/RegisterForm";

const Register = () => {
  return (
    <main className="max-w-screen min-h-screen w-screen bg-white flex items-center justify-center">
      <RegisterForm />
    </main>
  );
};

export default Register;
