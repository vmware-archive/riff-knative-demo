# Usage: node-create [artifact-name] [git-repo-path] [image-name]
# Example: node-create square Pivotal-Field-Engineering/riff-knative-demo node-fun-square

riff service delete $1

riff function create node $1 \
  --git-repo https://github.com/$2.git \
  --artifact $1.js \
  --image $DOCKER_ID/$3 \
  --verbose
  
