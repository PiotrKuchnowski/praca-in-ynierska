import React from "react";
import { Button, Modal, ModalProps } from "react-bootstrap";

const CookiesModal: React.FC<ModalProps> = ({ show, onHide }) => {
    return (
      <Modal show={show} onHide={onHide} backdrop="static" keyboard={false}>
        <Modal.Header>
          <Modal.Title>Ciasteczka</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            Strona korzysta z ciasteczek (ang. cookies). Dalsze korzystanie z portalu oznacza zgodę na ich wykorzystanie.
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={onHide}>
            Rozumiem, przechodzę dalej!
          </Button>
        </Modal.Footer>
      </Modal>
    );
};

export default CookiesModal;