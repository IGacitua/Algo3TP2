#!/bin/bash
set -e

mvn -B -ntp test-compile org.pitest:pitest-maven:mutationCoverage -DverboseLogging=true -Dverbose=true