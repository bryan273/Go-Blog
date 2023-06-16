import { FiChevronLeft } from "react-icons/fi";
import { useRouter } from "next/router";

export default function BackButton() {
  const router = useRouter();

  return (
    <div
      onClick={() => router.back()}
      className="flex justify-start items-center gap-x-2 cursor-pointer"
    >
      <FiChevronLeft className="text-cyan-500 w-6 h-6" />
      <p className="text-[16px] mb-[0.2rem] font-semibold text-gray-400 hover:text-cyan-500 hover:underline transition-all duration-100">
        Kembali
      </p>
    </div>
  );
}
