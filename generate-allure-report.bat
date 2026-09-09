@echo off
REM Generate Allure Report Script
REM This script runs tests and generates both Cucumber and Allure reports

echo.
echo ==========================================
echo  Selenium-RestAssured-Cucumber Framework
echo  Allure Report Generation
echo ==========================================
echo.

REM Step 1: Clean and run tests
echo [1/3] Running tests with Maven...
call mvn clean test
if %ERRORLEVEL% neq 0 (
    echo Error: Test execution failed!
    exit /b 1
)

REM Step 2: Generate Cucumber Reports
echo.
echo [2/3] Generating Cucumber Reports...
call mvn post-integration-test
if %ERRORLEVEL% neq 0 (
    echo Warning: Cucumber report generation had issues
)

REM Step 3: Generate Allure Report
echo.
echo [3/3] Generating Allure Report...
call mvn allure:report
if %ERRORLEVEL% neq 0 (
    echo Warning: Allure report generation had issues
)

REM Step 4: Open reports
echo.
echo ==========================================
echo  Reports Generated Successfully!
echo ==========================================
echo.
echo Cucumber Report: target/cucumber-reports/index.html
echo Allure Report: target/allure-report/index.html
echo.
echo Opening Allure Report in browser...
pause
start target\allure-report\index.html

echo.
echo Done!
pause
