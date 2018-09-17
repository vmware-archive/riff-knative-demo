# Usage: java-create [artifact-name] [git-repo-path] [image-name]
# Example: java-create uppercase Pivotal-Field-Engineering/riff-knative-demo java-fun-uppercase

riff service delete $1

riff function create java $1 \
  --git-repo https://github.com/$2.git \
  --artifact $1.jar \
  --handler $1 \
  --image cepage/$3
 
