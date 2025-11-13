pipeline {
    agent any
    tools {
        jdk 'jdk17'
        maven 'maven3'
    }
    environment {
        GITHUB_CRED = 'GitHub-Token'
        DOCKERHUB_CRED = 'DockerHub-Token'
        SONAR_CRED = 'SonarQube-Token'
        DOCKER_IMAGE = 'shaith/springboot-cicd'
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
        
        stage('Clean & Compile') {  // MOVED THIS BEFORE SONARQUBE
            steps {
                sh "mvn clean compile"  // Compile first
            }
        }
        
        stage('SonarQube Analysis') {  // NOW RUNS AFTER COMPILATION
            steps {
                withCredentials([string(credentialsId: "${SONAR_CRED}", variable: 'SONAR_TOKEN')]) {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.token=$SONAR_TOKEN \
                        -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }
        
        stage('Package') {  // SEPARATE PACKAGING STAGE
            steps {
                sh "mvn package -DskipTests"
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

