import React, { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Paper,
  List,
  ListItem,
  ListItemText,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  CircularProgress,
  TextField,
} from "@mui/material";
import RouteService from "../services/RouteService";
import LocationService from "../services/LocationService";

const RoutePage = () => {
  const [origin, setOrigin] = useState("");
  const [destination, setDestination] = useState("");
  const [date, setDate] = useState(new Date().toISOString().split("T")[0]);
  const [routes, setRoutes] = useState([]);
  const [selectedRoute, setSelectedRoute] = useState(null);
  const [error, setError] = useState("");
  const [locations, setLocations] = useState([]);
  const [loadingLocations, setLoadingLocations] = useState(true);

  useEffect(() => {
    const fetchLocations = async () => {
      try {
        const locationData = await LocationService.listLocations();
        setLocations(locationData);
      } catch (err) {
        setError("Failed to load locations");
        console.error("Error fetching locations:", err);
      } finally {
        setLoadingLocations(false);
      }
    };

    fetchLocations();
  }, []);

  const handleSearch = async () => {
    if (!origin || !destination) {
      setError("Please select both origin and destination");
      return;
    }

    try {
      setError("");
      const fetchedRoutes = await RouteService.getRoutes(
        origin,
        destination,
        date
      );

      if (!fetchedRoutes || fetchedRoutes.length === 0) {
        setRoutes([]);
        setSelectedRoute(null);
        setError(`No routes found from ${origin} to ${destination} on ${date}`);
        return;
      }

      const updatedRoutes = fetchedRoutes.map((route) => {
        const steps = route.steps || [];
        return {
          ...route,
          steps,
          origin: steps.length ? steps[0].originName : "Unknown",
          destination: steps.length
            ? steps[steps.length - 1].destinationName
            : "Unknown",
        };
      });

      setRoutes(updatedRoutes);
      setSelectedRoute(null);
    } catch (err) {
      setError(err.message);
      setRoutes([]);
      setSelectedRoute(null);
    }
  };

  const getLocationDisplayName = (locationCode) => {
    const location = locations.find((loc) => loc.locationCode === locationCode);
    return location
      ? `${location.name} (${location.locationCode})`
      : "Unknown Location";
  };

  if (loadingLocations) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Route Planner
      </Typography>

      <Paper elevation={3} sx={{ padding: 3, marginBottom: 3 }}>
        <Box
          sx={{ display: "flex", gap: 2, flexWrap: "wrap", marginBottom: 2 }}
        >
          <FormControl sx={{ minWidth: 200 }}>
            <InputLabel id="origin-label">Origin</InputLabel>
            <Select
              labelId="origin-label"
              id="origin"
              value={origin}
              label="Origin"
              onChange={(e) => setOrigin(e.target.value)}
              disabled={locations.length === 0}
            >
              {locations.map((loc) => (
                <MenuItem key={loc.locationCode} value={loc.locationCode}>
                  {`${loc.name} (${loc.locationCode})`}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <FormControl sx={{ minWidth: 200 }}>
            <InputLabel id="destination-label">Destination</InputLabel>
            <Select
              labelId="destination-label"
              id="destination"
              value={destination}
              label="Destination"
              onChange={(e) => setDestination(e.target.value)}
              disabled={locations.length === 0}
            >
              {locations.map((loc) => (
                <MenuItem key={loc.locationCode} value={loc.locationCode}>
                  {`${loc.name} (${loc.locationCode})`}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <TextField
            label="Travel Date"
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
            sx={{ minWidth: 200 }}
          />
        </Box>

        <Button
          variant="contained"
          onClick={handleSearch}
          sx={{ marginTop: 1 }}
          disabled={!origin || !destination || locations.length === 0}
        >
          Search Routes
        </Button>
      </Paper>

      {locations.length === 0 && !error && (
        <Typography color="textSecondary" sx={{ marginBottom: 2 }}>
          No locations available. Please check back later.
        </Typography>
      )}

      <Box sx={{ display: "flex", gap: 3 }}>
        <Paper elevation={3} sx={{ padding: 3, flex: 1 }}>
          <Typography variant="h6" gutterBottom>
            Available Routes
          </Typography>

          {routes.length > 0 ? (
            <List>
              {routes.map((route, index) => {
                const viaSteps = route.steps.slice(1, -1);
                const viaText =
                  viaSteps.length > 0
                    ? ` via ${viaSteps
                        .map((s) => s.originName.trim())
                        .join(", ")}`
                    : "";

                return (
                  <ListItem
                    key={index}
                    button
                    onClick={() => setSelectedRoute(route)}
                    selected={selectedRoute?.steps === route.steps}
                  >
                    <ListItemText
                      primary={`Route ${index + 1}${viaText}`}
                      secondary={`From: ${route.origin}, To: ${route.destination}`}
                    />
                  </ListItem>
                );
              })}
            </List>
          ) : (
            <Typography sx={{ color: "text.primary" }}>
              {error ? (
                <>
                  No routes found from{" "}
                  <strong>{getLocationDisplayName(origin)}</strong> to{" "}
                  <strong>{getLocationDisplayName(destination)}</strong> on{" "}
                  <strong>{date}</strong>
                </>
              ) : (
                "No routes available. Please search for routes."
              )}
            </Typography>
          )}
        </Paper>

        {selectedRoute && (
          <Paper elevation={3} sx={{ padding: 3, flex: 1 }}>
            <Box
              sx={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
              }}
            >
              <Typography variant="h6" gutterBottom>
                Route Details
              </Typography>
              <Button onClick={() => setSelectedRoute(null)}>Close</Button>
            </Box>

            <TableContainer component={Paper} sx={{ marginTop: 2 }}>
              <Table>
                <TableBody>
                  <TableRow>
                    <TableCell sx={{ fontWeight: "bold" }}>Origin</TableCell>
                    <TableCell>{selectedRoute.origin}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell sx={{ fontWeight: "bold" }}>
                      Destination
                    </TableCell>
                    <TableCell>{selectedRoute.destination}</TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell sx={{ fontWeight: "bold" }}>Date</TableCell>
                    <TableCell>{new Date(date).toLocaleDateString()}</TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>

            <Typography
              variant="subtitle1"
              sx={{ marginTop: 3, marginBottom: 1 }}
            >
              Transportation Steps:
            </Typography>
            <List>
              {selectedRoute.steps.map((step, index) => (
                <ListItem key={index}>
                  <ListItemText
                    primary={`${step.originName} → ${step.destinationName}`}
                    secondary={`Transport: ${step.transportationType}`}
                  />
                </ListItem>
              ))}
            </List>
          </Paper>
        )}
      </Box>
    </Box>
  );
};

export default RoutePage;
