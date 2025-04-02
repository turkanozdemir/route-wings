import axios from "axios";

const BASE_URL = "http://localhost:8000/locations";

class LocationService {
  static async createLocation(locationData) {
    try {
      const response = await axios.post(`${BASE_URL}`, locationData, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error while creating Location:", error);
      throw error;
    }
  }

  static async listLocations() {
    try {
      const response = await axios.get(`${BASE_URL}`);
      return response.data;
    } catch (error) {
      console.error("Error while fetching location list:", error);
      throw error;
    }
  }

  static async getLocationById(locationID) {
    try {
      const response = await axios.get(`${BASE_URL}/${locationID}`);
      return response.data;
    } catch (error) {
      console.error(
        `Error while fetching location with id ${locationID}:`,
        error
      );
      throw error;
    }
  }

  static async getLocationByName(locationName) {
    try {
      const response = await axios.get(`${BASE_URL}?name=${locationName}`);

      if (!response.data || response.data.length === 0) {
        throw new Error(`Location with name '${locationName}' not found`);
      }

      return response.data[0];
    } catch (error) {
      console.error(
        `Error while fetching location with name '${locationName}':`,
        error.message
      );
      throw error;
    }
  }

  static async updateLocation(locationID, updatedLocationData) {
    try {
      const response = await axios.patch(
        `${BASE_URL}/${locationID}`,
        updatedLocationData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(
        `Error while updating Location with id ${locationID}:`,
        error
      );
      throw error;
    }
  }

  static async deleteLocation(locationID) {
    try {
      const response = await axios.delete(`${BASE_URL}/${locationID}`);
      return response.data;
    } catch (error) {
      console.error(
        `Error while deleting Location with id ${locationID}:`,
        error
      );
      throw error;
    }
  }
}

export default LocationService;
