#!/bin/bash

function update(){
   job_name=$1
   main_class=$2
   replace_job="s_<finalName>.*</finalName>_<finalName>"$job_name"</finalName>_g"
   replace_main='s_<mainClass>.*</mainClass>_<mainClass>com.buptnsrc.search.job.'$main_class'</mainClass>_g'
   sed -i $replace_job  pom.xml
   sed -i $replace_main pom.xml
}

function build(){
   mvn package
}

function copy(){
   scp -r ./target/*jar hadoop@hadoop-master:/home/hadoop/job/
}

update 'generate' 'GeneratorJob'
build

update 'fetch' 'FetcherJob'
build

copy