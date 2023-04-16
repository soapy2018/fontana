@echo on
@echo =============================================================
@echo $                                                           $
@echo $               CQF Microservices-Platform                  $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  CQF All Right Reserved                                   $
@echo $  Copyright (C) 2021-2050                                  $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title CQF Microservices-Platform
@color 0e

call mvn clean package -Dmaven.test.skip=true

pause