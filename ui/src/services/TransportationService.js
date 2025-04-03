import axios from "axios";

const BASE_URL = "http://localhost:8000/transportations";

class TransportationService {
  static async createTransportation(transportationData) {
    try {
      const response = await axios.post(`${BASE_URL}`, transportationData);
      return response.data;
    } catch (error) {
      console.error("Error while creating transportation:", error);
      throw error;
    }
  }

  static async listTransportations() {
    try {
      const response = await axios.get(`${BASE_URL}`);
      return response.data;
    } catch (error) {
      console.error("Error while fetching transportation list:", error);
      throw error;
    }
  }

  static async getTransportationById(transportationID) {
    try {
      const response = await axios.get(`${BASE_URL}/${transportationID}`);
      return response.data;
    } catch (error) {
      console.error(
        `Error while fetching transportation with id ${transportationID}:`,
        error
      );
      throw error;
    }
  }

  static async updateTransportation(
    transportationID,
    updatedTransportationData
  ) {
    try {
      const response = await axios.patch(
        `${BASE_URL}/${transportationID}`,
        updatedTransportationData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(
        `Error while updating Transportation with id ${transportationID}:`,
        error
      );
      throw error;
    }
  }

  static async deleteTransportation(transportationID) {
    try {
      const response = await axios.delete(`${BASE_URL}/${transportationID}`);
      return response.data;
    } catch (error) {
      console.error(
        `Error while deleting Transportation with id ${transportationID}:`,
        error
      );
      throw error;
    }
  }
}

export default TransportationService;
