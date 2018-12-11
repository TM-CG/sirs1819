openssl ca -config ../intermediate/openssl.cnf -extensions usr_cert -days 375 -notext -md sha256 -in ../intermediate/csr/$1.csr.pem -out ../intermediate/certs/$1.cert.pem




