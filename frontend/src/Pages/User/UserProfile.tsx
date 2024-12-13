import React, { use, useCallback, useEffect, useRef, useState } from "react";
import { useUserContext } from "../../AppContextProvider";
import { Container, Card, Button, Form, Breadcrumb, Alert } from "react-bootstrap";
import { getUser, updateUser } from "../../Api/Users";
import { Link } from "react-router-dom";
import { UpdateUserForm } from "../../types";
import { translateFieldsToPolish } from "../../Utils/UserUtils";
import { error } from "console";

const UserProfile: React.FC = () => {
  const { user, setUser } = useUserContext();
  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState<UpdateUserForm>({
    firstName: user.firstName,
    lastName: user.lastName,
    email: user.email,
    phoneNumber: user.phoneNumber,
    birthDate: user.birthDate,
  });
  const [buttonText, setButtonText] = useState("Edytuj");
  const [errorMessage, setErrorMessage] = useState<string>("");
  const [successMessage, setSuccessMessage] = useState<string>("");

  const fieldsInPolish = useRef<{ [key: string]: string }>({});

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    if (user) {
      setFormData({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        phoneNumber: user.phoneNumber,
        birthDate: user.birthDate,
      });
    }
  }, [user]);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const updatedUser = await getUser();
        setUser(updatedUser);
      } catch (error) {
        setErrorMessage("Failed to fetch user data: " + error);
      }
    };
    fetchUser();
    fieldsInPolish.current = translateFieldsToPolish(Object.keys(formData));
  }, []);

  const handleButtonClick = async () => {
    if (editMode) {
      await updateUserProfile();
      setButtonText("Edytuj");
    } else {
      setButtonText("Zapisz");
    }
    setEditMode(!editMode);
  };

  const updateUserProfile = async () => {
    try {
      const updatedUser = await updateUser(formData);
      if (updatedUser) {
        setSuccessMessage("User profile updated successfully!");
        const timer = setTimeout(() => {
          setSuccessMessage("");
        }, 5000);
        return () => clearTimeout(timer);
      }
      setUser(updatedUser);
    } catch (error) {
      setErrorMessage("Failed to update user profile: " + error);
      const timer = setTimeout(() => {
        setErrorMessage("");
      }, 5000);
      return () => clearTimeout(timer);
    }
  };


  return (
    <Container>
      <Breadcrumb>
        <Breadcrumb.Item linkAs={Link} linkProps={{ to: "/" }}>
          Home
        </Breadcrumb.Item>
        <Breadcrumb.Item active>Profile</Breadcrumb.Item>
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
  );
};

export default UserProfile;