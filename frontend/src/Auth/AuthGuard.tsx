import React, { useEffect } from "react";
import { AuthGuardProps } from "../types";
import { useUserContext } from "../AppContextProvider";
import { useNavigate } from "react-router-dom";



const AuthGuard = (props : AuthGuardProps) => {
    const { isLogged } = useUserContext();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLogged) {
            navigate('/logowanie');
        }
    }, [isLogged, props, navigate]);

    return <>{props.children}</>
    
};


export default AuthGuard;