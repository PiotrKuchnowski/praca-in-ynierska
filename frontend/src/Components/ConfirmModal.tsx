import React from "react";
import { Button, Modal, ModalProps } from "react-bootstrap";


const ConfirmModal: React.FC<ModalProps> = ({ show, onHide, onConfirm, message, message2 }) => {
    const handleConfirm = () => {
        onConfirm();
        if (onHide) onHide();
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{message}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {message2}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Anuluj
                </Button>
                <Button variant="primary" onClick={handleConfirm}>
                    Kontynuuj
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ConfirmModal;