# Use the base image
FROM jetbrains/teamcity-agent

# Install dependencies and Ansible
USER root
RUN apt update && \
    apt install -y software-properties-common wget apt-transport-https gnupg lsb-release && \
    apt-add-repository --yes --update ppa:ansible/ansible && \
    apt install -y ansible

# Install Trivy
RUN wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | apt-key add - && \
    echo deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main | tee -a /etc/apt/sources.list.d/trivy.list && \
    apt update && \
    apt install -y trivy

# Copy .ssh directory
COPY .ssh /home/buildagent/.ssh

# Set correct permissions
RUN chown -R buildagent:buildagent /home/buildagent/.ssh && \
    chmod +r /home/buildagent/.ssh && \
    chmod +r /home/buildagent/.ssh/*

# Switch back to the original user
USER buildagent
