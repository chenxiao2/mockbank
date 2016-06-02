package app.util;

import com.amazon.payments.cobrandcreditcard.protocol.v1.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.CreditCardValidator;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

/**
 * Created by liazhang on 6/1/16.
 */
@Slf4j
public class UKCBCCUtil {
    public static void fillCommonResponse(ApplicationStateChangeRequest request, ApplicationStateChangeResponse response) throws DatatypeConfigurationException {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        response.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(utc)));
        response.setTransactionId(request.getTransactionId());
        response.setRequestId(request.getRequestId());
        response.setRecipientId(request.getSourceId());
        response.setSourceId(request.getRecipientId());
    }

    public static ApplicationStateChangeRequest createRequest(String ari) {
        ApplicationStateChangeRequest request = new ApplicationStateChangeRequest();
        //common elements
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        DatatypeFactory datatypeFactory = null;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            log.error("failed to get an instance of DatatypeFactory", e);
        }

        request.setTimestamp(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(utc)));
        request.setTransactionId("Transaction_ABCD");
        request.setRequestId("ANBKLSDLHKE");
        request.setSourceId("credit-mockbank.com");
        request.setRecipientId("AMAZON");
        request.setAmazonReferenceId(ari);
        //TODO: make the application status based on ari
        request.setApplicationStatus(ApplicationStatus.APPROVED_BUY);
        CreditCardDetails creditCardDetails = new CreditCardDetails();
        request.setCreditCardDetails(creditCardDetails);
        //TODO: generate the credit card number and encrypt it.
        creditCardDetails.setEncryptedCreditCardNumber("IAmTheEncryptedCreditCardNumber");
        creditCardDetails.setCreditCardType(CardType.CLASSIC);
        creditCardDetails.setExpirationDate(datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(utc.plusYears(5))));
        creditCardDetails.setCardHolderName("Sherlock Holmes");
        Address address = new Address();
        creditCardDetails.setBillingAddress(address);
        address.setAddressLine1("221B Baker Street");
        address.setCityOrTown("London");
        address.setPostcode("NW1 6XE");
        address.setCountryCode("UK");
        address.setPhoneNumber("+44 20 7224 3688");
        creditCardDetails.setEmailAddress("Sherlock.Holmes@co.uk");
        creditCardDetails.setPartnerReferenceId("FDJSLKFJLKDSJFKDSJ");

        return request;
    }
}
