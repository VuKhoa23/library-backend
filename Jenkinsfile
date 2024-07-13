pipeline {

    agent any

    tools { 
        maven 'my-maven' 
    }
    stages {

        stage('Build with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java -version'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Packaging/Pushing image') {

            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t vukhoa23/library .'
                    sh 'docker push vukhoa23/library'
                }
            }
        }

        stage('Deploy Spring Boot to DEV') {
            steps {
                echo 'Deploying and cleaning'
                sh 'docker image pull vukhoa23/library'
                sh 'docker container stop khoa-library || echo "this container does not exist" '
                sh 'docker network create dev || echo "this network exists"'
                sh 'echo y | docker container prune '
                sh 'docker container run -d --rm --name khoa-library -p 8081:8081 vukhoa23/library'
            }
        }
 
    }
    post {
        // Clean after build
        always {
            cleanWs()
        }
    }
}