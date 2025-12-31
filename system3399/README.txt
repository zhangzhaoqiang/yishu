root@yishu-Veriton-B430:/hcc/testapk/keystore# openssl pkcs8 -in platform.pk8 -inform DER -outform PEM -out platform.priv.pem -nocrypt
root@yishu-Veriton-B430:/hcc/testapk/keystore# openssl pkcs12 -export -in platform.x509.pem -inkey platform.priv.pem -out platform.pk12 -name yishu
root@yishu-Veriton-B430:/hcc/testapk/keystore# keytool -importkeystore -destkeystore platform.keystore -srckeystore platform.pk12 -srcstoretype PKCS12 -srcstorepass hzys123456 -alias yishu
