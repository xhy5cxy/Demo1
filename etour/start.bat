@echo off
title E旅游攻略系统后端服务
chcp 65001

echo ========================================
echo     E旅游攻略系统后端服务启动脚本
echo ========================================
echo.

REM 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未检测到Java环境，请先安装JDK 1.8或更高版本
    pause
    exit /b 1
)

REM 检查Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未检测到Maven环境，请先安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo 正在编译项目...
call mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo 错误：项目编译失败
    pause
    exit /b 1
)

echo.
echo 正在启动服务...
echo 访问地址：http://localhost:8888/api
echo API文档：http://localhost:8888/api/doc.html
echo.
echo 按Ctrl+C停止服务
echo.

REM 启动Spring Boot应用
call mvn spring-boot:run -Dspring-boot.run.profiles=dev

pause