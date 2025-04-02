import React from "react";
import { Box } from "@mui/material";

const HomePage = () => {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "flex-end",
        minHeight: "calc(100vh - 64px - 32px)",
        p: 3,
      }}
    ></Box>
  );
};

export default HomePage;
