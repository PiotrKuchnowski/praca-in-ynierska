package pl.edu.pwr.pkuchnowski.doryw.utils;

import pl.edu.pwr.pkuchnowski.doryw.dtoresponse.PayTypeResponse;
import pl.edu.pwr.pkuchnowski.doryw.entities.PayTypeEntity;

public class PayTypeUtils {

    public static PayTypeResponse fromPayTypeEntity(PayTypeEntity payTypeEntity) {
        PayTypeResponse payType = new PayTypeResponse();
        payType.setPayTypeID(payTypeEntity.getId());
        payType.setName(payTypeEntity.getName());
        return payType;
    }
}
