import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
import { UserContextType } from './types';
import { getUser } from './Api/Users';
import { getEmployer } from './Api/Employers';
import Alert from 'react-bootstrap/Alert';
import Spinner from 'react-bootstrap/Spinner';
import Container from 'react-bootstrap/Container';
import Text from 'react-bootstrap/FormText';
import './Styles/Spinner.scss';
import './Styles/AppContextProvider.scss';
import CookiesModal from './Components/CookiesModal';
export const UserContext = createContext<UserContextType | undefined>(undefined);

// Create a provider component
export const UserContextProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<any>(null);
  const [employer, setEmployer] = useState<any>(null);
  const [isLogged, setIsLogged] = useState<boolean>(false);
  const [isEmployer, setIsEmployer] = useState<boolean>(false);
  const [allertMessage, setAllertMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);
  const [showCookiesModal, setShowCookiesModal] = useState<boolean>(true);

  const resetContext = () => {
    setUser(null);
    setIsLogged(false);
    setIsEmployer(false);
  };

  useEffect(() => {
    if (localStorage.getItem("cookiesAcceptance") === null || localStorage.getItem("cookiesAcceptance") === "false") {
      localStorage.setItem("cookiesAcceptance", "false")
      setShowCookiesModal(true);
    } else if (localStorage.getItem("cookiesAcceptance") === "true") {
      setShowCookiesModal(false)
    }

    const logout = () => {
      console.log('wasLogged:', localStorage.getItem('wasLogged'));
      console.log('isLogged:', isLogged);
      if (localStorage.getItem('wasLogged') === 'true' && isLogged === false) {
        setAllertMessage('Sesja wygasła. Zaloguj się ponownie.');
        const timer = setTimeout(() => {
          setAllertMessage('');
          localStorage.setItem('wasLogged', 'false');
        }, 5000); // Hide alert after 5 seconds
        return () => clearTimeout(timer);
      }
    }

    const tryToGetEmployer = async () => {
      try {
        const response = await getEmployer();
        console.log('Employer response:', response);
        if (response.code === 200) {
          setEmployer(response.data.employer);
          console.log('Employer:', employer);
        }
      } catch (error) {
        console.error('Error while getting employer data:', error);
      }
    }

    const tryToGetUser = async () => {
      try {
        const response = await getUser();
        if (response.code === 200) {
          setUser(response.data.user);
          setIsLogged(true);
          localStorage.setItem('wasLogged', 'true');
          setIsEmployer(response.data.user.isEmployer);
          if (response.data.user.isEmployer) {
            await tryToGetEmployer();
          }
        }
      } catch (error) {
        console.error('Error while getting user data:', error);
        setIsLogged(false);
        setIsEmployer(false);
        setUser({ firstName: 'Nieznajomy' });
        logout();
      } finally {
        setLoading(false);

      }
    }
    tryToGetUser();

  }, [isLogged]);



  if (loading) {
    console.log(loading);
    return (
      <Container className="loading-container">
        <Container className="login-in-overlay">
          <Spinner animation="border" role="status" variant="primary" />
          <Text className="sr-only">Ładowanie aplikacji...</Text>
        </Container>
      </Container>
    );
  }

  const handleCookiesMessageAcceptance = () => {
    localStorage.setItem("cookiesAcceptance", "true")
    setShowCookiesModal(false)
  }

  return (
    <UserContext.Provider
      value={{
        user,
        setUser,
        isLogged,
        setIsLogged,
        isEmployer,
        setIsEmployer,
        employer,
        setEmployer,
        resetContext,
        allertMessage,
        setAllertMessage,
      }}
    >
      <CookiesModal show={showCookiesModal} onHide={handleCookiesMessageAcceptance} />
      {allertMessage && <Alert className="alert" variant="info">{allertMessage}</Alert>}
      {children}
    </UserContext.Provider>

  );
};


export const useUserContext = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUserContext must be used within a UserContextProvider');
  }
  return context;
};