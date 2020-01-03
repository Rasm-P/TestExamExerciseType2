[![Build Status](https://travis-ci.org/Rasm-P/testPipeline.svg?branch=master)](https://travis-ci.org/Rasm-P/testPipeline)

*This project is meant as start code for future projects and exercises.*

*Projects which are expected to use this start-code are projects that require all, or most of the following technologies:*
 - *JPA and REST*
- *Testing, including database test*
- *Testing, including tests of REST-API's*
- *CI and CONTINUOUS DELIVERY* 
- *More?*

### Preconditions
*In order to use this code, you should have a local developer setup + a "matching" droplet on Digital Ocean as described in the 3. semester guidelines* 

# Getting Started

This project contains two major documentation files: 
 - [First time users - getting started](README_proof_of_concept.md)
 - [How to use for future projects](README_how_to_use.md)
 
 In short, change the following:
 - pom.xml : Domain name
 - config.properties : names of both databases
 - .travis.yaml : name of test database
 - Travis, environment variables: REMOTE_USER + REMOTE_PW