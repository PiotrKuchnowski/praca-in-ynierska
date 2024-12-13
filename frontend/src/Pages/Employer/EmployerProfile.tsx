import React, { use, useEffect, useState, useRef } from "react";
import { Alert, Button, Card, Container, Form } from "react-bootstrap";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import { Link } from "react-router-dom";
import { useUserContext } from "../../AppContextProvider";
import { UpdateEmployerForm } from "../../types";
import { translateFieldsToPolish } from "../../Utils/EmployerUtils";
import { getEmployer, updateEmployer } from "../../Api/Employers";
import { set } from "date-fns";

const EmployerProfile = () => {
    const { employer, setEmployer } = useUserContext();
    const [editMode, setEditMode] = useState(false);
    const [formData, setFormData] = useState<UpdateEmployerForm>({
        companyName: '',
        nip: '',
        description: '',
    });

    const [errorMessage, setErrorMessage] = useState<string>('');
    const [successMessage, setSuccessMessage] = useState<string>('');

    const [buttonText, setButtonText] = useState("Edytuj");

    const fieldsInPolish = useRef<{ [key: string]: string }>({});

    useEffect(() => {
        if (employer) {
            setFormData({
                companyName: employer.companyName,
                nip: employer.nip,
                description: employer.description,
            });
        }
    }, [employer]);

    useEffect(() => {
        const fetchEmployer = async () => {
            try {
                const updatedEmployer = await getEmployer();
                setEmployer(updatedEmployer);
                
            } catch (error) {
                setErrorMessage('Wystąpił błąd podczas pobierania danych pracodawcy: ' + error);
            }
        }
        fetchEmployer();
        fieldsInPolish.current = translateFieldsToPolish(Object.keys(formData));
    }, []);

    const updateEmployerProfile = async () => {
        try {
            const updatedEmployer = await updateEmployer(formData);
            setEmployer(updatedEmployer);
            if(updatedEmployer) {
                setSuccessMessage('Profil pracodawcy zaktualizowany pomyślnie!');
                const timer = setTimeout(() => {
                    setSuccessMessage('');
                }, 5000);
                return () => clearTimeout(timer);
            }
        } catch (error) {
            setErrorMessage('Wystąpił błąd podczas aktualizacji profilu pracodawcy: ' + error);
            const timer = setTimeout(() => {
                setErrorMessage('');
            }, 5000);
            return () => clearTimeout(timer);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleButtonClick = async () => {
        if (editMode) {
            await updateEmployerProfile();
            setButtonText("Edytuj");
        } else {
            setButtonText("Zapisz");
        }
        setEditMode(!editMode);
    };

    return (
        <Container>
            <Breadcrumb>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>Strona główna</Breadcrumb.Item>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca" }}>Pracodawca</Breadcrumb.Item>
                <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/pracodawca/panel" }}>Panel</Breadcrumb.Item>
                <Breadcrumb.Item active>Profil</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
                {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
                {successMessage && <Alert variant="success">{successMessage}</Alert>}
                <Card.Body>
                    <Form>
                        {Object.keys(formData).map((key) => (
                            <Form.Group key={key} controlId={key}>
                                <Form.Label>{fieldsInPolish.current[key]}</Form.Label>
                                <Form.Control
                                    type="text"
                                    name={key}
                                    value={formData[key as keyof typeof formData]}
                                    onChange={handleInputChange}
                                    disabled={!editMode}
                                />
                            </Form.Group>
                        ))}
                        <Button onClick={handleButtonClick}>{buttonText}</Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    )
};

export default EmployerProfile;