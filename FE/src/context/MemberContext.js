import React, { createContext, useState, useContext } from "react";

const MemberContext = createContext(null);

export const MemberProvider = ({ children }) => {
  const [member, setMember] = useState(null);

  return (
    <MemberContext.Provider value={{ member, setMember }}>
      {children}
    </MemberContext.Provider>
  );
};

export const useMember = () => useContext(MemberContext);