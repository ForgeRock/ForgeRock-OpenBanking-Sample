#!/bin/bash

#These values should not be changed
keys=(SIGNATURE ENCRYPTION TRANSPORT)
SELF_SIGNED_CA_ALIAS="selfSignedCA"
FOLDER_TPP=tpp
FOLDER_DIRECTORY=frdirectory
KEYSTORE_TPP="${FOLDER_TPP}/keystore.p12"
TRUSTORE_TPP="${FOLDER_TPP}/truststore.p12"
TRANSPORT_KEY_ALGO=RSA
TRANSPORT_KEY_SIZE=2048
TRANSPORT_KEY_SIGNING_ALGO=SHA256withRSA
VALIDITY=3650
OB_ROOT_CA_ALIAS="ForgeRockDirectory"

# The values need to be changed
SIGNATURE="d78dde4b-94d1-45d4-8593-04a48c03d828"
ENCRYPTION="12b2eb8e-5568-4162-a7fa-b09bc7e60b42"
TRANSPORT="8d02c194-8a58-4897-95de-aa478fe57380"
SOFTWARE_ID="5b055183decdcd28f6b6f68c"
DIRECTORY_CA="ForgeRock-directory"
PASSWORD="changeit"

# Check to see if JAVA_HOME is set
if [ -z $JAVA_HOME ]; then
    echo "JAVA_HOME not set!"
    exit 1
fi

# Create FOLDER_TPP
mkdir -p ${FOLDER_TPP}

# Checking to see we have the necesary key/public pairs
echo "Checking we the necessary key/public pairs"
ALLFOUND=true
for i in "${keys[@]}"
do
    echo "${i}"
    if [ ! -f "${FOLDER_DIRECTORY}/${!i}.key" ]; then
         echo "    ${FOLDER_DIRECTORY}/${!i}.key not found!"
         ALLFOUND=false
    else 
         echo "    ${FOLDER_DIRECTORY}/${!i}.key found"
    fi
    if [ ! -f "${FOLDER_DIRECTORY}/${!i}.pem" ]; then
         echo "    ${FOLDER_DIRECTORY}/${!i}.pem not found!"
         ALLFOUND=false
    else 
         echo "    ${FOLDER_DIRECTORY}/${!i}.pem found"
    fi
done
echo "DIRECTORY_CA"
if [ ! -f "${FOLDER_DIRECTORY}/${DIRECTORY_CA}.pem" ]; then
    echo "    ${FOLDER_DIRECTORY}/${DIRECTORY_CA}.pem not found!"
    ALLFOUND=false
else
    echo "    ${FOLDER_DIRECTORY}/${DIRECTORY_CA}.pem found"
fi

# If filesa are missing we should report it and exit
if [ "$ALLFOUND" == false ]; then
    echo "Some files are missing. Please check that the above files are downloaded."
    exit 1;
fi

# Convert the file key/public pair into a p12 for future import
echo "Converting keys into PKCS files"
for i in "${keys[@]}"
do
    if [ -f "${i}-${!i}.p12" ]; then
        rm "${i}-${!i}.p12"
    fi
    echo "    Converting ${i}"
    openssl pkcs12 -export -in "${FOLDER_DIRECTORY}/${!i}.pem" -inkey "${FOLDER_DIRECTORY}/${!i}.key" -name "${i}" -out "${FOLDER_TPP}/${i}-${!i}.p12" -password pass:${PASSWORD}
done

# Create the truststore
echo "Creating ${TRUSTORE_TPP}"
if [ -f "${TRUSTORE_TPP}" ]; then
    rm "${TRUSTORE_TPP}"
fi
$JAVA_HOME/bin/keytool -import -trustcacerts -noprompt -alias ${OB_ROOT_CA_ALIAS} -file "${FOLDER_DIRECTORY}/${DIRECTORY_CA}.pem" -keystore ${TRUSTORE_TPP} -storepass ${PASSWORD} -deststoretype pkcs12

# Create the keystore
echo "Creating ${KEYSTORE_TPP}"
if [ -f "${KEYSTORE_TPP}" ]; then
    rm "${KEYSTORE_TPP}"
fi
for i in "${keys[@]}"
do
    echo "    Importing ${FOLDER_TPP}/${i}-${!i}.p12"
    $JAVA_HOME/bin/keytool -importkeystore -deststorepass "${PASSWORD}" -destkeystore "${KEYSTORE_TPP}" -srckeystore "${FOLDER_TPP}/${i}-${!i}.p12" -srcstoretype PKCS12 -srcalias "${i}" -destalias "${i,,}-${SOFTWARE_ID}" -srcstorepass "${PASSWORD}" -deststoretype PKCS12
done
echo "    importing "${DIRECTORY_CA}.pem""
$JAVA_HOME/bin/keytool -import -trustcacerts -file "${FOLDER_DIRECTORY}/${DIRECTORY_CA}.pem" -alias "${OB_ROOT_CA_ALIAS}" -keystore "${KEYSTORE_TPP}" -storepass "${PASSWORD}" -noprompt

echo "Contents of ${KEYSTORE_TPP}"
$JAVA_HOME/bin/keytool -list -keystore "${KEYSTORE_TPP}" -storepass "${PASSWORD}"

echo "Contents of ${TRUSTSTORE_TPP}"
$JAVA_HOME/bin/keytool -list -keystore "${TRUSTORE_TPP}" -storepass "${PASSWORD}"