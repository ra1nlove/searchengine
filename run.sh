#!/bin/bash

for (( ; 1;  ));
do
	if [[ -e "stop" ]]; then
		echo "finished"
		break;
	fi

	hadoop jar generate.jar
	echo $?

    echo "sleep 30s to start fetch"
    sleep 30;

	hadoop jar fetch.jar
	echo $?

    echo "sleep 30s to start generate"
    sleep 30;

    spark-submit --master yarn --class com.buptnsrc.search.job.PageRank --name pagerank PageRank.jar
	echo $?

    echo "sleep 30s to start generate"
    sleep 30;

done