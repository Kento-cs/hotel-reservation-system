@echo off
cd /d %~dp0

echo Compiling...
javac -encoding UTF-8 -cp lib\hsqldb.jar -d bin -sourcepath src\main\java src\main\java\web\HrsHttpServer.java
if errorlevel 1 (
    echo.
    echo Compile failed. Make sure the JDK is installed and javac is on PATH.
    pause
    exit /b 1
)

echo Starting server...
start "HRS Server" cmd /k java -Dstdout.encoding=UTF-8 -cp bin;lib\hsqldb.jar web.HrsHttpServer

timeout /t 2 /nobreak > nul
start http://localhost:8080/

echo.
echo Opened http://localhost:8080/ in your browser.
echo To stop the server, close the "HRS Server" window.
echo.
pause
