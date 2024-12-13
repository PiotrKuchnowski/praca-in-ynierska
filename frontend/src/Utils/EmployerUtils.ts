export const translateFieldsToPolish = (fields: string[]): { [key: string]: string } => {
    const translations: { [key: string]: string } = {
        companyName: "Nazwa firmy",
        nip: "NIP",
        description: "Opis działalności",
    };

    const translatedFields: { [key: string]: string } = {};
    fields.forEach(field => {
        translatedFields[field] = translations[field] || field;
    });
    return translatedFields;
};