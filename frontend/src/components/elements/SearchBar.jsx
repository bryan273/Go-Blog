import { FaSearch } from "react-icons/fa";

const SearchBar = () => {
  return (
    <form className=" w-full" action="/search">
      <div className="flex flex-col relative w-full">
        <input
          type="text"
          name="query"
          placeholder="cari post"
          className={
            "input h-[40px] rounded-[5px] pr-14 focus:outline-none border-2 border-[#DFE1E6] focus:border-blue-300 bg-[#FAFBFC] text-[16px] text-gray-600"
          }
        />
        <button className="absolute right-0 bg-cyan-500 p-3 py-2 rounded-r-[5px] hover:bg-cyan-600 h-full">
          <FaSearch className="text-white text-base h-6" />
        </button>
      </div>
    </form>
  );
};

export default SearchBar;
