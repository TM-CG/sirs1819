openssl genrsa -out ../intermediate/private/$1.key.pem 2048
openssl req -config ../intermediate/openssl.cnf -key ../intermediate/private/$1.key.pem -new -sha256 -out ../intermediate/csr/$1.csr.pem
openssl ca -config ../intermediate/openssl.cnf -extensions usr_cert -days 375 -notext -md sha256 -in ../intermediate/csr/$1.csr.pem -out ../intermediate/certs/$1.cert.pem






