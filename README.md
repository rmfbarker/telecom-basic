# Telecom Phone API Service 

A Telecom operator has a phone number database which stores phone numbers associated to customers. Each customer may 
 have multiple phone numbers.

This service provides the following capabilities:

   * get all phone numbers
   * get all phone numbers of a single customer
   * activate a phone number
  
The service is implemented as an AWS Lambda and each function is invoked by calling the lambda with a JSON payload.    
   
Response for each function contains the result, query that was passed in, and the (HTTP) status.

* 200 - successful invocation
* 400 - bad request e.g. unknown phone number
* 500 - server error
   
## Get all phone numbers
Returns all phone numbers for all customers.
   
Get all phone numbers is invoked by calling the service with the following event

    {
      "operation": "get-all"
    }
    
Sample response:

    {"result" {"3b2cb090-12b5-4bb8-aeaf-807b381c821" ["0423123123"],
               "3b2cb090-12b5-4bb8-aeaf-807b381c822" ["0423123124" "0423123125"],
               "3b2cb090-12b5-4bb8-aeaf-807b381c823" ["0423123126" "0423123127"]},
     "query" {"operation" "get-all"},
     "status" 200}    

## Get all customer phone numbers
    
To get a list of numbers for a customer the service is called with the customer ID. Any customer ID not in the database will trigger a 400 response.

Inputs:
- operation=get
- customer=[customer-id]

Outputs
- status
- query
- result - map of customer IDs to list of phone numbers

Sample invocation:

    {
      "operation": "get",
      "customer": "fec8c4ab-24cb-49ee-9a6a-50c5f9d8bf26"
    }
    
Sample response:

    {"status" 200,
     "result" ["0423123124" "0423123125"],
     "query" {"operation" "get", "customer" "3b2cb090-12b5-4bb8-aeaf-807b381c822"}}

    
## Activate phone number
    
To Activate a customer phone number the lambda is called with the customer ID and the number to associate/activate. 

Inputs:
- operation=activate
- customer=[customer-id]
- number=[phone-number]

Outputs
-

Sample invocation:

    {
      "operation": "activate",
      "customer": "fec8c4ab-24cb-49ee-9a6a-50c5f9d8bf26",
      "number": "0423123123"
    }

Sample response:

    {
        "query" {"operation" "activate", 
                 "customer" "3b2cb090-12b5-4bb8-aeaf-807b381c822", 
                 "number" "04230321234"},
        "status" 200
     }   
    
# Basic Function with Minimal Dependencies (Java)

The project source includes function code and supporting resources:
- `src/main` - A Java function.
- `src/test` - A unit test and helper classes.
- `template.yml` - An AWS CloudFormation template that creates an application.
- `build.gradle` - A Gradle build file.
- `1-create-bucket.sh`, `2-deploy.sh`, etc. - Shell scripts that use the AWS CLI to deploy and manage the application.

Use the following instructions to deploy the sample application.

# Requirements
- Java 11
- Gradle
- The Bash shell. For Linux and macOS, this is included by default. In Windows 10, you can install the [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) to get a Windows-integrated version of Ubuntu and Bash.
- [The AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# Setup

To create a new bucket for deployment artifacts, run `1-create-bucket.sh`. Or, if you already have a bucket, create a file named `bucket-name.txt` that contains the name of your bucket.

    telecom-basic$ ./1-create-bucket.sh
    make_bucket: lambda-artifacts-a5e4xmplb5b22e0d

# Deploy
To deploy the application, run `2-deploy.sh`.

    telecom-basic$ ./2-deploy.sh
    BUILD SUCCESSFUL in 1s
    Successfully packaged artifacts and wrote output template to file out.yml.
    Waiting for changeset to be created..
    Successfully created/updated stack - telecom-basic

This script uses AWS CloudFormation to deploy the Lambda functions and an IAM role. If the AWS CloudFormation stack that contains the resources already exists, the script updates it with any changes to the template or function code.

# Test
To invoke the function, run `3-invoke.sh`.

    telecom-basic$ ./3-invoke.sh 
    {
        "StatusCode": 200,
        "ExecutedVersion": "$LATEST"
    }
    "200 OK"

The application uses AWS X-Ray to trace requests. Open the [X-Ray console](https://console.aws.amazon.com/xray/home#/service-map) to view the service map.

Choose a node in the main function graph. Then choose **View traces** to see a list of traces. Choose any trace to view a timeline that breaks down the work done by the function.

Finally, view the application in the Lambda console.

*To view the output*
1. Open the [applications page](https://console.aws.amazon.com/lambda/home#/applications) in the Lambda console.
2. Choose **telecom-basic**.

# Cleanup
To delete the application, run `4-cleanup.sh`.

    telecom-basic$ ./4-cleanup.sh
