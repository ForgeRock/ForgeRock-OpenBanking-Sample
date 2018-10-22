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

Code sample for helping Third Party Providers (TPPs) and Account Servicing Payment Service Provider (ASPSP) using the ForgeRock Open Banking reference implementation platform.

## Licence
License:	CDDLv1.0 \
License URL	: http://forgerock.org/cddlv1-0/


## How to install the app


### Downloading the project code and loading it into an IDE such as IntelliJ

You need to get the code locally for the git project repo. For this, you need to

- Fork the project from the following URL: https://github.com/ForgeRock/ForgeRock-OpenBanking-Sample
- Clone your fork
- Open the project in your IDE
- Install MongoDB. If you're installing on a Mac, we recommend using `brew`
- Prepare to run MongoDB in the background. You can do so from either the console or with an external tool associated with your IDE.
(For one method: https://stackoverflow.com/questions/33595827/is-there-a-way-of-running-mongod-from-webstorm-mac)


### Set up host files

If you're running this sample locally, associate the following hostnames with the localhost IP address:

```$xslt
127.0.0.1		tpp.sample.ob.forgerock.financial redirect.tpp.sample.ob.forgerock.financial tpp.ob.forgerock.financial
```

### Register your TPP with the ForgeRock directory

The ForgeRock directory manages keys for you and exposes the jwkms, a service that manages generated JWTs.

To register your TPP:
- Create a new software statement in the ForgeRock directory.
- Download your transport key.
- Set up an API Development Environment to use your transport keys for the jwkms, directory and ASPSP. (We use Postman.)
- In the API Development Environment, run the following collection: ASPSP V1.1 > Examples > Example 1: Onboarding.

### Register your TPP with the Open Banking directory

We've included a script which will allow you to generate keys that you can import to Open Banking directory.
To proceed:
- create a new software statement into Open Banking
-- add a dummy redirect uri, like https://google.fr
- you will need to copy the organisation ID and software ID into two files:
-- `generate-keys/Makefile`
-- `forgerock-openbanking-sample-tpp/src/main/resources/application.yml` (be careful, in some places, you need to lower case the software id)
- generate the SSA and copy it to the file `forgerock-openbanking-sample-tpp/src/main/resources/ob/ssa.jwt`
Now you are ready to generate some new keys:
- do `make all` and follow the instructions of the script
- Copy the signing kid value from Open Banking directory to the file `forgerock-openbanking-sample-tpp/src/main/resources/application.yml`

You should be ready to run the application now.

### Run the application

- Compile the project with the following command: `mvn clean install`
- Run the project in one of the following two ways:
  - Using your IDE
  - Running the following command: `./forgerock-openbanking-sample-tpp/target/forgerock-openbanking-sample-tpp-1.1.1-SNAPSHOT.jar`
- Use the following REST call to contact the app:

```$xslt
curl -X GET \
  https://tpp.ob.forgerock.financial:7777/open-banking/v1.1//helloworld/ 
```

If successful, you'll see the following response::

```$xslt
Hello World!
```

### Bring the Application to Life

In the project, you will find a `postman` folder which includes a collection of API commands and an environment that you can import into your API Development Environment (Postman).

You'll see the `hello world` request in the collection, in the TPP Sample.

We recommend that you run the collection in the following order:
- Test your application with "Hello World Test" 
- Register your TPP. Under TPP Sample/onboarding run "Register TPP to ASPSP"
- Under TPP Sample/onboarding, run "Get registered ASPSPs"; this verifies that the ASPSP you just registered with is in the list
- Under TPP Sample/Account Request, run "initiate account request", which starts the Account request OB flow
  - This returns an URI that you can use in your browser.
  - Log into the URL and accept the consent form.
  - You're redirected to the redirect_uri with a code, state and id token.
- Under TPP Sample/Account Request, run "exchange code". This exchanges the code you received for an access token
- Run other requests as desired under the ASPSP/Account API V2.0 folder.

## Edit the application for raising support tickets to ForgeRock

To raise an issue with ForgeRock, we recommend that you modify this project to match the behaviour of your own TPP, as follows:
- Modify this project to reproduce the issue.
- Submit a Pull request.
- Include the Pull Request ID in your support ticket.
