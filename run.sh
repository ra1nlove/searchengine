#!/bin/bash

for (( ; 1;  ));
do
	if [[ -e "stop" ]]; then
		echo "finished"
		break;
	fi

	hadoop jar generate.jar
	echo $?

	hadoop jar fetch.jar
	echo $?

done