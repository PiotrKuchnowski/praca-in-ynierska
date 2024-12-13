import React, { useEffect, useState, useMemo } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Alert from "react-bootstrap/Alert";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Spinner } from "react-bootstrap";
import { createOffer, getCategories, updateOffer } from "../../Api/Offers";
import LocationForm from "../../Components/Offers/LocationForm";
import "../User/Styles/Login.scss";
import { CreateLocation, PayType } from "../../types";
import { Category } from "../../types";

const OfferForm = () => {
  const [categories, setCategories] = useState([] as Category[]);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [startHour, setStartHour] = useState('');
  const [endHour, setEndHour] = useState('');
  const [description, setDescription] = useState('');
  const [title, setTitle] = useState('');
  const [pay, setPay] = useState('');
  const [payType, setPayType] = useState<PayType>({ payTypeID: 0, name: '' });
  const [jobCategory, setJobCategory] = useState('');
  const [customCategory, setCustomCategory] = useState('');
  const [locations, setLocations] = useState<CreateLocation[]>([{
    remote: false, country: '', postalCode: '', city: '', street: '', houseNumber: '', apartmentNumber: ''
  }]);
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const [editMode, setEditMode] = useState(false);
  const jobOfferId = location.pathname.split('/')[3];

  const handleLocationChange = (index: number, location: any) => {
    const newLocations = [...locations];
    newLocations[index] = location;
    setLocations(newLocations);
  };

  const addLocation = () => {
    setLocations([...locations, {
      remote: false,
      country: '',
      postalCode: '',
      city: '',
      street: '',
      houseNumber: '',
      apartmentNumber: ''
    }]);
  };

  const removeLocation = (index: number) => {
    const newLocations = [...locations];
    newLocations.splice(index, 1);
    setLocations(newLocations);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoading(true);
    setSuccessMessage('');
    setErrorMessage('');
    window.scrollTo(0, 0);
    try {
      const combinedStartDate = startDate + 'T' + startHour + ':00';
      var combinedEndDate = '';
      if (endDate !== '') {
        combinedEndDate = endDate + 'T' + endHour + ':00';
      }
      const category = jobCategory === 'custom' ? customCategory : jobCategory;
      var responseData;
      if (editMode) {
        responseData = await updateOffer(jobOfferId, { startDate: combinedStartDate, endDate: combinedEndDate, description, title, pay, payType, locations, jobCategory: category });
      } else {
        responseData = await createOffer({ startDate: combinedStartDate, endDate: combinedEndDate, description, title, pay, payType, locations, jobCategory: category });
      }
      if (responseData.code === 200) {
        if (editMode) {
          setSuccessMessage('Oferta została zaktualizowana pomyślnie');
        }
        else {
          setSuccessMessage('Oferta została dodana pomyślnie');
        }
        setTimeout(() => {
          navigate('/pracodawca/panel');
        }, 2000);
      } else {
        if (editMode) {
          setErrorMessage('Aktualizacja oferty nieudana: ' + responseData.message);
        } else {
          setErrorMessage('Dodawanie oferty nieudane: ' + responseData.message);
        }
      }
    } catch (error) {
      console.log(error);
      if (editMode) {
        setErrorMessage('Wystąpił błąd podczas aktualizacji oferty: ' + error);
      } else {
        setErrorMessage('Wystąpił błąd podczas dodawania oferty: ' + error);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const retrieveCategories = async () => {
      try {
        const response = await getCategories();
        if (response.code === 200) {
          console.log(response.data);
          setCategories(response.data.categories);
        }
      } catch (error) {
        console.error('Error retrieving job categories:', error);
      }
    };
    retrieveCategories();

    if (location.pathname.includes('/edytuj-oferte')) {
      setEditMode(true);
      const offer = location.state?.offer;
      setStartDate(offer?.startDate.split('T')[0]);
      setStartHour(offer?.startDate.split('T')[1].split(':')[0] + ':' + offer?.startDate.split('T')[1].split(':')[1]);
      setEndDate(offer?.endDate?.split('T')[0]);
      setEndHour(offer?.endDate?.split('T')[1].split(':')[0] + ':' + offer?.endDate?.split('T')[1].split(':')[1]);
      setDescription(offer?.description);
      setTitle(offer?.title);
      setPay(offer?.pay);
      setPayType({ payTypeID: offer?.payType.payTypeID, name: offer?.payType.name });
      setJobCategory(offer?.jobCategory);
      setLocations(offer?.locations);

    }

  }, [location.pathname, location.state?.offer]);

  const getBreadcrumbs = useMemo(() => {
    if (editMode) {
      const pathSegments = location.pathname.split('/');
      const name = pathSegments[pathSegments.length - 3];
      const offerId = pathSegments[pathSegments.length - 2];
      return (
        <Breadcrumb>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel" }}>Panel</Breadcrumb.Item>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel/zarzadzaj-ofertami" }}>Zarządzaj ofertami</Breadcrumb.Item>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: `/pracodawca/panel/zarzadzaj-ofertami/${name}/${offerId}`, state: { manageMode: true } }}>Szczegóły ofertę</Breadcrumb.Item>
          <Breadcrumb.Item active>Edytuj ofertę</Breadcrumb.Item>
        </Breadcrumb>
      );
    } else {
      return (
        <Breadcrumb>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel" }}>Panel</Breadcrumb.Item>
          <Breadcrumb.Item active>Dodaj ofertę</Breadcrumb.Item>
        </Breadcrumb>
      );
    };
  }, [editMode, location.pathname]);

  return (
    <Container className="container-relative">
      {getBreadcrumbs}
      {loading && (
        <div className="loading-overlay">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
        </div>
      )}
      <Container className="add-offer-background">
        <Container className="add-offer-box">
          {successMessage && (
            <Alert variant="success" className="text-center">
              {successMessage}
            </Alert>
          )}
          {errorMessage && (
            <Alert variant="danger" className="text-center">
              {errorMessage}
            </Alert>
          )}
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formStartDate" className="form-group">
              <Form.Label className="required-asterisk">Data rozpoczęcia:</Form.Label>
              <Form.Control
                type="date"
                placeholder="Wprowadź datę rozpoczęcia"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                required
              />
              <Form.Control
                type="time"
                placeholder="Wprowadź godzinę rozpoczęcia"
                value={startHour}
                onChange={(e) => setStartHour(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="formEndDate" className="form-group">
              <Form.Label>Data zakończenia:</Form.Label>
              <Form.Control
                type="date"
                placeholder="Wprowadź datę zakończenia"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
              <Form.Control
                type="time"
                placeholder="Wprowadź godzinę zakończenia"
                value={endHour}
                onChange={(e) => setEndHour(e.target.value)}
              />
            </Form.Group>

            <Form.Group controlId="formTitle" className="form-group">
              <Form.Label className="required-asterisk">Tytuł:</Form.Label>
              <Form.Control
                type="text"
                placeholder="Wprowadź tytuł"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="formDescription" className="form-group">
              <Form.Label className="required-asterisk">Opis:</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="Wprowadź opis"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="formJobCategory" className="form-group">
              <Form.Label className="required-asterisk">Kategoria:</Form.Label>
              <Form.Control
                as="select"
                value={jobCategory}
                onChange={(e) => setJobCategory(e.target.value)}
                required
                placeholder="Wybierz kategorię"
              >
                <option value="">Wybierz kategorię</option>
                {Array.isArray(categories) && categories.map((category, index) => (
                  <option key={index} value={category.name}>{category.name}</option>
                ))}
                <option value="custom">Dodaj własną kategorię</option>
              </Form.Control>
              {jobCategory === 'custom' && (
                <Form.Control
                  type="text"
                  value={customCategory}
                  onChange={(e) => setCustomCategory(e.target.value)}
                  placeholder="Wpisz własną kategorię"
                  className="mt-2"
                />
              )}
            </Form.Group>

            <Form.Group controlId="formPay" className="form-group">
              <Form.Label className="required-asterisk">Wynagrodzenie:</Form.Label>
              <Form.Control
                type="number"
                placeholder="Wprowadź wynagrodzenie"
                value={pay}
                onChange={(e) => setPay(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="formPayType" className="form-group">
              <Form.Label className="required-asterisk">Typ wynagrodzenia:</Form.Label>
              <Form.Control
                as="select"
                value={payType.name}
                onChange={(e) => setPayType({ ...payType, name: e.target.value })}
                required
              >
                <option value="">Wybierz typ wynagrodzenia</option>
                <option value="Godzinowa">Godzinowa</option>
                <option value="Miesięczna">Miesięczna</option>
              </Form.Control>
            </Form.Group>

            {locations.map((location, index) => (
              <><LocationForm
                key={index}
                index={index}
                location={location}
                onLocationChange={handleLocationChange} />
                {index > 0 &&
                  <Button variant="danger" onClick={() => removeLocation(index)} className="mb-3">
                    Usuń lokalizację
                  </Button>
                }
              </>
            ))}

            <Button variant="secondary" onClick={addLocation} className="button">
              Dodaj lokalizację
            </Button>

            <Button variant="primary" type="submit" className="button" disabled={loading}>
              {editMode ? 'Zapisz zmiany' : 'Dodaj ofertę'}
            </Button>

          </Form>
        </Container>
      </Container>
    </Container>
  );
};

export default OfferForm;