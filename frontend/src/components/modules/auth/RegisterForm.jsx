import { useContext, useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { errorToast, successToast } from "../../../utils/toast";
import { IoArrowBack } from "react-icons/io5";
import { useRouter } from "next/router";
import { registerAccount } from "../../../queries/auth/register";

const RegisterForm = () => {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);

  const {
    register,
    handleSubmit,
    trigger,
    formState: { isDirty, isValid, errors },
  } = useForm({
    mode: "onChange",
    reValidateMode: "onBlur", //validate whenever user click outside of input field
    defaultValues: {
      username: "",
      fullname: "",
      email: "",
      gender: "",
      password: "",
      birthdate: "",
    },
  });

  const onSubmit = async (data) => {
    setIsLoading(true);
    const { username, fullname, birthdate, gender, email, password } = data;

    const [year, month, date] = birthdate.split("-");
    let cleanString = date + "/" + month + "/" + year;
    const res = await registerAccount(
      username,
      fullname,
      email,
      gender,
      birthdate,
      password
    );

    setTimeout(() => {
      setIsLoading(false);
      if (res.status >= 400) {
        errorToast(res.message);
      } else if (res.status == 200) {
        successToast("your account has been created");
        router.push("/auth/login");
      } else {
        errorToast("unknown error");
      }
    }, 1000);
  };

  return (
    <section
      className={`flex flex-col gap-y-10 items-center bg-[#F7FAFC] lg:bg-white `}
    >
      {/*actual body*/}
      <div className="relative flex flex-col mb-20 lg:mb-0 gap-y-4 lg:gap-y-3 w-11/12 max-w-md lg:w-full lg:max-w-full">
        {/*page header and back button for smaller device*/}
        <div className="lg:hidden w-screen flex flex-row items-center gap-x-3 mt-20">
          <IoArrowBack
            className="ml-2 mb-1 w-[20px] h-[20px]"
            onClick={() => {}}
          />
          <h1 className=" font-[700] text-md text-gray-700 ">Register</h1>
        </div>

        <h1 className="hidden lg:block font-[700] text-[24px] text-gray-700 ">
          Register
        </h1>

        {/* <UpdateProfilePicture /> */}

        <form
          onSubmit={handleSubmit(onSubmit)}
          className="flex flex-col gap-y-3 mx-2 lg:mx-0"
        >
          {/*username form input*/}
          <div className=" flex flex-col lg:flex-row w-full gap-y-4 lg:gap-x-4">
            <div className="flex flex-col lg:w-1/2">
              <label
                htmlFor="username"
                className="font-semibold text-[16px] text-gray-600"
              >
                Username <span className="text-red-600">&#42;</span>
              </label>
              <input
                type="text"
                className={
                  "input h-[40px] rounded-[3px] focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600" +
                  (errors.username && " border-red-600")
                }
                {...register("username", {
                  required: "Data ini wajib diisi",
                  pattern: {
                    value: /^[A-Za-z0-9_]+$/i,
                    message: "Hanya boleh mengandung angka, huruf dan _",
                  },
                })}
                onKeyUp={() => {
                  trigger("username");
                }}
              ></input>

              {errors.username && (
                <span className="text-[13px] text-red-600 mt-1">
                  {"*" + errors.username?.message}
                </span>
              )}
            </div>

            {/*fullname form input*/}
            <div className="flex flex-col lg:w-1/2">
              <label
                htmlFor="fullname"
                className="font-semibold text-[16px] text-gray-600"
              >
                FullName <span className="text-red-600">&#42;</span>
              </label>
              <input
                type="text"
                className={
                  "input h-[40px] rounded-[3px] focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600" +
                  (errors.fullname && " border-red-600")
                }
                {...register("fullname", {
                  required: "Data ini wajib diisi",
                  pattern: {
                    value: /^[A-Za-z ]+$/i,
                    message: "Nama hanya boleh mengandung huruf",
                  },
                })}
                onKeyUp={() => {
                  trigger("fullname");
                }}
              ></input>

              {errors.fullname && (
                <span className="text-[13px] text-red-600 mt-1">
                  {"*" + errors.fullname?.message}
                </span>
              )}
            </div>
          </div>

          <div className=" flex flex-col lg:flex-row w-full gap-y-4 lg:gap-x-4">
            <div className="flex flex-col lg:w-1/2">
              <label
                htmlFor="email"
                className="font-semibold text-[16px] text-gray-600"
              >
                Tanggal Lahir <span className="text-red-600">&#42;</span>
              </label>
              <input
                type="date"
                className={
                  "input w-full h-[40px] rounded-[3px] focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600" +
                  (errors.birthdate && " border-red-600")
                }
                {...register("birthdate", {
                  required: "Data ini wajib diisi",
                })}
                onKeyUp={() => {
                  trigger("birthdate");
                }}
              ></input>

              {errors.birthdate && (
                <span className="text-[13px] text-red-600 mt-1">
                  {"*" + errors.birthdate?.message}
                </span>
              )}
            </div>

            {/*fullname form input*/}
            <div className="flex flex-col lg:w-1/2">
              <label
                htmlFor="gender"
                className="font-semibold text-[16px] text-gray-600"
              >
                Jenis Kelamin
              </label>
              <select
                className={
                  "input h-[40px] rounded-[3px] focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600" +
                  (errors.gender && " border-red-600")
                }
                {...register("gender", {
                  required: "Data ini wajib diisi",
                })}
              >
                <option value="MALE">Pria</option>
                <option value="FEMALE">Wanita</option>
              </select>

              {errors.gender && (
                <span className="text-[13px] text-red-600 mt-1">
                  {"*" + errors.gender?.message}
                </span>
              )}
            </div>
          </div>

          {/*email form input*/}
          <div className="flex flex-col">
            <label
              htmlFor="email"
              className="font-semibold text-[16px] text-gray-600"
            >
              Email<span className="text-red-600">&#42;</span>
            </label>
            <input
              type="text"
              className={
                "input h-[40px] rounded-[3px] focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600" +
                (errors.email && " border-red-600")
              }
              {...register("email", {
                required: "Data ini wajib diisi",
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: "email tidak valid",
                },
              })}
              onKeyUp={() => {
                trigger("email");
              }}
            ></input>

            {errors.email && (
              <span className="text-[13px] text-red-600 mt-1">
                {"*" + errors.email?.message}
              </span>
            )}
          </div>
          <div className="flex flex-col">
            <label
              htmlFor="password"
              className="font-semibold text-[16px] text-gray-600"
            >
              Password<span className="text-red-600">&#42;</span>
            </label>
            <input
              type="password"
              className={
                "input h-[40px] rounded-[3px] focus:outline-none border border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600" +
                (errors.password && " border-red-600")
              }
              {...register("password", {
                required: "Data ini wajib diisi",
                pattern: {
                  value: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/,
                  message: "password harus mengandung huruf dan angka!",
                },
                maxLength: {
                  value: 20,
                  message: "panjang password harus diantara 8 - 20 karakter!",
                },
                minLength: {
                  value: 8,
                  message: "panjang password harus diantara 8 - 20 karakter!",
                },
              })}
              onKeyUp={() => {
                trigger("password");
              }}
            ></input>

            {errors.password && (
              <span className="text-[13px] text-red-600 mt-1">
                {"*" + errors.password?.message}
              </span>
            )}
          </div>

          {/*submit button; disabled when form is not filled (!isDirty) or input is invalid (!isvalid)*/}
          <div className="mt-2 w-full flex flex-row justify-center">
            <button
              disabled={!isDirty || !isValid || isLoading}
              type="submit"
              className={
                "w-full lg:w-auto cursor-pointer text-white text-base p-6 py-3 rounded-md transition-all duration-200 shadow-lg" +
                (!isDirty || !isValid || isLoading
                  ? " bg-gray-400"
                  : " bg-[#30b465] hover:bg-[#27884e]") +
                (isLoading ? " cursor-wait" : "")
              }
            >
              register
            </button>
          </div>
        </form>
      </div>
    </section>
  );
};

export default RegisterForm;
