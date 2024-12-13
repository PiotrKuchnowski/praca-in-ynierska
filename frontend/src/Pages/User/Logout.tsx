import { useEffect } from "react";
import { logout } from "../../Api/Users";
import { useNavigate } from "react-router-dom";
import { useUserContext } from "../../AppContextProvider";

const Logout = () => {
    const navigate = useNavigate();
    const { resetContext, setAllertMessage } = useUserContext();
    useEffect(() => {
        const handleLogout = async () => {
            try {
                const responseData = await logout();
                console.log(responseData);
                if (responseData.code === 200 || responseData.code === 401 || responseData.code === 403) {
                    console.log('Wylogowano pomyślnie');
                    localStorage.setItem('wasLogged', 'false');
                    setAllertMessage('Zostałeś pomyślnie wylogowany');
                    setTimeout(() => {
                        setAllertMessage('');
                    }, 5000);
                    resetContext();
                } else {
                    console.log('Błąd podczas wylogowywania');
                    console.log(responseData);
                }
                navigate('/');
            } catch (error) {
                console.log('Błąd podczas wylogowywania');
                console.log(error);
                navigate('/');
            }
        };
        handleLogout();
    },);
    return null;
}
export default Logout;