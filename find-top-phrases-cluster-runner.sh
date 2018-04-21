#!/bin/bash

usage(){
	echo "Usage: $0 --pathsToTextToAnalyze [path_1] [path_2] [path_n-1] [path_n]"
	exit 1
}

if [[ "$#" -lt 2 ]]; then
    usage
fi

yarn_queue=$2

echo "starting job for yarn_queue : $yarn_queue"

spark-submit \
--class com.yunjae.spark.task.FindTopPhrasesJob \
--master local[*] \
target/scala-2.11/scala-big-data-spark*.jar $@