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

set /p version=«Î ‰»Î∞Ê±æ∫≈:

call mvn versions:set -DnewVersion=%version%

call mvn versions:commit

pause