pipeline {
    agent any

    tools {
        jdk 'jdk17'       // Jenkins JDK tool name
        maven 'maven3'    // Jenkins Maven tool name
    }

    environment {
        GITHUB_CRED = 'GitHub-Token'      // Jenkins credential ID for GitHub
        DOCKERHUB_CRED = 'DockerHub-Token' // Jenkins credential ID for DockerHub
        SONAR_TOKEN = 'SonarQube-Token'    // Jenkins credential ID for SonarQube
        DOCKER_IMAGE = 'shaith/springboot-cicd' // Docker image name
    }

    stages {
        stage('Code Checkout') {
            steps {
                git branch: 'main',
                    changelog: false,
                    poll: false,
                    url: 'https://github.com/Shaith-Ahamed/sprinboot-cicd-check.git',
                    credentialsId: "${GITHUB_CRED}"
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '--scan ./ --format HTML', odcInstallation: 'db-check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh """
                   mvn sonar:sonar \
                   -Dsonar.host.url=http://localhost:9000/ \
                   -Dsonar.login=${SONAR_TOKEN}
                """
            }
        }

        stage('Clean & Package') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: "${DOCKERHUB_CRED}", url: '') {
                        def buildTag = "${DOCKER_IMAGE}:${BUILD_NUMBER}"
                        def latestTag = "${DOCKER_IMAGE}:latest"

                        sh "docker build -t ${DOCKER_IMAGE} -f Dockerfile.final ."
                        sh "docker tag ${DOCKER_IMAGE} ${buildTag}"
                        sh "docker tag ${DOCKER_IMAGE} ${latestTag}"
                        sh "docker push ${buildTag}"
                        sh "docker push ${latestTag}"
                        env.BUILD_TAG = buildTag
                    }
                }
            }
        }

        stage('Vulnerability Scanning') {
            steps {
                sh "trivy image ${env.BUILD_TAG}"
            }
        }

        stage('Staging Deployment') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
