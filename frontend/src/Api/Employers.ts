import axios from "axios";

const BASE_URL = process.env.REACT_APP_BASE_URL || 'http://192.168.196.4:8085';



export const createEmployer = async (userReferenceId: string, companyName: string, companyNIP: string, description: string, isNaturalPerson: boolean, verificationUrl : string) => {
    console.log(userReferenceId, companyName, companyNIP, description, isNaturalPerson);
    try {
        const response = await axios(`${BASE_URL}/employer/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            data: {
                companyName: companyName,
                nip: companyNIP,
                description: description,
                isNaturalPerson: isNaturalPerson,
                verificationUrl: verificationUrl
            },
            withCredentials: true

        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.response?.data?.message || error.message);
        } else {
            throw new Error('An unexpected error occurred');
        }
    }
}

export const verifyEmployer = async (token: string) => {
    try {
        const response = await axios(`${BASE_URL}/employer/verify-account?token=${token}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.response?.data?.message || error.message);
        } else {
            throw new Error('An unexpected error occurred');
        }
    }
}

export const getEmployer = async () => {
    try {
        const response = await axios(`${BASE_URL}/employer`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.response?.data?.message || error.message);
        } else {
            throw new Error('An unexpected error occurred');
        }
    }
}

export const updateEmployer = async (formData: any) => {
    try {
        const response = await axios(`${BASE_URL}/employer`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            data: {
                companyName: formData.companyName,
                nip: formData.nip,
                description: formData.description
            },
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.response?.data?.message || error.message);
        } else {
            throw new Error('An unexpected error occurred');
        }
    }
}