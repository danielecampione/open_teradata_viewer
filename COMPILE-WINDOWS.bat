@echo off
setlocal
cd /d "%~dp0"

rem Compilation via Gradle (JAVA_HOME: if not set, uses the system default;
rem a JDK 11 or later is required).
call gradlew.bat classes --offline
if not errorlevel 1 goto done

rem Offline resolution failed, most likely because a dependency declared in
rem build.gradle is not in the local Gradle cache yet (first build on this
rem machine, or right after adding/changing a dependency). Retry once with
rem network access so Gradle can download and cache it; every build after
rem this one goes back to running fully offline.
echo Offline build failed, retrying online to fetch any missing dependencies...
call gradlew.bat classes
if errorlevel 1 goto error

:done
echo COMPILATION COMPLETED.
exit /b 0

:error
echo ERROR: compilation failed.
pause
exit /b 1
