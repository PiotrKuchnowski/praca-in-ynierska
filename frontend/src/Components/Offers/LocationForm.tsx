import React from "react";
import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
// import Button from "react-bootstrap/Button";
import { LocationFormProps } from "../../types";

const LocationForm: React.FC<LocationFormProps> = ({ index, location, onLocationChange}) => {
  const handleStringChange = (field: string, value: string) => {
    const newLocation = { ...location, [field]: value };
    onLocationChange(index, newLocation);
  };

  const handleBooleanChange = (field: string, value: boolean) => {
    const newLocation = { ...location, [field]: value };
    onLocationChange(index, newLocation);
  };

  return (
    <>
      <Form.Group controlId={`formRemote-${index}`} className="form-group">
        <Form.Check
          type="checkbox"
          label="Praca zdalna"
          checked={location.remote || false}
          onChange={(e) => handleBooleanChange('remote', e.target.checked)}
        />
      </Form.Group>

      {!location.remote && (
        <Container className="address-container">
          <Form.Group controlId={`formCountry-${index}`} className="form-group">
            <Form.Label className="required-asterisk">Kraj:</Form.Label>
            <Form.Control
              type="text"
              placeholder="Wprowadź kraj"
              value={location.country || ''}
              onChange={(e) => handleStringChange('country', e.target.value)}
              required
            />
          </Form.Group>

          <Form.Group controlId={`formPostalCode-${index}`} className="form-group">
            <Form.Label className="required-asterisk">Kod pocztowy:</Form.Label>
            <Form.Control
              type="text"
              placeholder="Wprowadź kod pocztowy"
              value={location.postalCode || ''}
              onChange={(e) => handleStringChange('postalCode', e.target.value)}
              required
            />
          </Form.Group>

          <Form.Group controlId={`formCity-${index}`} className="form-group">
            <Form.Label className="required-asterisk">Miasto:</Form.Label>
            <Form.Control
              type="text"
              placeholder="Wprowadź miasto"
              value={location.city || ''}
              onChange={(e) => handleStringChange('city', e.target.value)}
              required
            />
          </Form.Group>

          <Form.Group controlId={`formStreet-${index}`} className="form-group">
            <Form.Label>Ulica:</Form.Label>
            <Form.Control
              type="text"
              placeholder="Wprowadź ulicę"
              value={location.street || ''}
              onChange={(e) => handleStringChange('street', e.target.value)}
            />
          </Form.Group>

          <Form.Group controlId={`formHouseNumber-${index}`} className="form-group">
            <Form.Label className="required-asterisk">Numer domu:</Form.Label>
            <Form.Control
              type="text"
              placeholder="Wprowadź numer domu"
              value={location.houseNumber || ''}
              onChange={(e) => handleStringChange('houseNumber', e.target.value)}
              required
            />
          </Form.Group>

          <Form.Group controlId={`formApartmentNumber-${index}`} className="form-group">
            <Form.Label>Numer mieszkania:</Form.Label>
            <Form.Control
              type="text"
              placeholder="Wprowadź numer mieszkania"
              value={location.apartmentNumber || ''}
              onChange={(e) => handleStringChange('apartmentNumber', e.target.value)}
            />
          </Form.Group>
        </Container>
      )}
    </>
  );
};

export default LocationForm;