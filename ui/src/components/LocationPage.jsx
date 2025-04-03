import React, { useState, useEffect } from "react";
import {
  Button,
  Paper,
  Typography,
  FormControl,
  createTheme,
  ThemeProvider,
  TextField,
  Tabs,
  Tab,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Checkbox,
} from "@mui/material";
import {
  Edit as EditIcon,
  Delete as DeleteIcon,
  Add as AddIcon,
} from "@mui/icons-material";
import LocationService from "../services/LocationService";

const LocationPage = () => {
  const [tabValue, setTabValue] = useState(0);
  const [locations, setLocations] = useState([]);
  const [selectedLocations, setSelectedLocations] = useState([]);
  const [locationData, setLocationData] = useState({
    id: "",
    name: "",
    city: "",
    country: "",
    locationCode: "",
  });
  const [openDialog, setOpenDialog] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);

  const theme = createTheme({
    palette: {
      primary: {
        main: "#1976d2",
      },
      secondary: {
        main: "#f50057",
      },
    },
  });

  useEffect(() => {
    if (tabValue === 1) {
      fetchLocations();
    }
  }, [tabValue]);

  const fetchLocations = async () => {
    try {
      const response = await LocationService.listLocations();
      setLocations(response);
      setSelectedLocations([]);
    } catch (error) {
      console.error("Error fetching locations:", error);
      alert("Failed to load locations!");
    }
  };

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);

    if (newValue === 0) {
      resetForm();
    }

    if (newValue === 2 || newValue === 3) {
      if (selectedLocations.length === 1) {
        const selectedLocation = locations.find(
          (loc) => loc.id === selectedLocations[0]
        );
        if (selectedLocation) {
          setLocationData(selectedLocation);
        }
      } else {
        setLocationData({
          id: "",
          name: "",
          city: "",
          country: "",
          locationCode: "",
        });
      }
    }
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setLocationData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      if (locationData.id) {
        await LocationService.updateLocation(locationData.id, locationData);
        alert("Location updated successfully!");
      } else {
        await LocationService.createLocation(locationData);
        alert("Location created successfully!");
      }
      setOpenDialog(false);
      setTabValue(1);
      fetchLocations();
      resetForm();
    } catch (error) {
      console.error("Error while saving location:", error);
      alert("Operation failed!");
    }
  };

  const handleSelectLocation = (locationId) => {
    setSelectedLocations((prev) => {
      if (prev.includes(locationId)) {
        return prev.filter((id) => id !== locationId);
      } else {
        return [...prev, locationId];
      }
    });
  };

  const handleDeleteSelected = async () => {
    if (selectedLocations.length === 0) {
      alert("Please select at least one location to delete");
      return;
    }

    if (
      window.confirm(
        `Are you sure you want to delete ${selectedLocations.length} location(s)?`
      )
    ) {
      try {
        await Promise.all(
          selectedLocations.map((id) => LocationService.deleteLocation(id))
        );
        fetchLocations();
        alert("Locations deleted successfully!");
        setSelectedLocations([]);
      } catch (error) {
        console.error("Error while deleting locations:", error);
        alert("Delete failed!");
      }
    }
  };

  const handleAddNew = () => {
    resetForm();
    setIsEditMode(false);
    setOpenDialog(true);
  };

  const resetForm = () => {
    setLocationData({
      id: "",
      name: "",
      city: "",
      country: "",
      locationCode: "",
    });
  };

  return (
    <ThemeProvider theme={theme}>
      <div
        style={{
          minHeight: "100vh",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          backgroundImage: `url(background.jpg)`,
          backgroundSize: "cover",
          padding: "20px",
        }}
      >
        <Paper
          style={{
            padding: "20px",
            width: "100%",
            maxWidth: "1200px",
            backgroundColor: "rgba(255, 255, 255, 0.9)",
            marginTop: "20px",
          }}
        >
          <Typography variant="h4" align="center" gutterBottom>
            Location Management
          </Typography>

          {/* Side Bar */}
          <Box sx={{ borderBottom: 1, borderColor: "divider", mb: 3 }}>
            <Tabs value={tabValue} onChange={handleTabChange}>
              <Tab label="Create" />
              <Tab label="List" />
              <Tab label="Update" />
              <Tab label="Delete" />
            </Tabs>
          </Box>

          {/* Tab Content */}
          {tabValue === 0 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Create New Location
              </Typography>
              <form onSubmit={handleSubmit}>
                <FormControl fullWidth style={{ marginBottom: "10px" }}>
                  {Object.entries(locationData)
                    .filter(([key]) => key !== "id")
                    .map(([key, value]) => (
                      <TextField
                        key={key}
                        label={key
                          .replace(/([A-Z])/g, " $1")
                          .replace(/^./, (str) => str.toUpperCase())}
                        name={key}
                        value={value}
                        onChange={handleChange}
                        fullWidth
                        variant="outlined"
                        margin="normal"
                        required
                      />
                    ))}
                  <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                    style={{ marginTop: "10px" }}
                    fullWidth
                    startIcon={<AddIcon />}
                  >
                    Create Location
                  </Button>
                </FormControl>
              </form>
            </Box>
          )}

          {tabValue === 1 && (
            <Box>
              <Box display="flex" justifyContent="space-between" mb={2}>
                <Box>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={handleAddNew}
                    startIcon={<AddIcon />}
                    style={{ marginRight: "10px" }}
                  >
                    Add New
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    onClick={handleDeleteSelected}
                    startIcon={<DeleteIcon />}
                    disabled={selectedLocations.length === 0}
                  >
                    Delete Selected
                  </Button>
                </Box>
                <Typography>
                  {selectedLocations.length} item(s) selected
                </Typography>
              </Box>
              <TableContainer component={Paper}>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell padding="checkbox"></TableCell>
                      <TableCell>Name</TableCell>
                      <TableCell>City</TableCell>
                      <TableCell>Country</TableCell>
                      <TableCell>Location Code</TableCell>
                      <TableCell>Actions</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {locations.map((location) => (
                      <TableRow key={location.id} hover>
                        <TableCell padding="checkbox">
                          <Checkbox
                            checked={selectedLocations.includes(location.id)}
                            onChange={() => handleSelectLocation(location.id)}
                          />
                        </TableCell>
                        <TableCell>{location.name}</TableCell>
                        <TableCell>{location.city}</TableCell>
                        <TableCell>{location.country}</TableCell>
                        <TableCell>{location.locationCode}</TableCell>
                        <TableCell>
                          <IconButton
                            onClick={() => {
                              setSelectedLocations([location.id]);
                              setLocationData(location);
                              setTabValue(2);
                            }}
                          >
                            <EditIcon color="primary" />
                          </IconButton>
                          <IconButton
                            onClick={() => {
                              setSelectedLocations([location.id]);
                              handleDeleteSelected();
                            }}
                          >
                            <DeleteIcon color="error" />
                          </IconButton>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            </Box>
          )}

          {tabValue === 2 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Update Location
              </Typography>
              {selectedLocations.length === 1 ? (
                <form onSubmit={handleSubmit}>
                  <input type="hidden" name="id" value={locationData.id} />
                  <FormControl fullWidth style={{ marginBottom: "10px" }}>
                    {Object.entries(locationData)
                      .filter(([key]) => key !== "id")
                      .map(([key, value]) => (
                        <TextField
                          key={key}
                          label={key
                            .replace(/([A-Z])/g, " $1")
                            .replace(/^./, (str) => str.toUpperCase())}
                          name={key}
                          value={value}
                          onChange={handleChange}
                          fullWidth
                          variant="outlined"
                          margin="normal"
                          required
                        />
                      ))}
                    <Button
                      variant="contained"
                      color="primary"
                      type="submit"
                      style={{ marginTop: "10px" }}
                      fullWidth
                      startIcon={<EditIcon />}
                    >
                      Update Location
                    </Button>
                  </FormControl>
                </form>
              ) : (
                <Box textAlign="center" py={4}>
                  <Typography variant="h6" gutterBottom>
                    Please select exactly one location from the List tab to
                    update
                  </Typography>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={() => setTabValue(1)}
                  >
                    Go to List
                  </Button>
                </Box>
              )}
            </Box>
          )}

          {tabValue === 3 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Delete Location
              </Typography>
              {selectedLocations.length > 0 ? (
                <Box>
                  <Typography variant="body1" gutterBottom>
                    You are about to delete {selectedLocations.length}{" "}
                    location(s). Are you sure?
                  </Typography>
                  <TableContainer
                    component={Paper}
                    style={{ marginBottom: "20px" }}
                  >
                    <Table>
                      <TableHead>
                        <TableRow>
                          <TableCell>Name</TableCell>
                          <TableCell>City</TableCell>
                          <TableCell>Country</TableCell>
                          <TableCell>Location Code</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {locations
                          .filter((loc) => selectedLocations.includes(loc.id))
                          .map((location) => (
                            <TableRow key={location.id}>
                              <TableCell>{location.name}</TableCell>
                              <TableCell>{location.city}</TableCell>
                              <TableCell>{location.country}</TableCell>
                              <TableCell>{location.locationCode}</TableCell>
                            </TableRow>
                          ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                  <Button
                    variant="contained"
                    color="error"
                    onClick={handleDeleteSelected}
                    startIcon={<DeleteIcon />}
                    fullWidth
                  >
                    Confirm Delete
                  </Button>
                </Box>
              ) : (
                <Box textAlign="center" py={4}>
                  <Typography variant="h6" gutterBottom>
                    Please select one or more locations from the List tab to
                    delete
                  </Typography>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={() => setTabValue(1)}
                  >
                    Go to List
                  </Button>
                </Box>
              )}
            </Box>
          )}
        </Paper>
      </div>
    </ThemeProvider>
  );
};

export default LocationPage;
