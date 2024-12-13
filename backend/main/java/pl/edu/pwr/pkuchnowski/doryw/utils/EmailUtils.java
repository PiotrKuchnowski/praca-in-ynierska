package pl.edu.pwr.pkuchnowski.doryw.utils;

public class EmailUtils {
    public static String getNewAccountEmailMessage(String name, String token, String frontendURL){
        return "Cześć " + name + ",\n\n" +
                "Dziękujemy za rejestrację w Doryw. Aby aktywować swoje konto, kliknij w poniższy link:\n" +
                frontendURL + "?token=" + token + "\n\n" +
                "Jeśli nie rejestrowałeś się na naszej stronie, zignoruj tę wiadomość.\n\n" +
                "Pozdrawiamy,\n" +
                "Zespół Doryw";
    }

    public static String getResetPasswordMessage(String name, String token, String frontendURL){
        return "Cześć " + name + ",\n\n" +
                "Otrzymaliśmy prośbę o zresetowanie hasła do Twojego konta w Doryw. Aby to zrobić, kliknij w poniższy link:\n" +
                frontendURL + "?token=" + token + "\n\n" +
                "Jeśli nie prosiłeś o zresetowanie hasła, zignoruj tę wiadomość.\n\n" +
                "Pozdrawiamy,\n" +
                "Zespół Doryw";
    }

    public static String getNewEmployerAccountEmailMessage(String name, String token, String frontendURL){
        return "Cześć " + name + ",\n\n" +
                "Cieszymy się, że zdecydowałeś się na dołączenie do pracodawców korzystających z Doryw. Prosimy o potwierdzenie założenia konta pracodawcy klikając w poniższy link:\n" +
                frontendURL + "?token=" + token + "\n\n" +
                "Jeśli nie zakładałeś konta pracodawcy na naszej stronie, zignoruj tę wiadomość.\n\n" +
                "Pozdrawiamy,\n" +
                "Zespół Doryw";
    }
}
