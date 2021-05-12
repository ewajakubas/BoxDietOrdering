# BoxDietOrdering
# System informatyczny wspomagający zamawianie diety pudełkowej.



Celem projektu było stworzenie wielodostępnego systemu informatycznego, umożliwiającego obsługę zamówień diety pudełkowej czyli cateringu dietetycznego z dostawą. System pozwala zarejestrowanym klientom zamawiać wybrany przez siebie rodzaj diety na wybrany przez siebie okresu czasu. 

Budowany system informatyczny złożony jest ze stanowej aplikacji internetowej i relacyjnej bazy danych. Aplikacja jest uruchomiona w serwerze aplikacyjnym Payara, a baza danych jest osadzona w systemie zarządzania bazami danych JavaDB.

Stworzony system zapewnia trzy poziomy dostępu i funkcje użytkownika nieuwierzytelnionego. Użytkownik nieuwierzytelniony, czyli Gość, może przeglądać ofertę diet, zalogować się, utworzyć nowe konto lub zresetować hasło. Administrator posiada możliwość zarządzania kontami wszystkich użytkowników, m.in. ich tworzenia (pracownik, administrator), edytowania oraz aktywowania i dezaktywowania. Pracownik odpowiedzialny jest za obsługę zamówień diet, może przeglądać ich listy, zatwierdzać zamówienia opłacone oraz usuwać zamówienia nieopłacone. Pracownik zarządza też dietami, może je dodawać, usuwać oraz je edytować. Ma też dostęp do listy klientów. Klient ma możliwość utworzenia zamówienia na dietę poprzez wybór rodzaju diety, liczby dni obowiązującej diety oraz wyboru pierwszego dnia od kiedy dieta ma obowiązywać czyli pierwszego dnia dostawy cateringu. Ma dostęp do listy swoich zamówień opłaconych i nieopłaconych, może usuwać swoje nieopłacone zamówienia. Dodatkowo system usuwa nieopłacone zamówienia klienta po upływie terminu 3 dni od złożenia zamówienia, dzięki zastosowaniu metody wywoływanej cyklicznie.

# Wersje zastosowanych technologii i narzędzi
Stos technologiczny użyty przy realizacji projektu:
* język programowania Java 11;
* ziarna CDI (ang. Context and Dependency Injection) w wersji 1.2;
* Kontener EJB (ang. Enerprise JavaBeans) 3.2;
* JSF (ang. Java Server Faces ) w wersji 2.2;
* PrimeFaces w wersji 7.0;
* JTA (ang. Java Transaction API ) 1.2;
* JPA (ang. Java Persistence API ) 2.5.2;

Środowisko developerskie i produkcyjne:
* NetBeansIDE w wersji 11;
* Maven w wersji 3.1;
* platformaprogramistycznaJEE(ang.JavaEnterpriseEdition) w wersji8.0;
* serwera plikacyjny Payara w wersji5.183;
* JavaDB(ApacheDerby)odpowiedzialna za składowanie danych ww ersji 10.13.1.1;
* Modelio Open Source w wersji 4.0.


Do zdefiniowania interfejsu użytkownika zastosowano technologię JavaServer Faces wykorzystując komponenty biblioteki PrimeFaces.
Dzięki zastosowanemu mechanizmowi internacjonalizacji, możliwe jest uruchomianie aplikacji w języku angielskim bądź polskim.
W aplikacji zaimplementowano obsługę błędów. W celu zapobieganiu uszkodzenia danych, każdy zgłoszony wyjątek aplikacyjny wymusza wycofanie transakcji aplikacyjnej, a jego obsługa odbywa się poprzez propagowanie go do warstwy widoku i poinformowaniu użytkownika o wystąpieniu błędu. 
W aplikacji zastosowano strategię zarządzania transakcjami aplikacyjnymi przez kontener EJB, tzw. strategię CMT. Do serwera aplikacji została delegowana obsługa uprawnień oraz kontroli dostępu. Dostęp do biznesowych metod w punktach dostępowych oraz fasadach został przyznany określonym poziomom dostępu za pomocą adnotacji. W aplikacji zaimplementowano mechanizm rejestrujący podejmowane przez użytkowników czynności biznesowe w dziennikach zdarzeń. W dziennikach tych utrwalane są też informacje o granicach transakcji bazodanowych wraz ze statusem jej zakończenia. 
W zbudowanym systemie informatycznym w celu umożliwienia jednoczesnej pracy wielu użytkowników wykorzystano mechanizm blokad optymistycznych.

# Screeny
Lista zamówień (widok dla pracownika)
![ScreenShot](https://github.com/ewajakubas/BoxDietOrdering/blob/master/images/lista%20zamowien%20(pracownik).png)

Lista zamówień (widok dla klienta)
![ScreenShot](https://github.com/ewajakubas/BoxDietOrdering/blob/master/images/lista%20zamowien%20(klient).png)

Wprowadzanie nowej diety
![ScreenShot](https://github.com/ewajakubas/BoxDietOrdering/blob/master/images/dodaj%20nową%20dietę.png)



# Instrukcja wdrożenia
Do uruchomienia stworzonego systemu niezbędne jest przygotowanie środowiska, które posiada działający system operacyjny oraz współczesną przeglądarkę internetową. Do tego należy posiadać zainstalowane: serwer aplikacyjny Payara oraz system Java DB (Apache 55 Derby) w wersji 10.13.1.1. Następnie należy wykonać następujące czynności:
*Utworzenie bazy danych z użyciem programu narzędziowego ij dostarczonego wraz z Apache Derby. Konfiguracja bazy danych zdefiniowana została w pliku konfiguracyjnym glassfish-resources.hml:

<property name="databaseName" value="BoxDiet"/> 
<property name="User" value="diet"/> 
<property name="Password" value="diet"/>
W wierszu poleceń należy uruchomić server bazy danych. Następnie w drugim oknie uruchomić program narzędziowy ij i utworzyć bazę danych.
java -jar $DERBY_HOME/lib/derbyrun.jar server start
java –jar $DERBY_HOME/lib/derbyrun.jar ij
ij>CONNECT ‘jdbc:derby://localhost:1527/BoxDiet;create=true;user=diet;password=diet; ij>exit;

*Konfiguracja obszaru bezpieczeństwa na serwerze. Należy otworzyć przeglądarkę internetową i w pasek adresu wpisać adres URL lokalnego serwera http://localhost:4848/common/index.jsf. Z menu po lewej stronie należy wybrać klikając kolejno: Configurations → server-config → Security → Realms. Następnie należy wybrać przycisk New i uzupełnić formularz podanymi wartościami.
Realm Name: DietOrderingJDBCRealm
Class Name: com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm
JAAS Context: jdbcRealm
JNDI: jdbc/BoxDietDataSource
User Table: DATALOGIN
User Name Column: LOGIN
Password Column: PASSWORD
Group Table: DATALOGIN
Group Name Column: TYPE
 
* Kolejnym krokiem jest wdrożenie i uruchomienie aplikacji. W tym celu należy uruchomić server aplikacyjny wpisując w wierszu poleceń w katalogu {PAYARA_INSTALL_DIR}/bin komendę:
bin>asadmin start-domain
Następnie należy wdrożyć aplikację poleceniem:
bin>asadmin deploy nazwa_katalogu/BoxDieOrdering/target/BoxDietOrdering-1.0.war

4. Aby użytkownik mógł korzystać z funkcjonalności systemu wymagane jest zainicjowanie wstępnych danych m.in. kont użytkowników. W tym celu należy wczytać plik initDB.sql z danymi do uwierzytelnienia
ij>run ‘nazwa_katalogu/BoxDietOrdering/src/main/resources/db/init/initDB.sql’;

Wdrożona aplikacja jest dostępna pod adresem http://localhost:8080/BoxDietOrdering/. Jeśli wszystkie powyższe kroki zostały wykonane poprawnie możliwe będzie zalogowanie się do aplikacji na jedno z predefiniowanych kont, znajdujących się w pliku z danymi inicjującymi. Loginy oraz hasła niezbędne do uwierzytelnienia:
 
Typ konta:login:hasło
Administrator:admin1:Administrator1! 
Pracownik:pracownik1:Pracownik1! 
Klient:klient1:Klient1! 
