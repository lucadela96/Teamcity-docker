package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

object BuildDeploy : BuildType({
    name = "Build & Deploy odoo 14.0"

    artifactRules = "artifacts/*"
    buildNumberPattern = "%vcsroot.branch%.%build.counter%"

    vcs {
        root(HttpsGithubComLucadela96odooCicdSampleRefsHeadsMain)
    }

    steps {
        dockerCommand {
            name = "Docker build"
            id = "DockerCommand"
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                namesAndTags = """
                    cs-nuc-04:5001/compassion-odoo:%vcsroot.branch%
                    cs-nuc-04:5001/compassion-odoo:%build.number%
                """.trimIndent()
                commandArgs = "--pull"
            }
        }
        script {
            name = "Trivy audit"
            id = "Trivy_audit"
            scriptContent = """
                mkdir -p artifacts
                script -q -c "trivy image --scanners vuln cs-nuc-04:5001/compassion-odoo:%vcsroot.branch%" artifacts/trivy-%vcsroot.branch%.log
            """.trimIndent()
        }
        dockerCommand {
            name = "Push to registry"
            id = "Push_to_registry"
            commandType = push {
                namesAndTags = """
                    cs-nuc-04:5001/compassion-odoo:%vcsroot.branch%
                    cs-nuc-04:5001/compassion-odoo:%build.number%
                """.trimIndent()
            }
        }
        script {
            name = "Ansible Deploy"
            id = "Ansible_Deploy"
            workingDir = "ansible"
            scriptContent = "ansible-playbook playbooks/deploy-nuc.yml -i inventory.ini --private-key=/home/buildagent/.ssh/keys/cp-nuc-04 -u zivi"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
        dockerSupport {
            enabled = false
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_2"
            }
        }
    }
})
