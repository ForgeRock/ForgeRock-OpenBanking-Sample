#
# The contents of this file are subject to the terms of the Common Development and
#  Distribution License (the License). You may not use this file except in compliance with the
#  License.
#
#  You can obtain a copy of the License at https://forgerock.org/license/CDDLv1.0.html. See the License for the
#  specific language governing permission and limitations under the License.
#
#  When distributing Covered Software, include this CDDL Header Notice in each file and include
#  the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
#  Header, with the fields enclosed by brackets [] replaced by your own identifying
#  information: "Portions copyright [year] [name of copyright owner]".
#
#  Copyright 2018 ForgeRock AS.
#
#

# !!!! REPLACE THOSE IMPORMATION BY YOURS !!!
TPP_ORGANISATION_ID=0015800001041REAAY
TPP_SOFTWARE_ID=7BtarWcyGjdHwH9285FojD


#Default keys parameters

TRANSPORT_KEY_ALGO=RSA
TRANSPORT_KEY_SIZE=2048
TRANSPORT_KEY_SIGNING_ALGO=SHA256withRSA

SIGNING_KEY_ALGO=RSA
SIGNING_KEY_SIZE=2048
SIGNING_KEY_SIGNING_ALGO=SHA256withRSA

#OB directory
OB_ORGANISATION_ID=OpenBanking
OB_ROOT_CA_PATH=obOfficialCertificates/OB_SandBox_PP_Root_CA.cer
OB_ROOT_CA_ALIAS=obsandboxrootca

OB_ROOT_MIT_CA_PATH=obOfficialCertificates/obtestrootca.cer
OB_ROOT_MIT_CA_ALIAS=obmitrootca

#
# Your TPP settings
#
HOSTNAME_TPP=tpp.sample.ob.forgerock.financial
KEYSTORE_TPP="${FOLDER_TPP}/keystore.p12"
TRUSTORE_TPP="${FOLDER_TPP}/truststore.p12"
FOLDER_TPP=tpp
PASSWORD=changeit

#
# Your certificate settings
#
FORGEROCK_OU=forgerock.financial
FORGEROCK_O=ForgeRock
FORGEROCK_L=Bristol
FORGEROCK_ST=Avon
FORGEROCK_C=GB

VALIDITY=3650

# OB requirements
TPP_OU=$(TPP_ORGANISATION_ID)
TPP_CN=$(TPP_SOFTWARE_ID)
TPP_O=$(OB_ORGANISATION_ID)
TPP_C=GB

#We generate a self-signed CA to generate keys from it
DNAME_CA='CN=openbanking CA,OU=$(FORGEROCK_OU),O=$(FORGEROCK_O),L=$(FORGEROCK_L),ST=$(FORGEROCK_ST),C=$(FORGEROCK_C)'
#It's a bit unusual but the CN is not the hostname for OB. They want it to be the software ID and the OU the organisation ID
DNAME_TPP_OB='CN=$(TPP_CN),OU=$(TPP_OU),O=$(TPP_O),C=$(FORGEROCK_C)'

#Self signed certificate
SELF_SIGNED_CA_ALIAS=selfSignedCA

#deploy destination
TPP_SAMPLE_KEYSTORE_FOLDER=../forgerock-openbanking-sample-tpp/src/main/resources/keystore

#Makefile commands:
all: clean step1

step1:	clean \
 init-folder \
 create-keystores \
 create-truststore \
 create-csr \
 instructions-after-step-1 \

step2: import-pem \
deploy \
instructions-after-step-2 \

init-folder: init-folder-tpp

init-folder-tpp:
	# Generate tpp folder
	mkdir ${FOLDER_TPP}

#
# Create keystores
#
create-keystores: create-tpp-keystore \

create-tpp-keystore:
	@echo "\nGenerate TPP keystore\n"
	keytool -genkey -alias $(SELF_SIGNED_CA_ALIAS) -ext BC=ca:true \
	    -keyalg $(TRANSPORT_KEY_ALGO) -keysize $(TRANSPORT_KEY_SIZE) -sigalg $(TRANSPORT_KEY_SIGNING_ALGO) \
	    -keypass $(PASSWORD) \
	    -validity $(VALIDITY) -dname $(DNAME_CA) \
	    -keystore $(KEYSTORE_TPP) -deststoretype pkcs12 -storepass $(PASSWORD)

	@echo "\nExport the self signed CA in case you need it later\n"
	keytool -export -alias $(SELF_SIGNED_CA_ALIAS) -file $(FOLDER_TPP)/$(SELF_SIGNED_CA_ALIAS).crt -rfc \
		-keystore $(KEYSTORE_TPP) -storepass $(PASSWORD)

create-truststore: create-tpp-truststore

create-tpp-truststore:
	# Import OB certificates into truststore
	keytool -import -trustcacerts -noprompt -alias $(OB_ROOT_CA_ALIAS) -file $(OB_ROOT_CA_PATH) \
	    -keystore $(TRUSTORE_TPP) -storepass $(PASSWORD) -deststoretype pkcs12

	# Import MIT OB certificate into truststore
	keytool -noprompt -import -alias $(OB_ROOT_MIT_CA_ALIAS) -file $(OB_ROOT_MIT_CA_PATH) -keystore $(TRUSTORE_TPP) -storepass changeit
#Function to generate a key and export it in all the format you may need.
define generate_csr
	$(eval ALIAS=$(1))
	$(eval DNAME=$(2))
	$(eval KEYSTORE=$(3))
	$(eval PASSWORD=$(4))
	$(eval FOLDER=$(5))
	$(eval KEY_SIZE=$(6))
	$(eval KEY_SIGNING_ALGO=$(7))
	$(eval KEY_ALGO=$(8))

	@echo "\nGenerate transport $(ALIAS) certificate\n"
	keytool -genkey -alias $(ALIAS) \
		-keyalg $(KEY_ALGO) -keysize $(KEY_SIZE) -sigalg $(KEY_SIGNING_ALGO) \
		-keypass $(PASSWORD) \
		-validity $(VALIDITY) -dname $(DNAME) \
		-keystore $(KEYSTORE) -storepass $(PASSWORD)

	@echo "\nGenerate $(ALIAS) certificate signing request\n"
	keytool -certreq -alias $(ALIAS) -ext BC=ca:true \
		-keyalg $(KEY_ALGO) -keysize $(KEY_SIZE) -sigalg $(KEY_SIGNING_ALGO) \
		-validity $(VALIDITY) -file "$(FOLDER)/$(ALIAS).csr" \
		-keystore $(KEYSTORE) -storepass $(PASSWORD)
endef

define import_csr
	$(eval ALIAS=$(1))
	$(eval DNAME=$(2))
	$(eval KEYSTORE=$(3))
	$(eval PASSWORD=$(4))
	$(eval FOLDER=$(5))
	$(eval KEY_SIZE=$(6))
	$(eval KEY_SIGNING_ALGO=$(7))
	$(eval KEY_ALGO=$(8))

	@echo "\nImport $(ALIAS) signed certificate into $(KEYSTORE)\n"
	keytool -import -trustcacerts -noprompt -alias $(ALIAS) \
		-file "$(FOLDER)/$(ALIAS).pem"  \
		-keystore $(KEYSTORE) -storepass $(PASSWORD)

	@echo "\nExport $(ALIAS) private certificate for importing into a browser\n"
	keytool -importkeystore -srcalias $(ALIAS) \
		-srckeystore $(KEYSTORE) -srcstorepass $(PASSWORD) \
		-destkeystore "$(FOLDER)/$(ALIAS).p12" -deststorepass $(PASSWORD) \
		-deststoretype PKCS12

	@echo "\nExport $(ALIAS) certificate into .key\n"
	echo $(PASSWORD) > /tmp/pwd.txt
	openssl pkcs12 -in $(FOLDER)/$(ALIAS).p12  -nodes -nocerts -out $(FOLDER)/$(ALIAS).key -passin file:/tmp/pwd.txt
endef

#
# Create CSR and import PEM
#
create-csr: create-tpp-transport-csr create-tpp-signing-csr
import-pem: import-tpp-request-pem import-tpp-signing-pem

# Transport
create-tpp-transport-csr:
	@echo "\nGenerate a transport CSR\n"
	$(eval ALIAS=transport-$(TPP_SOFTWARE_ID))

	$(call generate_csr,$(ALIAS),$(DNAME_TPP_OB),\
	$(KEYSTORE_TPP),$(PASSWORD),$(FOLDER_TPP), \
	$(TRANSPORT_KEY_SIZE), $(TRANSPORT_KEY_SIGNING_ALGO), $(TRANSPORT_KEY_ALGO))

import-tpp-request-pem:
	@echo "\nImport the transport PEM\n"
	$(eval ALIAS=transport-$(TPP_SOFTWARE_ID))

	$(call import_csr,$(ALIAS),$(DNAME_TPP_OB),\
	$(KEYSTORE_TPP),$(PASSWORD),$(FOLDER_TPP), \
	$(TRANSPORT_KEY_SIZE), $(TRANSPORT_KEY_SIGNING_ALGO), $(TRANSPORT_KEY_ALGO))


# Signing
create-tpp-signing-csr:
	@echo "\nGenerate a signing CSR\n"
	$(eval ALIAS=signing-$(TPP_SOFTWARE_ID))

	$(call generate_csr,$(ALIAS),$(DNAME_TPP_OB),\
	$(KEYSTORE_TPP),$(PASSWORD),$(FOLDER_TPP), \
	$(TRANSPORT_KEY_SIZE), $(TRANSPORT_KEY_SIGNING_ALGO), $(TRANSPORT_KEY_ALGO))

import-tpp-signing-pem:
	@echo "\nimport the signing pem\n"
	$(eval ALIAS=signing-$(TPP_SOFTWARE_ID))

	$(call import_csr,$(ALIAS),$(DNAME_TPP_OB),\
	$(KEYSTORE_TPP),$(PASSWORD),$(FOLDER_TPP), \
	$(TRANSPORT_KEY_SIZE), $(TRANSPORT_KEY_SIGNING_ALGO), $(TRANSPORT_KEY_ALGO))

#Instructions after step 1
instructions-after-step-1:
	@echo "\n\n!!!! STEP 1 :SUCCESS!!!\n"
	@echo "TPP CSRs generated under $FOLDER_TPP. Import them into Open Banking directory\n"
	@echo "In Open Banking directory, export the keys into pem."
	@echo "Save the transport pem under $(FOLDER_TPP)/transport-$(TPP_SOFTWARE_ID).pem\n"
	@echo "and the signing pem under $(FOLDER_TPP)/signing-$(TPP_SOFTWARE_ID).pem\n"
	@echo "(be careful, Open Banking directory has a bug in their pem format export, you need to make sure the pem has a new line every 65 characters.)\n"
	@echo "When you are done, run 'make step2'\n"

#Instructions after step 2
instructions-after-step-2:
	@echo "\nYour keys are now ready to use! Use the keys generated into the folder '$(FOLDER_TPP)' for your TPP application."
	@echo "\n(PS: the script already copied the keys into the forgerock-openbanking-sample-tpp keystore folder ;) )"

#help
help:
	@echo "Makefile for generating keys and integrate them into Open Banking directory"
	@echo "Run 'make all' for starting the process and follow the instruction"

#
# Deploy
#
deploy:
	# copy keys into the forgerock sample keystore
	cp $(FOLDER_TPP)/* ${TPP_SAMPLE_KEYSTORE_FOLDER}/
#
# Clean
#
clean: clean-tpp

clean-tpp:
	# Remove tpp
	rm -rf ${FOLDER_TPP}
