/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useEffect } from "react";
import { useUserContext } from "../../AppContextProvider";
import NotLoggedIn from "../../Components/NotLoggedIn";
import { useNavigate } from "react-router-dom";

const User = () => {
    const { isLogged, isEmployer } = useUserContext();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLogged) {
            const redirectToCreateEmployer = () => {
                navigate('/rejestracja');
            }
            redirectToCreateEmployer();
        }else if(isLogged){
            navigate('/uzytkownik/panel');
        }
    }, [isEmployer, navigate, isLogged, ]);

    return (

        <>{isLogged ? null : <NotLoggedIn />}</>
    )
};

export default User;
/* eslint-enable @typescript-eslint/no-unused-vars */