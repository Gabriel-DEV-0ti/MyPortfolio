@echo off
title Portfolio Backend (Java + H2 Database)
echo ===================================================
echo   Iniciando Backend Java (Spring Boot + H2 DB)
echo   Porta: 8080
echo ===================================================
cd /d "%~dp0\backend"
java -jar target/portfolio-backend-1.0.0.jar
pause
