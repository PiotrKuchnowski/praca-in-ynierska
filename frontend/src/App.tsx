import React from 'react';
import { useEffect, } from 'react';
import { useNavigate } from 'react-router-dom';
import CustomNavbar from './Components/CustomNavbar';
import CustomFooter from './Components/CustomFooter';
import Login from './Pages/User/Login';
import Register from './Pages/User/Register';
import Home from './Pages/Home';
import './Styles/App.scss';
import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider, Outlet, Navigate } from 'react-router-dom';
import Offer from './Pages/Offers/Offer';
import Offers from './Pages/Offers/Offers';
import Employer from './Pages/Employer/Employer';
import UserProfile from './Pages/User/UserProfile';
import CreateEmployer from './Pages/Employer/CreateEmployer';
import EmployerPanel from './Pages/Employer/EmployerPanel';
import OfferForm from './Pages/Offers/OfferForm';
import ManageOffers from './Pages/Employer/ManageOffers';
import Interested from './Pages/Employer/Interested';
import EmployerProfile from './Pages/Employer/EmployerProfile';
import AuthGuard from './Auth/AuthGuard';
import UserPanel from './Pages/User/UserPanel';
import User from './Pages/User/User';
import EmployerOffer from './Pages/Employer/EmployerOffer';
import Logout from './Pages/User/Logout';
import AccountVerification from './Pages/AccountVerification';
import AppliedOffers from './Pages/Employee/AppliedOffers';
import EmployedOffers from './Pages/Employee/EmployedOffers';
import ApplicationReview from './Pages/Employee/ApplicationReview';
import EmploymentReview from './Pages/Employee/EmploymentReview';
import { UserContextProvider } from './AppContextProvider';


const basename = process.env.REACT_APP_BASENAME || '';


const RedirectHandler = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const redirectPath = params.get('redirect');
    if (redirectPath) {
      navigate(redirectPath);
    }
  }, [navigate]);

  return null;
};

const App = () => {
  return (

    <>
    <UserContextProvider>
      <RedirectHandler />
      <div className='App'>
        <header>
          <CustomNavbar />
        </header>
        <main>
          <Outlet />
        </main>
        <footer>
          <CustomFooter />
        </footer>
      </div>
    </UserContextProvider>
    </>

  );
}

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<App />}>
      <Route path="" element={<Home />} />
      <Route path="/oferty" element={<Offers />} />
      <Route path="/oferty/:name/:offerId" element={<Offer />} />
      <Route path="/logowanie" element={<Login />} />
      <Route path="/rejestracja" element={<Register />} />
      <Route path="/pracodawca" element={<Employer />} />
      <Route path="/pracodawca/utworz-podkonto" element={<CreateEmployer />} />
      <Route path="/pracodawca/panel" element={<AuthGuard><EmployerPanel /></AuthGuard>} />
      <Route path="/pracodawca/panel/dodaj-oferte" element={<AuthGuard><OfferForm /></AuthGuard>} />
      <Route path="/pracodawca/panel/zarzadzaj-ofertami" element={<AuthGuard><ManageOffers /></AuthGuard>} />
      <Route path="/pracodawca/panel/zarzadzaj-ofertami/:name/:offerId" element={<AuthGuard><EmployerOffer /></AuthGuard>} />
      <Route path="/pracodawca/panel/zainteresowani-kandydaci" element={<AuthGuard><Interested /></AuthGuard>} />
      <Route path="/pracodawca/panel/profil" element={<AuthGuard><EmployerProfile /></AuthGuard>} />
      <Route path="/uzytkownik" element={<AuthGuard><User /></AuthGuard>} />
      <Route path="/uzytkownik/panel" element={<AuthGuard><UserPanel /></AuthGuard>} />
      <Route path="/uzytkownik/panel/profil" element={<AuthGuard><UserProfile /></AuthGuard>} />
      <Route path="/uzytkownik/panel/aplikacje" element={<AuthGuard><AppliedOffers /></AuthGuard>} />
      <Route path="/uzytkownik/panel/aplikacje/:name/:offerId" element={<AuthGuard><ApplicationReview /></AuthGuard>} />
      <Route path="/uzytkownik/panel/kontrakty/:name/:offerId" element={<AuthGuard><EmploymentReview /></AuthGuard>} />
      <Route path="/uzytkownik/panel/kontrakty" element={<AuthGuard><EmployedOffers /></AuthGuard>} />
      <Route path="/logout" element={<AuthGuard><Logout /></AuthGuard>} />
      <Route path="/pracodawca/zweryfikuj-konto" element={<AccountVerification />} />
      <Route path="/uzytkownik/zweryfikuj-konto" element={<AccountVerification />} />
      <Route path="/pracodawca/panel/zarzadzaj-ofertami/:name/:offerId/edytuj-oferte" element={<AuthGuard><OfferForm /></AuthGuard>} />
      <Route path="*" element={<Navigate to="/" />} />
    </Route>
  ),
  {
    basename: basename
  }
);

const AppWithRouter = () => (
  <RouterProvider router={router}>
    <App />
  </RouterProvider>
);

export default AppWithRouter;

