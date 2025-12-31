:: 获取设备号并将其存储在device.txt文件中
%cd%\adb devices > %cd%\devices.txt
::使用/f循环语句遍历文件内容获取设备号，skip=1为跳过第一行，因为第一行为List，并将其作为参数传入test2.bat
for /f "skip=1" %%a in (devices.txt) do call %cd%\adb -s %%a install -r %cd%\localnet.apk
pause