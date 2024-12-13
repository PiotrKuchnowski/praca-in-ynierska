import axios from 'axios';
import { UpdateUserForm } from '../types';

const BASE_URL = process.env.REACT_APP_BASE_URL || 'http://192.168.196.4:8085';
const client = axios.create({
    baseURL: `${BASE_URL}/user`,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true,
});

export const login = async (email: string, password: string) => {
    let response: any;

    try{
    
    response = await client.post('/login', {
        email: email,
        password: password,
    });
    console.log(response.data);
    
    return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.response?.data?.message || error.message);
        }
        else {
            throw new Error('An unexpected error occurred');
        }
    }

};

export const register = async (email : string, password : string, firstName: string, lastName: string, birthDate: string, verificationUrl : String) => {
    try {
        const response = await client.post('/register', {
            email: email,
            password: password,
            firstName: firstName,
            lastName: lastName,
            birthDate: birthDate,
            verificationUrl : verificationUrl
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            throw new Error(error.response?.data?.message || error.message);
        } else {
            throw new Error('An unexpected error occurred');
        }
    }
};

export const updateUser = async (formData : UpdateUserForm) => {
    try {
        const response = await axios(`${BASE_URL}/user` ,{
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            data: {
                email: formData.email,
                firstName: formData.firstName,
                lastName: formData.lastName,
                phoneNumber: formData.phoneNumber,
                birthDate: formData.birthDate
            }
        ,
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

export const getUser = async () => {
    try{
        const response = await axios(`${BASE_URL}/user`, {
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
};

export const logout = async () => {
    try {
        const response = await axios(`${BASE_URL}/user/logout`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.log(error)
            if(error.response?.data.code === 401 || error.response?.data.code === 403) {
                return error.response.data;
            }
            throw new Error(error.response?.data?.message || error.message);
        } else {
            console.log(error)
            throw new Error('An unexpected error occurred');
        }
    }
}

export const verifyAccount = async (token: string) => {
    try {
        const response = await axios(`${BASE_URL}/user/verify-account?token=${token}`, {
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
