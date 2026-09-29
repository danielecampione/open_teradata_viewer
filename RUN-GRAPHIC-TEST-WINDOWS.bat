@echo off
setlocal
cd /d "%~dp0"

call RUN-TEST-WINDOWS.bat
if errorlevel 1 goto error

rem Requires a truly visible desktop session (it does not work on a
rem headless machine / without a graphical session, nor through a
rem disconnected Remote Desktop session): it actually opens the SplashScreen
rem and captures it with Robot.
call gradlew.bat robotGuiTest --offline
if not errorlevel 1 goto done

rem Offline resolution failed, most likely because a dependency declared in
rem build.gradle is not in the local Gradle cache yet (first build on this
rem machine, or right after adding/changing a dependency). Retry once with
rem network access so Gradle can download and cache it; every build after
rem this one goes back to running fully offline.
echo Offline build failed, retrying online to fetch any missing dependencies...
call gradlew.bat robotGuiTest
if errorlevel 1 goto error

:done
pause
exit /b 0

:error
echo ERROR: graphical test failed.
pause
exit /b 1
