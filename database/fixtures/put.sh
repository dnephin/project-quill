#
# Temporary script to upload a fixture.
#
# Usage: ./put.sh <database> <filename>
#

url="http://localhost:5984/$1"
filename=$2

curl -X POST \
    -H "Content-Type: application/json" \
    -d @"$filename" $url
