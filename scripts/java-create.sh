# Usage: java-create [artifact-name] [image-name]
# Example: java-create uppercase java-fun-uppercase

riff service delete $1

riff function create java $1 \
  --local-path textdisplay \
  --image $DOCKER_ID/$2 \
  --verbose
