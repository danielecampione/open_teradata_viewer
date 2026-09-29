@echo off
setlocal
cd /d "%~dp0"

rem "run" already depends on "classes": Gradle compiles automatically if necessary,
rem so there is no need to call COMPILE-WINDOWS.bat separately.
call gradlew.bat run --offline
if not errorlevel 1 exit /b 0

rem Offline resolution failed, most likely because a dependency declared in
rem build.gradle is not in the local Gradle cache yet (first build on this
rem machine, or right after adding/changing a dependency). Retry once with
rem network access so Gradle can download and cache it; every build after
rem this one goes back to running fully offline.
echo Offline build failed, retrying online to fetch any missing dependencies...
call gradlew.bat run
if errorlevel 1 goto error
exit /b 0

:error
echo ERROR: startup failed.
pause
exit /b 1
