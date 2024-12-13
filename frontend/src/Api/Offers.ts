import axios from "axios";
import { CreateOffer } from "../types";

const BASE_URL = process.env.REACT_APP_BASE_URL || 'http://192.168.196.4:8085';
const client = axios.create({
  baseURL: `${BASE_URL}/job-offers`,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

export const createOffer = async (offer : CreateOffer) => {
    console.log(offer);

    try {
        const response = await client.post("", {
            startDate: offer.startDate,
            endDate: offer.endDate,
            description: offer.description,
            title: offer.title,
            pay: offer.pay,
            payType: offer.payType,
            locations: offer.locations,
            jobCategory: offer.jobCategory
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

export const getAllOffers = async () => {
    try {
        const response = await client.get("");
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getEmployerOffers = async () => {
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

export const getOfferById = async (jobOfferId : string) => {
    try {
        const response = await client.get(`/${jobOfferId}`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getCategories = async () => {
    try {
        const response = await client.get("/categories");
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const getCities = async () => {
    try {
        const response = await client.get("/cities");
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}

export const updateOffer = async (jobOfferId : String, offer : CreateOffer) => {
    try {
        const response = await client.patch(`/${jobOfferId}`, {
            startDate: offer.startDate,
            endDate: offer.endDate,
            description: offer.description,
            title: offer.title,
            pay: offer.pay,
            payType: offer.payType,
            locations: offer.locations,
            jobCategory: offer.jobCategory
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


export const deleteOffer = async (referenceId: string) => {
    try {
        const response = await client.delete(`/${referenceId}`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || error.message);
        } else {
        throw new Error("An unexpected error occurred");
        }
    }
}
