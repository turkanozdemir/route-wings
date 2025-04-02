import React, { useState, useEffect } from "react";
import {
  Button,
  Paper,
  Typography,
  FormControl,
  createTheme,
  ThemeProvider,
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
  Select,
  MenuItem,
  InputLabel,
  FormHelperText,
} from "@mui/material";
import {
  Edit as EditIcon,
  Delete as DeleteIcon,
  Add as AddIcon,
} from "@mui/icons-material";
import TransportationService from "../services/TransportationService";
import LocationService from "../services/LocationService";

const TransportationPage = () => {
  const [tabValue, setTabValue] = useState(0);
  const [transportations, setTransportations] = useState([]);
  const [locations, setLocations] = useState([]);
  const [selectedTransportations, setSelectedTransportations] = useState([]);
  const [transportationData, setTransportationData] = useState({
    id: "",
    originName: "",
    destinationName: "",
    transportationType: "",
    operatingDays: [],
  });
  const [isEditMode, setIsEditMode] = useState(false);

  const transportationTypes = ["FLIGHT", "BUS", "SUBWAY", "UBER"];
  const daysOfWeek = [
    { value: 1, label: "Monday" },
    { value: 2, label: "Tuesday" },
    { value: 3, label: "Wednesday" },
    { value: 4, label: "Thursday" },
    { value: 5, label: "Friday" },
    { value: 6, label: "Saturday" },
    { value: 7, label: "Sunday" },
  ];

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
      fetchTransportations();
    }
    fetchLocations();
  }, [tabValue]);

  const fetchTransportations = async () => {
    try {
      const response = await TransportationService.listTransportations();
      setTransportations(response);
    } catch (error) {
      console.error("Error fetching transportations:", error);
      alert("Failed to load transportations!");
    }
  };

  const fetchLocations = async () => {
    try {
      const response = await LocationService.listLocations();
      setLocations(response);
    } catch (error) {
      console.error("Error fetching locations:", error);
    }
  };

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);

    if (newValue === 0) {
      setSelectedTransportations([]);
      resetForm();
    } else if (newValue === 2 || newValue === 3) {
      if (selectedTransportations.length === 1) {
        const selected = transportations.find(
          (t) => t.id === selectedTransportations[0]
        );
        if (selected) {
          setTransportationData({
            ...selected,
            operatingDays: selected.operatingDays || [],
          });
        }
      } else {
        resetForm();
      }
    }
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setTransportationData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleOperatingDaysChange = (event) => {
    const { value } = event.target;
    setTransportationData((prev) => ({ ...prev, operatingDays: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const payload = {
        originId: locations.find(
          (loc) => loc.name === transportationData.originName
        )?.id,
        destinationId: locations.find(
          (loc) => loc.name === transportationData.destinationName
        )?.id,
        transportationType: transportationData.transportationType,
        operatingDays: transportationData.operatingDays,
      };

      if (transportationData.id) {
        await TransportationService.updateTransportation(
          transportationData.id,
          payload
        );
        alert("Transportation updated successfully!");
      } else {
        await TransportationService.createTransportation(payload);
        alert("Transportation created successfully!");
      }
      fetchTransportations();
      setTabValue(1);
      resetForm();
    } catch (error) {
      console.error("Error while saving transportation:", error);
      alert("Operation failed!");
    }
  };

  const handleSelectTransportation = (id) => {
    setSelectedTransportations((prev) =>
      prev.includes(id) ? prev.filter((i) => i !== id) : [...prev, id]
    );
  };

  const handleDeleteSelected = async () => {
    if (selectedTransportations.length === 0) {
      alert("Please select at least one transportation to delete");
      return;
    }

    if (
      window.confirm(
        `Are you sure you want to delete ${selectedTransportations.length} transportation(s)?`
      )
    ) {
      try {
        await Promise.all(
          selectedTransportations.map((id) =>
            TransportationService.deleteTransportation(id)
          )
        );
        fetchTransportations();
        alert("Transportations deleted successfully!");
      } catch (error) {
        console.error("Error while deleting transportations:", error);
        alert("Delete failed!");
      }
    }
  };

  const handleAddNew = () => {
    resetForm();
    setIsEditMode(false);
    setTabValue(0);
  };

  const resetForm = () => {
    setTransportationData({
      id: "",
      originName: "",
      destinationName: "",
      transportationType: "",
      operatingDays: [],
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
            Transportation Management
          </Typography>

          <Box sx={{ borderBottom: 1, borderColor: "divider", mb: 3 }}>
            <Tabs value={tabValue} onChange={handleTabChange}>
              <Tab label="Create" />
              <Tab label="List" />
              <Tab label="Update" />
              <Tab label="Delete" />
            </Tabs>
          </Box>

          {tabValue === 0 && (
            <Box>
              <Typography variant="h5" gutterBottom>
                Create New Transportation
              </Typography>
              <form onSubmit={handleSubmit}>
                <FormControl fullWidth style={{ marginBottom: "16px" }}>
                  <InputLabel>Origin Location</InputLabel>
                  <Select
                    name="originName"
                    value={transportationData.originName || ""}
                    onChange={handleChange}
                    required
                  >
                    <MenuItem value="">Select Origin</MenuItem>
                    {locations.map((location) => (
                      <MenuItem key={location.id} value={location.name}>
                        {location.name} ({location.locationCode})
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>

                <FormControl fullWidth style={{ marginBottom: "16px" }}>
                  <InputLabel>Destination Location</InputLabel>
                  <Select
                    name="destinationName"
                    value={transportationData.destinationName || ""}
                    onChange={handleChange}
                    required
                  >
                    <MenuItem value="">Select Destination</MenuItem>
                    {locations.map((location) => (
                      <MenuItem key={location.id} value={location.name}>
                        {location.name} ({location.locationCode})
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>

                <FormControl fullWidth style={{ marginBottom: "16px" }}>
                  <InputLabel>Transportation Type</InputLabel>
                  <Select
                    name="transportationType"
                    value={transportationData.transportationType}
                    onChange={handleChange}
                    required
                  >
                    {transportationTypes.map((type) => (
                      <MenuItem key={type} value={type}>
                        {type}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>

                <FormControl fullWidth style={{ marginBottom: "16px" }}>
                  <InputLabel>Operating Days</InputLabel>
                  <Select
                    multiple
                    value={transportationData.operatingDays}
                    onChange={handleOperatingDaysChange}
                    renderValue={(selected) =>
                      selected
                        .map(
                          (day) =>
                            daysOfWeek.find((d) => d.value === day)?.label
                        )
                        .join(", ")
                    }
                    required
                  >
                    {daysOfWeek.map((day) => (
                      <MenuItem key={day.value} value={day.value}>
                        <Checkbox
                          checked={
                            transportationData.operatingDays.indexOf(
                              day.value
                            ) > -1
                          }
                        />
                        {day.label}
                      </MenuItem>
                    ))}
                  </Select>
                  <FormHelperText>Select operating days</FormHelperText>
                </FormControl>

                <Button
                  variant="contained"
                  color="primary"
                  type="submit"
                  fullWidth
                  startIcon={<AddIcon />}
                >
                  Create Transportation
                </Button>
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
                    disabled={selectedTransportations.length === 0}
                  >
                    Delete Selected
                  </Button>
                </Box>
                <Typography>
                  {selectedTransportations.length} item(s) selected
                </Typography>
              </Box>
              <TableContainer component={Paper}>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell padding="checkbox"></TableCell>
                      <TableCell>Origin</TableCell>
                      <TableCell>Destination</TableCell>
                      <TableCell>Type</TableCell>
                      <TableCell>Operating Days</TableCell>
                      <TableCell>Actions</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {transportations.map((transportation) => (
                      <TableRow key={transportation.id} hover>
                        <TableCell padding="checkbox">
                          <Checkbox
                            checked={selectedTransportations.includes(
                              transportation.id
                            )}
                            onChange={() =>
                              handleSelectTransportation(transportation.id)
                            }
                          />
                        </TableCell>
                        <TableCell>{transportation.originName}</TableCell>
                        <TableCell>{transportation.destinationName}</TableCell>
                        <TableCell>
                          {transportation.transportationType}
                        </TableCell>
                        <TableCell>
                          {transportation.operatingDays
                            ?.map(
                              (day) =>
                                daysOfWeek.find((d) => d.value === day)?.label
                            )
                            .join(", ")}
                        </TableCell>
                        <TableCell>
                          <IconButton
                            onClick={() => {
                              setSelectedTransportations([transportation.id]);
                              setTransportationData({
                                id: transportation.id,
                                originName: transportation.originName,
                                destinationName: transportation.destinationName,
                                transportationType:
                                  transportation.transportationType,
                                operatingDays:
                                  transportation.operatingDays || [],
                              });
                              setTabValue(2);
                            }}
                          >
                            <EditIcon color="primary" />
                          </IconButton>
                          <IconButton
                            onClick={() => {
                              setSelectedTransportations([transportation.id]);
                              handleDeleteSelected();
                            }}
                            style={{ marginLeft: 8 }}
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
                Update Transportation
              </Typography>
              {selectedTransportations.length === 1 ? (
                <form onSubmit={handleSubmit}>
                  <input
                    type="hidden"
                    name="id"
                    value={transportationData.id}
                  />
                  <FormControl fullWidth style={{ marginBottom: "16px" }}>
                    <InputLabel>Origin Location</InputLabel>
                    <Select
                      name="originName"
                      value={transportationData.originName || ""}
                      onChange={handleChange}
                      required
                    >
                      {locations.map((location) => (
                        <MenuItem key={location.id} value={location.name}>
                          {location.name} ({location.locationCode})
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>

                  <FormControl fullWidth style={{ marginBottom: "16px" }}>
                    <InputLabel>Destination Location</InputLabel>
                    <Select
                      name="destinationName"
                      value={transportationData.destinationName || ""}
                      onChange={handleChange}
                      required
                    >
                      {locations.map((location) => (
                        <MenuItem key={location.id} value={location.name}>
                          {location.name} ({location.locationCode})
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>

                  <FormControl fullWidth style={{ marginBottom: "16px" }}>
                    <InputLabel>Transportation Type</InputLabel>
                    <Select
                      name="transportationType"
                      value={transportationData.transportationType}
                      onChange={handleChange}
                      required
                    >
                      {transportationTypes.map((type) => (
                        <MenuItem key={type} value={type}>
                          {type}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>

                  <FormControl fullWidth style={{ marginBottom: "16px" }}>
                    <InputLabel>Operating Days</InputLabel>
                    <Select
                      multiple
                      value={transportationData.operatingDays}
                      onChange={handleOperatingDaysChange}
                      renderValue={(selected) =>
                        selected
                          .map(
                            (day) =>
                              daysOfWeek.find((d) => d.value === day)?.label
                          )
                          .join(", ")
                      }
                      required
                    >
                      {daysOfWeek.map((day) => (
                        <MenuItem key={day.value} value={day.value}>
                          <Checkbox
                            checked={
                              transportationData.operatingDays.indexOf(
                                day.value
                              ) > -1
                            }
                          />
                          {day.label}
                        </MenuItem>
                      ))}
                    </Select>
                    <FormHelperText>Select operating days</FormHelperText>
                  </FormControl>

                  <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                    fullWidth
                    startIcon={<EditIcon />}
                  >
                    Update Transportation
                  </Button>
                </form>
              ) : (
                <Box textAlign="center" py={4}>
                  <Typography variant="h6" gutterBottom>
                    Please select exactly one transportation from the List tab
                    to update
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
                Delete Transportation
              </Typography>
              {selectedTransportations.length > 0 ? (
                <Box>
                  <Typography variant="body1" gutterBottom>
                    You are about to delete {selectedTransportations.length}{" "}
                    transportation(s). Are you sure?
                  </Typography>
                  <TableContainer
                    component={Paper}
                    style={{ marginBottom: "20px" }}
                  >
                    <Table>
                      <TableHead>
                        <TableRow>
                          <TableCell>Origin</TableCell>
                          <TableCell>Destination</TableCell>
                          <TableCell>Type</TableCell>
                          <TableCell>Operating Days</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {transportations
                          .filter((t) => selectedTransportations.includes(t.id))
                          .map((transportation) => (
                            <TableRow key={transportation.id}>
                              <TableCell>{transportation.originName}</TableCell>
                              <TableCell>
                                {transportation.destinationName}
                              </TableCell>
                              <TableCell>
                                {transportation.transportationType}
                              </TableCell>
                              <TableCell>
                                {transportation.operatingDays
                                  ?.map(
                                    (day) =>
                                      daysOfWeek.find((d) => d.value === day)
                                        ?.label
                                  )
                                  .join(", ")}
                              </TableCell>
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
                    Please select one or more transportations from the List tab
                    to delete
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

export default TransportationPage;
