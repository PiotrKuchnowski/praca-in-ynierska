import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { verifyEmployer } from '../Api/Employers';
import { verifyAccount } from '../Api/Users';
import { useUserContext } from '../AppContextProvider';

const AccountVerification = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { allertMessage, setAllertMessage } = useUserContext();
    const [countdown, setCountdown] = useState(3);
    const [veryfing, setVeryfing] = useState(true);

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');
        const pathElements = location.pathname.split('/');
        const isEmployerVerification = pathElements.includes('pracodawca');

        if (token) {
            const handleVerification = async () => {
                try {
                    let response;
                    if (isEmployerVerification) {
                        response = await verifyEmployer(token);
                    } else {
                        response = await verifyAccount(token);
                    }
                    setVeryfing(false);

                    if (response.code === 200) {
                        setAllertMessage('Konto zweryfikowane.');
                    } else {
                        setAllertMessage('Błąd podczas weryfikacji konta.');
                    }
                } catch (error) {
                    setAllertMessage('Błąd podczas weryfikacji konta.');
                } finally {
                    const interval = setInterval(() => {
                        setCountdown((prevCountdown) => {
                            if (prevCountdown === 1) {
                                clearInterval(interval);
                                navigate('/');
                            }
                            return prevCountdown - 1;
                        });
                    }, 1000);
                }
            };
            handleVerification();
        } else {
            setAllertMessage('Brak tokenu w adresie URL.');
            navigate('/');
        }
    }, [navigate, location.pathname, setAllertMessage]);

    useEffect(() => {
        const messageWithouCountdown = allertMessage.replace(/Przekierowuję na stronę główną za \d+ sekundy\.\.\./, '');
        if (countdown > 0 && !veryfing) {
            setAllertMessage(`${messageWithouCountdown} Przekierowuję na stronę główną za ${countdown} sekundy...`);
        }else {
            setAllertMessage('');
        }
    }, [countdown, setAllertMessage]);

    return null;
};

export default AccountVerification;