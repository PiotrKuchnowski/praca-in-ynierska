export const translateFieldsToPolish = (fields: string[]): { [key: string]: string } => {
    const translations: { [key: string]: string } = {
      firstName: "Imię",
      lastName: "Nazwisko",
      email: "Email",
      phoneNumber: "Numer telefonu",
      birthDate: "Data urodzenia",
    };
    
    const translatedFields: { [key: string]: string } = {};
    fields.forEach(field => {
      translatedFields[field] = translations[field] || field;
    });
    return translatedFields;
  };