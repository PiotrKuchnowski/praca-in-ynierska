package pl.edu.pwr.pkuchnowski.doryw.enumeration.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.edu.pwr.pkuchnowski.doryw.enumeration.Authority;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if(authority == null) {
            return null;
        }
        return authority.getValue();
    }

    @Override
    public Authority convertToEntityAttribute(String dbData) {
        if(dbData == null) {
            return null;
        }
        return Authority.fromDbData(dbData);
    }
}
