import axios from "axios";

const BASE_URL = process.env.REACT_APP_BASE_URL || 'http://192.168.196.4:8085';
const client = axios.create({
  baseURL: `${BASE_URL}/applications`,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

export const apply = async (offerId : string, message : string) => {
    try {
        const response = await client.post("", {
            jobOfferReferenceId: offerId,
            message: message
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getAllApplicationsForEmployer = async () => {
    try {
        const response = await client.get("/employer");
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getAllApplicationsForUser = async () => {
    try {
        const response = await client.get("/user");
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getAllEmploymentsForEmployer = async () => {
    try {
        const response = await client.get("/employer/accepted");
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getAllEmploymentsForUser = async () => {
    try {
        const response = await client.get("/user/accepted");
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getAllApplicationsForJobOffer = async (jobOfferId: string) => {
    try {
        const response = await client.get(`/job/${jobOfferId}`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getAllEmploymentsForJobOffer = async (jobOfferId: string) => {
    try {
        const response = await client.get(`/job/${jobOfferId}/accepted`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getApplicationByJobOfferId = async (jobOfferId: string) => {
    try {
        const response = await client.get(`/user/job-offer/${jobOfferId}`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}



export const respondToApplication = async (applicationId: string, applicationResponse: string, accepted: boolean) => {
    try {
        const response = await client.patch(`/${applicationId}`, {
            response: applicationResponse,
            accepted: accepted
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const acknowledgeEmployment = async (applicationId: string) => {
    try {
        const response = await client.patch(`/${applicationId}/acknowledge`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const deleteApplication = async (applicationId: string) => {
    try {
        const response = await client.delete(`/${applicationId}`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}
