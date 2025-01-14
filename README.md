
# Aplikacja Webowa do Publikacji Ogłoszeń o Pracy Dorywczej

## Opis projektu

Aplikacja webowa umożliwia użytkownikom:
- **Przeglądanie ofert pracy dorywczej**,
- **Aplikowanie na wybrane ogłoszenia**,
- **Dodawanie nowych propozycji pracy**.

Aplikacja została zaprojektowana z myślą o responsywności (**RWD**) oraz progresywności (**PWA**), zapewniając użytkownikom komfortowe korzystanie z niej zarówno na urządzeniach mobilnych, jak i stacjonarnych.

## Technologie

Projekt został zrealizowany z wykorzystaniem następujących narzędzi i technologii:
- **PostgreSQL** - baza danych,
- **Java Spring Boot** - warstwa serwerowa,
- **React** - interfejs użytkownika (frontend),
- **Docker Compose** - do łatwego uruchamiania wszystkich usług.

## Funkcje

Aplikacja pozwala użytkownikom:
- Rejestrować i logować się na swoje konta
- Publikować ogłoszenia o pracy dorywczej,
- Przeglądać dostępne oferty,
- Aplikować na interesujące ich ogłoszenia załączając wiadomości dla pracodawców,
- W ramach konta utworzyć jeden profil pracodawcy,
- Odpowiadać na ogłoszenia załączając opcjonalne wiadomości dla aplikantów.

## Uruchamianie aplikacji

Do uruchomienia aplikacji zalecany jest **Docker** oraz **Docker Compose**.

### Kroki uruchomienia

1. **Uruchomienie aplikacji**
   W katalogu zawierającym plik `docker-compose.yml` uruchom następujące polecenie:
   ```bash
   docker-compose up
   ```

2. Dostęp do aplikacji

Domyślne adresy:
- **Frontend:** [http://localhost:3000](http://localhost:3000)
- **Backend API:** [http://localhost:8085](http://localhost:8085)

Jeśli aplikacja jest uruchomiona na innym urządzeniu w sieci, zamień `localhost` na adres IP tego urządzenia. Przykładowo, jeśli IP to `192.168.1.100`:
- **Frontend:** [http://192.168.1.100:3000](http://192.168.1.100:3000)
- **Backend API:** [http://192.168.1.100:8085](http://192.168.1.100:8085)

> **Wskazówka:** Możesz korzystać z VPN, aby uzyskać dostęp do urządzenia hostującego aplikację w sieci prywatnej.

3. **Zatrzymanie aplikacji**
   Aby zatrzymać aplikację, użyj:
   ```bash
   docker-compose down
   ```

## Konfiguracja

W pliku `docker-compose.yml` znajdują się zmienne środowiskowe, które należy dostosować:
- **Backend**:
  - `DATABASE_URL`: Adres URL serwera bazy danych PostgreSQL.
  - `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`: Konfiguracja serwera mailowego.
  - `JWT_SECRET`: Klucz JWT dla generowania tokenów.
  - `CORS_ALLOWED_ORIGIN`: Adres frontendowej aplikacji.
- **Frontend**:
  - `REACT_APP_BASE_URL`: Adres backendu.

## Licencja

Kod aplikacji jest objęty licencją **Creative Commons Attribution-NonCommercial (CC BY-NC)**.  
Oznacza to, że kod źródłowy i jego modyfikacje mogą być używane wyłącznie w celach niekomercyjnych. Więcej szczegółów znajdziesz w [tekście licencji](LICENSE).

---

**Autor:** Piotr Kuchnowski 
**Wrocław, Polska**
