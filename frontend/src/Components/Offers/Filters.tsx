import React, { ChangeEvent } from 'react';
import { Container, Row, Col, Form } from 'react-bootstrap';
import { FiltersProps } from '../../types';
import './Styles/Filters.scss';

const Filters = (props : FiltersProps) => {
  return (
    <Container className="filter-pane card p-3">
      {/* Offers Per Page Filter */}
      <Row className="mb-3">
        <Col>
          <Form.Group controlId="offersPerPageSelect">
            <Form.Label>Oferty na stronę</Form.Label>
            <Form.Control
              as="select"
              value={props.offersPerPage}
              onChange={(event) => props.handleOffersPerPageChange(event as unknown as React.ChangeEvent<HTMLSelectElement>)}
            >
              <option value={15}>15</option>
              <option value={30}>30</option>
              <option value={45}>45</option>
              <option value={60}>60</option>
              <option value={75}>75</option>
              <option value={90}>90</option>
            </Form.Control>
          </Form.Group>
        </Col>
      </Row>

      {/* Category Filter */}
      <Row className="mb-3">
        <Col>
          <Form.Group controlId="categorySelect">
            <Form.Label>Kategoria</Form.Label>
            <Form.Control
              as="select"
              value={props.selectedCategory}
              onChange={(event) => props.handleCategoryChange(event as unknown as React.ChangeEvent<HTMLSelectElement>)}
            >
              <option value="">Wszystkie</option>
              {props.categories.map((category) => (
                <option key={category.name} value={category.name}>{category.name}</option>
              ))}
            </Form.Control>
          </Form.Group>
        </Col>
      </Row>

      
      <Row className="mb-3">
        <Col>
          <Form.Group controlId="locationSelect">
            <Form.Label>Lokalizacja</Form.Label>
            <Form.Control
              as="select"
              value={props.selectedCity}
              onChange={(event) => props.handleLocationChange(event as unknown as React.ChangeEvent<HTMLSelectElement>)}
            >
              <option value="">Wszystkie</option>
              {props.cities.filter((city) => !!city.name).map((city) => (
                <option key={city.name} value={city.name}>{city.name}</option>
              ))}
            </Form.Control>
          </Form.Group>
        </Col>
      </Row>

      <Row className="mb-3 align-items-center">
        <Col xs={6} className="pr-2">
          <Form.Group controlId="payRangeMin">
            <Form.Label>Minimalna stawka</Form.Label>
            <Form.Control
              type="number"
              placeholder="Min"
              value={props.minPay}
              onChange={(event: ChangeEvent<HTMLInputElement>) => props.handlePayRangeChange(Number(event.target.value), props.maxPay)}
            />
          </Form.Group>
        </Col>
        <Col xs={6} className="pl-2">
          <Form.Group controlId="payRangeMax">
            <Form.Label>Maksymalna stawka</Form.Label>
            <Form.Control
              type="number"
              placeholder="Max"
              value={props.maxPay}
              onChange={(event: ChangeEvent<HTMLInputElement>) => props.handlePayRangeChange(props.minPay, Number(event.target.value))}
            />
          </Form.Group>
        </Col>
        <Col xs={12}>
          <Form.Group controlId="payTypeSelect">
            <Form.Label>Typ stawki</Form.Label>
            <Form.Control
              as="select"
              value={props.selectedPayType}
              onChange={(event) => props.handlePayTypeChange(Number(event.target.value))}
            >
              <option value="">Wszystkie</option>
              <option value={2}>Godzinowa</option>
              <option value={1}>Miesięczna</option>
              <option value={3}>Dzienna</option>
              <option value={4}>Od zlecenia</option>
            </Form.Control>
          </Form.Group>
        </Col>
      </Row>

      <Row className="mb-3">
        <Col>
          <Form.Group controlId="remoteToggle">
            <Form.Check
              type="checkbox"
              label="Zdalna"
              checked={props.remote}
              onChange={props.handleRemoteToggle}
            />
          </Form.Group>
        </Col>
      </Row>
    </Container>
  );
};

export default Filters;
