del app-release.apk
copy .\app\build\intermediates\apk\release\app-release-unsigned.apk .\
scp .\app-release-unsigned.apk root@192.168.0.222:/hcc/testapk/app-release-unsigned.apk
ssh root@192.168.0.222 "cd /hcc/testapk;/usr/lib/jvm/java-8-openjdk-amd64/bin/java -jar -Djava.library.path=. -jar signapk.jar platform.x509.pem platform.pk8 app-release-unsigned.apk app-release.apk"
scp root@192.168.0.222:/hcc/testapk/app-release.apk .\localnet.apk
