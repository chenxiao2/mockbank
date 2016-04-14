## Avaiable action
- http://address:8080/services/encrypt
  - POST: encrypt a XML document
- http://address:8080/services/decrypt
  - POST: decrypt an encrypted XML document
- http://address:8080/services for mockbank
  - POST: XML request with Content-Type application/xml or text/xml


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

## TODO
1. [ ] mockbank implementation
  - [x] implement LoanApplicationCreation
  - [ ] implement LoanStatusNotification
  - [ ] implement LoanConfirmation
  - [ ] implement LoanAdjustment
    - [ ] For PurchaseClose
    - [ ] For Refund
  - [ ] implement LoanCharge
  - [ ] implement CreditLineNotification
2. [ ] Load Asymmetric keys rather than generating them on start-up.
3. [ ] Enable accepting encrypted mockbank request
4. [ ] Enable HTTPS
  - [ ] Enable mutual handshake
5. [ ] migrate to Scalatra
6. [ ] Improve deployment and maintainance
  - [ ] Proxied by CloudFront
  - [ ] move to ECS and/or Elastic Beanstalk
  
