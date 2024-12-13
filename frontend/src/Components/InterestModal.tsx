import React from "react";
import { Modal, Button, Row, Col } from "react-bootstrap";
import { Form } from "react-bootstrap";
import { ModalProps } from "../types";

const InterestModal: React.FC<ModalProps> = ({ show, onHide, onConfirm, message, title, messageTitle, messageWarning }) => {
  const [userMessage, setUserMessageState] = React.useState<string>("");
  const handleConfirm = () => {
    onConfirm(userMessage);
    onHide();
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>{title ? title : "Wyraź zainteresowanie"}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {message ? message : "Czy na pewno chcesz wyrazić zainteresowanie tą ofertą?"}
        <Form.Group controlId="formMessage" className="form-group">
          <Form.Label className={messageTitle ? undefined : "required-asterisk"}>{messageTitle ? messageTitle : "Wiadomość:"}</Form.Label>
          <Form.Control
            as="textarea"
            placeholder="Wprowadź wiadomość"
            defaultValue={userMessage}
            onChange={(e) => setUserMessageState(e.target.value)}
            isInvalid={userMessage.trim() === ''}
            required
          />
          <Col>
            <Form.Text className="text-muted">
              {messageWarning ? messageWarning : "Wiadomość ta zostanie przekazana do pracodawcy."}
            </Form.Text>
            <Row className="mt-3 justify-content-end g-2">             
             <Button variant="secondary" onClick={onHide}>
                Anuluj
              </Button>
              <Button type="submit" variant="primary" onClick={handleConfirm}>
                Potwierdź
              </Button>
            </Row>
          </Col>
        </Form.Group>
      </Modal.Body>
    </Modal>
  );
};

export default InterestModal;