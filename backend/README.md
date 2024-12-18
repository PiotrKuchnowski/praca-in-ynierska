
# Backend

To jest backend aplikacji, zbudowany z wykorzystaniem Java Spring Boot. Oferuje zestaw usług backendowych dla frontendu aplikacji.

## Spis Treści

- [Struktura Projektu](#struktura-projektu)
- [Uruchomienie Aplikacji](#uruchomienie-aplikacji)
  - [Lokalne Uruchomienie](#lokalne-uruchomienie)
  - [Uruchomienie za Pomocą Dockera](#uruchomienie-za-pomocą-dockera)
- [Zmienne Środowiskowe](#zmienne-środowiskowe)
- [Główne Funkcje](#główne-funkcje)

## Struktura Projektu

- `src/main/java` - kod źródłowy aplikacji
- `src/main/resources` - zasoby aplikacji (np. pliki konfiguracyjne)
- `src/test/java` - testy jednostkowe i integracyjne
- `build.gradle` - plik konfiguracyjny Gradle
- `Dockerfile` - plik konfiguracyjny Docker

## Uruchomienie Aplikacji

### Lokalne Uruchomienie

1. Upewnij się, że masz zainstalowaną JDK 17 oraz Gradle.
2. Sklonuj repozytorium i przejdź do folderu backend:
   ```sh
   git clone https://github.com/PiotrKuchnowski/praca-inzynierska.git
   cd backend
   ```
3. Uruchom aplikację za pomocą Gradle:
   ```sh
   ./gradlew bootRun
   ```
4. Aplikacja będzie dostępna pod adresem `http://localhost:8080`.


## Zmienne Środowiskowe

W projekcie wykorzystywane są następujące zmienne środowiskowe:
- `DATABASE_URL` - adres URL bazy danych (np. `jdbc:postgresql://<adres_bazy>:5432/nazwa_bazy`)
- `DATABASE_USERNAME` - nazwa użytkownika bazy danych
- `DATABASE_PASSWORD` - hasło użytkownika bazy danych
- `MAIL_HOST` - adres serwera pocztowego
- `MAIL_PORT` - port serwera pocztowego
- `MAIL_USERNAME` - adres email używany do wysyłania wiadomości
- `MAIL_PASSWORD` - hasło do konta email
- `JWT_SECRET` - sekret do generowania tokenów JWT
- `CORS_ALLOWED_ORIGIN` - adres frontendowej aplikacji

Możesz ustawić te zmienne w plikach `application.properties` lub przekazać je bezpośrednio jako zmienne środowiskowe w systemie.

## Główne Funkcje

- **Obsługa JWT** - uwierzytelnianie użytkowników za pomocą tokenów JWT.
- **Zarządzanie Ofertami Pracy** - tworzenie, aktualizowanie i usuwanie ofert pracy.
- **Obsługa Użytkowników** - rejestracja, logowanie i weryfikacja konta.
- **Wysyłanie Wiadomości Email** - obsługa powiadomień emailowych.
- **Integracja z Bazą Danych** - operacje CRUD na danych za pomocą JPA.

Aby uzyskać więcej informacji o funkcjach aplikacji, zajrzyj do głównego pliku README w katalogu głównym projektu.
