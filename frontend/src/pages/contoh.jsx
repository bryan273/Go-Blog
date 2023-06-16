import { useState, useEffect } from "react";
import { GiAbstract038 } from "react-icons/gi";
import Image from "next/image";
import axios from "axios";

const Contoh = () => {
  const [counter, setCounter] = useState(0);
  const [query, setQuery] = useState({
    id: "",
    advice: "",
  });
  const getAdvice = async () => {
    setQuery({
      id: "",
      advice: "",
    });

    axios.get("https://api.adviceslip.com/advice", {}).then((response) => {
      setQuery(response.data.slip);
    });
  };

  useEffect(() => {
    getAdvice();
  }, []);

  const { id, advice } = query;

  return (
    <div
      className="w-screen min-h-screen flex flex-col justify-start items-center"
      data-theme="emerald"
    >
      <div className="w-1/3 p-10 rounded-lg shadow-lg mt-10 flex flex-col items-center gap-y-10 bg-emerald-400">
        <h3 className="text-2xl font-bold text-gray-800">COUNTER</h3>
        <p className="text-lg font-bold text-gray-700">{counter}</p>
        <div className="w-full flex justify-between items-center">
          <button
            className="btn btn-outline"
            onClick={() => {
              setCounter(counter - 1);
            }}
          >
            -
          </button>
          <button
            className="btn btn-outline"
            onClick={() => {
              setCounter(counter + 1);
            }}
          >
            +
          </button>
        </div>
      </div>

      <GiAbstract038 className="w-44 h-44 mt-10 animate-spin" />

      <div className="border border-blue-300 shadow rounded-md p-4 max-w-sm w-full mx-auto mt-20">
        <div className="animate-pulse flex space-x-4">
          <div className="rounded-full bg-slate-700 h-10 w-10"></div>
          <div className="flex-1 space-y-6 py-1">
            <div className="h-2 bg-slate-700 rounded"></div>
            <div className="space-y-3">
              <div className="grid grid-cols-3 gap-4">
                <div className="h-2 bg-slate-700 rounded col-span-2"></div>
                <div className="h-2 bg-slate-700 rounded col-span-1"></div>
              </div>
              <div className="h-2 bg-slate-700 rounded"></div>
            </div>
          </div>
        </div>
      </div>

      <article
        className="relative flex flex-col mx-6 my-4 justify-between items-center max-w-xl w-full sm:w-1/2 lg:w-1/3 h-auto min-h-72 
             bg-grayish-darkblue rounded-2xl p-8 text-center "
      >
        {id === "" && (
          <div className="mt-14 loader-circle-4 spinner_top">
            <div className="loader-circle-4 spinner_mid">
              <div className="loader-circle-4 spinner_bot"></div>
            </div>
          </div>
        )}

        {id !== "" && (
          <h1 className="text-xs wide-spacing neon-green">{"ADVICE #" + id}</h1>
        )}
        {id !== "" && (
          <h2 className="light-cyan text-xl sm:text-2xl lg:text-3xl block my-6">
            {'"' + advice + '"'}
          </h2>
        )}

        <button
          className="noSelect absolute -bottom-7 flex items-center justify-center bg-neon-green p-4 rounded-full green-shadow 
                transition-all duration-200 ease-in-out"
          onClick={() => getAdvice()}
        >
          <Image
            src="/assets/icon-dice.svg"
            width={25}
            height={25}
            alt="dice"
            fill={false}
          />
        </button>
      </article>
    </div>
  );
};

export default Contoh;
