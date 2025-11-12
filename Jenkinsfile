pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {
        stage('Code Checkout') {
            steps {
                git branch: 'main', 
                    changelog: false, 
                    poll: false, 
                    url: 'https://github.com/Shaith-Ahamed/sprinboot-cicd-check.git', 
                    credentialsId: 'github-pat'  // Add your GitHub PAT credentials ID
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
                sh '''
                    mvn sonar:sonar \
                        -Dsonar.host.url=http://localhost:9000/ \
                        -Dsonar.login=squ_9bd7c664e4941bd4e7670a88ed93d68af40b42a3
                '''
            }
        }

        stage('Clean & Package') {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }

        stage("Docker Build & Push") {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'DockerHub-Token', toolName: 'docker') {
                        def imageName = "spring-boot-prof-management"
                        def buildTag = "${imageName}:${BUILD_NUMBER}"
                        def latestTag = "${imageName}:latest"
                        
                        // Build Docker image
                        sh "docker build -t ${imageName} -f Dockerfile.final ."
                        
                        // Tag and push to DockerHub
                        sh "docker tag ${imageName} shaith/${buildTag}"
                        sh "docker tag ${imageName} shaith/${latestTag}"
                        sh "docker push shaith/${buildTag}"
                        sh "docker push shaith/${latestTag}"
                        
                        env.BUILD_TAG = buildTag
                    }
                }
            }
        }

        stage('Vulnerability Scanning') {
            steps {
                sh "trivy image shaith/${BUILD_TAG}"
            }
        }

        stage("Staging") {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
}
