# Use the base image
FROM jetbrains/teamcity-agent

# Install dependencies and Ansible
USER root
RUN apt update && \
    apt install -y software-properties-common && \
    apt-add-repository --yes --update ppa:ansible/ansible && \
    apt install -y ansible

# Copy .ssh directory
COPY .ssh /home/buildagent/.ssh

# Set correct permissions
RUN chown -R buildagent:buildagent /home/buildagent/.ssh && \
    chmod +r /home/buildagent/.ssh && \
    chmod +r /home/buildagent/.ssh/*

# Switch back to the original user
USER buildagent

