import "./App.css";

import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LocationPage from "./components/LocationPage";
import TransportationPage from "./components/TransportationPage";
import RoutePage from "./components/RoutePage";
import Layout from "./components/Layout";
import HomePage from "./components/HomePage";

function App() {
  return (
    <Router>
      <div>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/" element={<HomePage />} />
            <Route path="/locations" element={<LocationPage />} />
            <Route path="/transportations" element={<TransportationPage />} />
            <Route path="/routes" element={<RoutePage />} />
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
