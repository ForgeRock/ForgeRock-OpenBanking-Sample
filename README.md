<!--
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2018 ForgeRock AS.
-->

# Open Banking Reference Implementation Sample

Code sample for helping TPPs and ASPSP using the ForgeRock Open Banking reference implementation platform.

## Licence

License: CDDLv1.0 \
License URL: http://forgerock.org/cddlv1-0/


## How to install the app


### Downloading the project code and loading it with intellij

You need to get the code locally for the git project repo. For this, you need to

- Fork the project [https://github.com/darkedges/ForgeRock-OpenBanking-Sample](https://github.com/darkedges/ForgeRock-OpenBanking-Sample)
- Clone your fork (if it's your first development with GIT, we recommend putting[] all the git repo in ~/Development/GIT)
- Download the latest intellij version (ultimate)
- Open the project with intellij (you will need to open it as an existing maven project)
- Install mongod if you don't have it already. There is plenty of doc online for this. We recommend using brew
  __**note:**__ You can use the embedded version of mongodb by commenting out the following line in [application.yml](src/main/resources/application.yml)
  ```
  #exclude: org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
  ```
- You will need later to run mongod in background. Either do it in console before working or setup an external tool 

In intellij so you can run it from intellij (tips: [https://stackoverflow.com/questions/33595827/is-there-a-way-of-running-mongod-from-webstorm-mac](https://stackoverflow.com/questions/33595827/is-there-a-way-of-running-mongod-from-webstorm-mac))

### Setup the host files

You will need to create some new hostnames for the application.

```$xslt
127.0.0.1 tpp.sample.ob.forgerock.financial redirect.tpp.sample.ob.forgerock.financial tpp.ob.forgerock.financial
```

### Register your TPP with the Open Banking directory

We implemented a script which will allow you to generate keys, ready to go, that you can import to Open Banking directory.
What you need to do:

- create a new software statement into Open Banking
  - add a dummy redirect uri, like https://google.fr
- you will need to copy the organisation ID and software ID into two files:
  - `generate-keys/Makefile`
  - `forgerock-openbanking-sample-tpp/src/main/resources/application.yml` (be careful, in some places, you need to lower case the software id)
- generate the SSA and copy it to the file `forgerock-openbanking-sample-tpp/src/main/resources/ob/ssa.jwt`

Now you are ready to generate some new keys:

- do `make all` and follow the instructions of the script
- Copy the signing kid value from Open Banking directory to the file `forgerock-openbanking-sample-tpp/src/main/resources/application.yml`

You should be ready to run the application now.

### Run the application

- Compile the project: `./mvnw clean install`
- run it using intellij or by doing `./mvnw spring-boot:run -pl forgerock-openbanking-sample-tpp`
- try to say hello to the app:

```$xslt
curl -X GET \
  https://tpp.ob.forgerock.financial:7777/open-banking/v1.1//helloworld/ 
```

you should have as a response:

```$xslt
Hello World!
```

### How to use the application ?

In the project, you will find a folder called `postman`. In there, you will find a postman collection and an environment that you can import in your postman application.

You will find the `hello world` request in it.

A suggested order of execution:

- "hello world" : for testing your application installation
- "register TPP to ASPSP": register your TPP
- "Get registered ASPSPs": you can check that the ASPSP you just registered with is in the list
- "initiate account request": you start the Account request OB flow
  - you get back an uri that you need to execute in your browser.
  - login and accept the consent
  - you are redirected to the redirect_uri with a code, state and id token.
- "exchange code": exchange the code you received to get an access token back
- call the request you want under the "Account API" folder.

## Edit the application for raising support tickets to ForgeRock

You can modify this project to match a behaviour you are doing with your own TPP. 
It's the recommended way if you want to raise an issue to the ForgeRock team:

- modify this project to reproduce the issue
- push a Pull request into the project
- use the Pull Request ID into your support ticket.
