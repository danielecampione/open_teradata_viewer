@echo off
setlocal
cd /d "%~dp0"

rem "test" already depends on "classes"/"testClasses". The JUnit 5 console
rem standalone jar is resolved from Maven Central (see build.gradle), no
rem local lib/junit5 jar is needed any more.
call gradlew.bat test --offline
if not errorlevel 1 goto done

rem Offline resolution failed, most likely because a dependency declared in
rem build.gradle is not in the local Gradle cache yet (first build on this
rem machine, or right after adding/changing a dependency). Retry once with
rem network access so Gradle can download and cache it; every build after
rem this one goes back to running fully offline.
echo Offline build failed, retrying online to fetch any missing dependencies...
call gradlew.bat test
if errorlevel 1 goto error

:done
echo TESTS COMPLETED.
exit /b 0

:error
echo ERROR: one or more tests failed.
pause
exit /b 1
