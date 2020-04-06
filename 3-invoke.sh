#!/bin/bash
set -eo pipefail
FUNCTION=$(aws cloudformation describe-stack-resource --stack-name telecom-basic --logical-resource-id function --query 'StackResourceDetail.PhysicalResourceId' --output text)
if [ $1 ]
then
  case $1 in
    get-all)
      PAYLOAD=file://event-get.json
      ;;

    get-customer)
      PAYLOAD=file://event-get-customer.json
      ;;

    activate)
      PAYLOAD=file://event-activate.json
      ;;

    *)
      echo -n "Unknown event type"
      ;;
  esac
fi
if [ $PAYLOAD ]
then
  aws lambda invoke --function-name $FUNCTION --payload $PAYLOAD out.json
else
  aws lambda invoke --function-name $FUNCTION --payload file://event-get.json out.json
fi
cat out.json
