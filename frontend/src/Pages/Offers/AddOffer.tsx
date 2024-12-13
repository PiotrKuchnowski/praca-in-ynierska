import React, { useEffect, useState } from "react";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Alert from "react-bootstrap/Alert";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import { Link, useNavigate } from "react-router-dom";
import { Spinner } from "react-bootstrap";
import { createOffer, getCategories } from "../../Api/Offers";
import LocationForm from "../../Components/Offers/LocationForm";
import "../Styles/Login.scss";
import { PayType } from "../../types";
import { Category } from "../../types";
import { useUserContext } from "../../AppContextProvider";

const AddOffer = () => {
  const { user } = useUserContext();
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
  const [locations, setLocations] = useState([{
    remote: false,
    country: '',
    postalCode: '',
    city: '',
    street: '',
    houseNumber: '',
    apartmentNumber: ''
  }]);
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

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
      const responseData = await createOffer({ startDate: combinedStartDate, endDate: combinedEndDate, description, title, pay, payType, locations, jobCategory: category });
      if (responseData.code === 200) {
        setSuccessMessage('Oferta dodana pomyślnie!');
        setTimeout(() => {
          navigate('/pracodawca/panel');
        }, 2000);
      } else {
        setErrorMessage('Dodawanie oferty nieudane: ' + responseData.message);
      }
    } catch (error) {
      console.log(error);
      setErrorMessage('Wystąpił błąd podczas dodawania oferty: ' + error);
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
  }, []);

  return (
    <Container className="container-relative">
      <Breadcrumb>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel" }}>Panel</Breadcrumb.Item>
        <Breadcrumb.Item active>Dodaj ofertę</Breadcrumb.Item>
      </Breadcrumb>
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
              Dodaj ofertę
            </Button>

          </Form>
        </Container>
      </Container>
    </Container>
  );
};

export default AddOffer;
