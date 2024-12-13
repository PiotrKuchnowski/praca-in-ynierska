/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useEffect } from "react";
import { useUserContext } from "../../AppContextProvider";
import NotLoggedIn from "../../Components/NotLoggedIn";
import { useNavigate } from "react-router-dom";
import EmployerPanel from "./EmployerPanel";

const Employer = () => {
    const { isLogged, isEmployer } = useUserContext();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isEmployer && isLogged) {
            const redirectToCreateEmployer = () => {
                navigate('/pracodawca/utworz-podkonto');
            }
            redirectToCreateEmployer();
        }else if(isEmployer && isLogged){
            navigate('/pracodawca/panel');
        }
    }, [isEmployer, navigate, isLogged, ]);

    return (

        <>{isLogged ? null : <NotLoggedIn />}</>
    )
};

export default Employer;
/* eslint-enable @typescript-eslint/no-unused-vars */