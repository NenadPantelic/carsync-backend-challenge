# Carsync backend challenge - Login360 app

This is a repo for the challenge for the backend dev position at [CarSync](https://carsync.io/).

## 1. Specification:
1. **Sign-up request:** Given the user email address, send to this email a secret code for
verification. Note that Login360 does not allow users to register unless they verify their
email.
2. **Verify user request:** Given the user email address, the secret that was sent and the
password, create a user account and return a token. This token is used to authenticate
the user for requests that require authentication.
3. **Login request:** Given email address and password, return the token if successful,
otherwise return error.
4. **Change password:** Given the previous and new password, change the user’s password.
To be able to do this, the user must be authenticated.
5. **Two-factor authentication.** Support two-factor authentication through SMS.
	- Users can add two-factor authentication through SMS to their account. Design
and implement the necessary requests. Make sure to verify the phone number
during this workflow.
	- Expand login functionality to support two-factor authentication. Design and
implement the necessary requests.

## 2. Endpoints

1. `/api/v1/signup` - user can create register request by providing email.  Email is sent to that email address with secret verification code.
```json
{
	"email": "<someemail>@<somedomain>.com"
}
```
2. `/api/v1/signup/verify-account` - user provides:
```json
{
	"email": "<someemail>@<somedomain>.com",
	"password": "password",
	"verificationToken": "xxxxx-yyyyy-zzzz-qqqq"
}

```
If the data is valid, new user account is created and the auth token is returned.

3. `/api/v1/login` - the user makes login request with:
```json
{
	"email": "<someemail>@<somedomain>.com",
	"password": "password",
}

```
If the 2FA is not enabled, response looks similar to this:
```json
{
  "token": "token",
  "proceedWith2FA": false
}
```

Otherwise, it looks like this:
```json
{
  "token": null,
  "proceedWith2FA": true
}

```
For more about the next step, look at endpoint #7 - `/api/v1/login/two-fa`

4. `/api/v1/my-profile/settings/two-fa` - the user (logged-in) creates 2FA enable request:
```json
{
	"phoneNo": "+380123456789"
}
```
If the phone number is valid, it receives the verification code via SMS. In that way, the phone number is verified.

5. `/api/v1/my-profile/settings/two-fa/enable` - after step 4., the user provides the verification token from his SMS:
```json
{
	"verificationToken": "6-digit code"
}
```
If the token is valid, 2FA is enabled.

6. `/api/v1/my-profile/settings/two-fa/disable` - to disable 2FA (must be logged-in)
7. `/api/v1/login/two-fa` - if 2FA is enabled, after #3, the user receives secret code via SMS. To verify 2FA step, client sends:
```json
{
	"twoFAToken": "6-digit code"
}
```
8. `/api/v1/my-profile/settings` - change password. The request payload looks like this:
```json
{
	"oldPassword": "old pass",
	"newPassword": "new pass"	
}

```
For more details about endpoints, check Swagger docs at `/swagger-ui/`.


## 3. Requirements
Login360 is implemented with:
- Java (JDK 11) - ![](https://img.shields.io/badge/Code-Java-informational?style=flat&logo=java&logoColor=white&color=2bbc8a)
- Spring Boot (2.5.1) - ![](https://img.shields.io/badge/Framework-Spring-informational?style=flat&logo=spring&logoColor=white&color=2bbc8a)
- Maven (Apache Maven 3.6.0)

In order to setup the project locally, install the prerequisites - JDK (probably 8+ will do the job ) and Maven.  

## 4. Setup
The first step is cloning a project:
```
git clone https://github.com/NenadPantelic/carsync-backend-challenge.git
```
In order to use it locally, you can:
1. **Run it from your IDE (Eclipse, IntelliJ, NetBeans, VSCode with plugins...)**
- Import it as Maven project
- Run it as Java app
2. **Build it and run  JAR executable**
- Perform (from project root dir where `pom.xml` is located)
```bash
mvn clean install
```
You will get snapshot JAR file in your `target` dir. After that, run JAR file regularly with:
```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```


3.  **Run it with Maven in exploded form**
```bash
mvn spring-boot:run
```
Maven will download all the necessary dependencies and API will be listening to port 9600 on embedded Tomcat servlet container (you can change the  port in `application.properties` file).

**NOTE:** You'll have to provide third-party API auth credentials in `application.properties`. Concretely:
```
# Email settings
mail.api-endpoint=<PUT YOUR ENDPOINT HERE>
mail.api-key=<PUT YOUR API KEY HERE>
mail.sender=<PUT YOUR SENDER HERE>
mail.subject=Signup verification secret code
mail.template-file-path=templates/signup_verification_email_template.html

# SMS Twilio settings
twilio.account-sid=<PUT YOUR SID HERE>
twilio.auth-token=<PUT YOUR AUTH TOKEN HERE>
twilio.sender-phone-no=<PUT YOUR PHONE NO HERE>
sms.template-file-path=templates/sms_template.txt
```
Replace placeholders with your values.

**NOTE:** The app uses H2 in-memory database which means data will be blown up when the app is shutdown or reset. You can change datasource configuration easily with:
```
...
spring.datasource.url=<NEW JDBC URL>
spring.datasource.driverClassName=<DRIVER NAME>
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASS>
spring.jpa.database-platform=<DB DIALECT>
...
```
## 5. Task board
[Trello board](https://trello.com/b/7Ty3jt9Z/carsync-challenge)  was used for the task management.