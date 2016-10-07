#!/bin/bash
echo "Job started $1"
# start the job
bash $1
# Once it's done, we can search for all direct child
# First look at the last charactere before _lsd.bash
lenght=${#1}
index="$(($lenght - 9))"
#echo $index

REGEX="_*_lsd.bash"
i=0
start=${1:0:$index}
echo $start
for files in "${start}*_lsd.bash"
	do
	for file in $files
		do
		# test if the file is not this one and that lenght is 1 more (means direct child)
		newlenght="$((${#file} - 1 ))"
		if [ $1 != $file ] && [ ${newlenght} -eq  ${lenght} ] ; then
			# Start all direct child in parallel
			../startbatchjob.bash ${file} &
		fi
	done
done

