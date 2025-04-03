import axios from "axios";

const BASE_URL = "http://localhost:8000/routes";

class RouteService {
  static async getRoutes(origin, destination, date) {
    try {
      const response = await axios.get(`${BASE_URL}`, {
        params: {
          origin: origin,
          destination: destination,
          date: date,
        },
      });

      if (!response.data || response.data.length === 0) {
        throw new Error(
          `No routes found from ${origin} to ${destination} on ${date}`
        );
      }

      return response.data;
    } catch (error) {
      console.error(
        `Error while fetching routes from ${origin} to ${destination} on ${date}:`,
        error.message
      );
      throw error;
    }
  }
}

export default RouteService;
