#! /bin/bash
date > $OPENSHIFT_JBOSSEWS_LOG_DIR/last_date_cron_ran
current=$(cd $(dirname $0) && pwd)
confdir=${current}/conf
libdir=${current}/lib
echo $current
echo $current
echo $libdir
java -Drootdir=${current} -Dconfdir=${confdir} -jar ${libdir}/sign.jar