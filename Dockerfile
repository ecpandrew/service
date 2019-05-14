############################################################
# Dockerfile to build Service container images
# Based on GlassFish 4.1 image
# File Author: Daniel Carvalho
# docker run --name service -tid -p 5001:8080 service
############################################################

################ Service BEGIN INSTALLATION ################

# Set the base image to GlassFish 4.1
FROM bonelli/glassfish-4.1

# Update the repository sources list
RUN apt-get update

# Install ant
RUN apt-get install -y ant

# Copy files
ADD . /service

# Set working directory
WORKDIR service/

# Copy script and make it executable
ADD run.sh ./run.sh
RUN sed -i -e 's/\r$//' ./run.sh
RUN chmod +x ./run.sh

# Build service
RUN ant -silent clean dist

# Expose the default port
#EXPOSE 8080

# Start run script
CMD ["./run.sh"]

################# Service INSTALLATION END #################