#!/bin/sh
PROCESS=morpheus-web

DATE=`date +"%Y-%m-%d"`
FILES=/var/tmp/log/${PROCESS}/*
for FILE in ${FILES}
do
 if [[ ${FILE} != *${DATE}* ]]
 then
     gzip ${FILE}
 fi
done

scp -i /home/morpheus/.ssh/id_rsa /var/tmp/log/${PROCESS}/*log.gz hydra@10.179.69.191:/opt/data/logs/${PROCESS}/ > /dev/null 2>/dev/null
if [ $? -eq 0 ]; then
    rm -rf /var/tmp/log/${PROCESS}/*log.gz > /dev/null 2>/dev/null
    exit 0
else
    exit 1
fi
