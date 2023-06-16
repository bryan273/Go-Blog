//@ts-check
import React from "react";

const ModalTemplate = ({ children, htmlFor }) => {
  return (
    <>
      {/* Put this part before </body> tag */}
      <input type="checkbox" id={htmlFor} className="modal-toggle" />
      <div className="modal modal-bottom sm:modal-middle">
        <div className="modal-box bg-white">
          {/* make sure you make modal close button using <label> with the SAME htmlFor value */}
          {children}
        </div>
      </div>
    </>
  );
};

export default ModalTemplate;
