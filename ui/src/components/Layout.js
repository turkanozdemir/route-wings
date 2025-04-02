import React from "react";
import { Outlet, Link } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Typography,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Box,
} from "@mui/material";

const Layout = () => {
  return (
    <Box sx={{ display: "flex" }}>
      {/* Header */}
      <AppBar
        position="fixed"
        elevation={0}
        color="transparent"
        sx={{
          zIndex: (theme) => theme.zIndex.drawer + 1,
          backgroundColor: "rgba(21, 101, 192, 0.92)",
          backdropFilter: "blur(8px)",
        }}
      >
        <Toolbar>
          <Typography
            variant="h6"
            noWrap
            component="div"
            sx={{
              color: "white",
              fontWeight: 600,
              textShadow: "1px 1px 2px rgba(0,0,0,0.2)",
            }}
          >
            Route Wings
          </Typography>
        </Toolbar>
      </AppBar>

      {/* Sidebar */}
      <Drawer
        variant="permanent"
        sx={{
          width: 240,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: 240,
            boxSizing: "border-box",
            backgroundColor: "rgba(245, 245, 245, 0.98)",
            borderRight: "none",
            boxShadow: "2px 0 10px rgba(0,0,0,0.1)",
          },
        }}
      >
        <Toolbar />
        <Box sx={{ overflow: "auto" }}>
          <List>
            {["Locations", "Transportations", "Routes"].map((text) => (
              <ListItem key={text} disablePadding>
                <ListItemButton
                  component={Link}
                  to={`/${text.toLowerCase()}`}
                  sx={{
                    "&:hover": {
                      backgroundColor: "rgba(21, 101, 192, 0.08)",
                    },
                  }}
                >
                  <ListItemText
                    primary={text}
                    primaryTypographyProps={{
                      color: "rgba(0, 0, 0, 0.8)",
                      fontWeight: 500,
                    }}
                  />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>

      {/* Main Content */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          pt: 8,
          pl: 3,
          pr: 3,
          pb: 3,
          backgroundImage: "url('/background.png')",
          backgroundSize: "cover",
          backgroundRepeat: "no-repeat",
          backgroundPosition: "center",
          minHeight: "100vh",
          position: "relative",
          "&::before": {
            content: '""',
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: "rgba(255, 255, 255, 0.75)",
          },
        }}
      >
        <Box position="relative" sx={{ zIndex: 1 }}>
          <Outlet />
        </Box>
      </Box>
    </Box>
  );
};

export default Layout;
