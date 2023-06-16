import Link from "next/link";

const LinkedButton = ({ link, title, className }) => {
  return (
    <Link href={link}>
      <button
        className={
          " flex items-center justify-center bg-cyan-500 text-white rounded px-4 py-2 hover:bg-cyan-600 transition-colors text-[1.1rem] " +
          className
        }
      >
        {title}
      </button>
    </Link>
  );
};

export default LinkedButton;
