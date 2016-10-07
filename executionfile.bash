#!/bin/bash
echo "Parallele mode"
cd bashfiles
# In case we are not in batch mode
# All file that correspond to _*_lsd.bash must be run with bash
for file in _*_lsd.bash
	do
	bash ${file} &
	echo ---
done

# In case we are in batch mode, we must find all heads of the threads
echo "batch mode"
REGEX="_*_lsd.bash"
i=0
for file in *_lsd.bash
	do
	if [ ! ${file} == $REGEX ]; then 
		# file does have a _
		isanhead=true
		for subfile in *_lsd.bash
			do
			# test if file is the same
			if [ ${file} != ${subfile} ]; then
				# test if file has first common charactere and if longer than an other file
				if [ ${file:0:1} == ${subfile:0:1} ] && [ ${#file} -gt ${#subfile} ]; then
					#echo "${file} is not head"
					isanhead=false
					break
				fi
			fi
		done
		if $isanhead ; then
			Heads[i]=${file}
			i=${i}+1
		fi
	fi
done

# now we have all heads, we can start them in parallel
for head in "${Heads[@]}"
	do
	../startbatchjob.bash "$head" &
done
