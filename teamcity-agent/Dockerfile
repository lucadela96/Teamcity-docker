# Use the base image
FROM jetbrains/teamcity-agent

# Install dependencies and Ansible
USER root
RUN apt update && \
    apt install -y software-properties-common && \
    apt-add-repository --yes --update ppa:ansible/ansible && \
    apt install -y ansible

# Switch back to the original user
USER buildagent

