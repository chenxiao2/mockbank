## Swagger support
- This service is integrated with Swagger and has a built-in swagger ui http://credit-mockbank.com/swagger-ui/index.html.
- The swagger specification is published under http://credit-mockbank.com/swagger.json and http://credit-mockbank.com/swagger.yaml
- The avaliable interface could be explored by going to the swagger ui and exploring the swagger specification.

## Avaiable action
- http://credit-mockbank.com/xml/encrypt
  - POST: encrypt a XML document
- http://credit-mockbank.com/xml/decrypt
  - POST: decrypt an encrypted XML document
- http://credit-mockbank.com/gil/fr for GIL FR mockbank
  - POST: XML request with Content-Type application/xml or text/xml
- http://credit-mockbank.com/gil/fr/notify to orchestrate a LoanStatusNotification/CreditLineNotification call
  - POST: LoanStatusNotificationRequest
  - X-Mockbank-NotificationEndpoint header to set the client URL, for example http://localhost:8080/client/gil/fr
  - X-Mockbank-Async header to set if making the client call asynchronously
  - http://credit-mockbank.com/client/gil/fr is the endpoint mocking the client of MockBank for notification call.
- http://credit-mockbank.com/cbcc/uk for UK CBCC mockbank
  - GET: the redirecting URL
- http://credit-mockbank.com/cbcc/uk/notify for orchestract a ApplicationStateChange call
  - POST: The usage is similar to that of GIL FR



## to build
  mvn install
## to start the service
  mvn jetty:run
  
or

  mvn jetty:run-war

## to run as a docker container
  - build the image `docker build -t <namespace>/<imagename> .`
  - check the image `docker images`
  - run the docker container using the built image `docker run -p 18080:8080 -d <namespace>/<imagename>`
  - then the server will download runtime dependencies and start,
    wait for about one minutes and use `docker logs <container>` to check the status of the server.

## JAX-RS 2.0
  - This service is written in JAX-RS 2.0

## Guice integration
  - This service is using Guice for DI and integrated Guice with JAX-RS 2.0.

## to generate a RSA key pair used for encryption and decryption
```bash
$ openssl genrsa -out rsa.key 2048 # generate a RSA 2048 bits key pair
$ openssl rsa -text -in rsa.key # display the generated RSA key pair
$ openssl pkcs8 -topk8 -nocrypt -in rsa.key -out rsa1.pk8 # extract the private key in PKCS8 format
$ openssl rsa -text -in rsa.pk8 # display the generated RSA private key
$ openssl rsa -pubout -in rsa.key -out rsa.x509 # extract the public key
```

## set up a keystore and truststore for SSL server and client respectively
```
$ keytool -genkeypair -keystore keystore -alias liang -keyalg RSA -sigalg SHA256WITHRSA -keysize 2048 #step 1: generate a keypair into keystore
$ keytool -list -keystore keystore -v
$ keytool -exportcert -rfc -keystore keystore -alias liang -file liang.cer #step 2: export the certificate from keystore
$ cat liang.cer
$ openssl x509 -in liang.cer -text
$ keytool -importcert -alias liang -keystore truststore -file liang.cer #step 3: import the certificate into truststore
$ keytool -list -keystore truststore -v

$ java -Djavax.net.ssl.trustStore=truststore SSLSimpleClient
$ java -Djavax.net.ssl.keyStore=keystore -Djavax.net.ssl.keyStorePassword=password SSLSimpleServer
```
or open a web browser and visit https://localhost:9090/


## TODO
1. [ ] GIL FR mockbank implementation
  - [x] implement LoanApplicationCreation
  - [x] implement LoanStatusNotification
  - [x] implement LoanConfirmation
  - [x] implement LoanAdjustment
    - [x] For PurchaseClose
    - [x] For Refund
  - [x] implement LoanCharge
  - [ ] implement CreditLineNotification
  - [x] manage state of an application and a loan
2. [x] UK CBCC mockbank implementation
  - [x] implement mocking client
  - [x] implement ApplicationStateChange
  - [x] implement redirecting URL
3. [x] Load Asymmetric keys rather than generating them on start-up.
  - [x] Load from files generated by openssl or keytool
  - [x] Load from a keystore
4. [ ] Enable accepting encrypted mockbank request
5. [x] Enable HTTPS
  - [ ] Enable mutual handshake
6. [ ] migrate to Scalatra
7. [ ] Improve deployment and maintainance
  - [x] Proxied by CloudFront
  - [x] deploy to ECS
  - [ ] alternatively deploy to Elastic Beanstalk
  - [ ] auto test by Travis
8. [ ] Error handling
9. [ ] Message validation
10. [x] Integration testing
11. [x] Logging
  - [x] implements ContainerRequestFilter/ContainerResponseFilter to log method, headers, and status code
  - [x] implements ReaderInteceptor/WriterInterceptor to log http body.


  
